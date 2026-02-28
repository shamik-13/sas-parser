#!/usr/bin/env python3
"""Batch-test SAS files in one or more folders against the current grammar.

Usage:
    uv run python scripts/batch_test.py folder1 [folder2 ...]

Folder names are relative to sources/sas_source_codes/.
Produces a JSON report in reports/ and prints a human-readable summary.
"""

import json
import os
import subprocess
import sys
from collections import defaultdict
from datetime import datetime

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCES_DIR = os.path.join(PROJECT_DIR, "sources", "sas_source_codes")
JAR_PATH = os.path.join(PROJECT_DIR, "tools", "sas-parser.jar")


def find_sas_files(folders: list[str]) -> list[str]:
    """Find all .sas files in the specified folders."""
    files = []
    for folder in folders:
        folder_path = os.path.join(SOURCES_DIR, folder)
        if not os.path.isdir(folder_path):
            print(f"WARNING: Folder not found: {folder_path}", file=sys.stderr)
            continue
        for entry in sorted(os.listdir(folder_path)):
            if entry.lower().endswith(".sas"):
                files.append(os.path.join(folder_path, entry))
    return files


def _java_available() -> bool:
    """Check if Java parser JAR exists and SAS_PARSER_PYTHON_ONLY is not set."""
    if os.environ.get("SAS_PARSER_PYTHON_ONLY"):
        return False
    return os.path.isfile(JAR_PATH)


def validate_batch_java(files: list[str]) -> dict[str, dict | None]:
    """Validate all files in a single JVM invocation. Returns {filepath: error_dict_or_None}."""
    cmd = ["java", "-jar", JAR_PATH, "validate", "--batch"]
    if sys.stderr.isatty():
        cmd.append("--progress")
    cmd.extend(files)
    result = subprocess.run(cmd, stdout=subprocess.PIPE, text=True, timeout=1800)
    results = {}
    for line in result.stdout.strip().split("\n"):
        if not line:
            continue
        obj = json.loads(line)
        filepath = obj["file"]
        if obj["status"] == "pass":
            results[filepath] = None
        else:
            results[filepath] = {
                "file": filepath,
                "error_count": obj.get("error_count", 0),
                "errors": obj.get("errors", []),
            }
    return results


def validate_file(filepath: str) -> dict | None:
    """Run validate.py on a file. Returns error dict or None for pass."""
    result = subprocess.run(
        [sys.executable, os.path.join(PROJECT_DIR, "scripts", "validate.py"), filepath],
        capture_output=True,
        text=True,
    )
    if result.returncode == 0:
        return None

    try:
        return json.loads(result.stdout)
    except json.JSONDecodeError:
        return {
            "file": filepath,
            "error_count": 1,
            "errors": [{"line": 0, "column": 0, "message": result.stdout[:500], "source_line": "", "context": ""}],
        }


def error_cluster_key(msg: str) -> str:
    """Extract a cluster key from an error message."""
    for prefix in [
        "mismatched input",
        "no viable alternative at input",
        "extraneous input",
        "missing",
        "token recognition error",
    ]:
        if msg.lower().startswith(prefix):
            return prefix
    # Return first 60 chars as cluster key for other errors
    return msg[:60]


def run_batch(folders: list[str]) -> dict:
    """Run batch validation and return report dict."""
    files = find_sas_files(folders)
    if not files:
        print("No .sas files found in specified folders.", file=sys.stderr)
        sys.exit(1)

    passed = []
    failed = []
    error_clusters = defaultdict(lambda: {"count": 0, "files": [], "sample_error": None})

    # Try Java batch mode (single JVM, all files at once)
    use_java = _java_available()
    if use_java:
        try:
            print(f"Testing {len(files)} files (Java parser)...")
            java_results = validate_batch_java(files)
            for filepath in files:
                result = java_results.get(filepath)
                if result is None:
                    passed.append(filepath)
                else:
                    failed.append(filepath)
                    if result.get("errors"):
                        first_error = result["errors"][0]
                        key = error_cluster_key(first_error["message"])
                        cluster = error_clusters[key]
                        cluster["count"] += 1
                        cluster["files"].append(filepath)
                        if cluster["sample_error"] is None:
                            cluster["sample_error"] = first_error
        except Exception as e:
            print(f"Java batch failed ({e}), falling back to Python...", file=sys.stderr)
            use_java = False
            passed.clear()
            failed.clear()
            error_clusters.clear()

    if not use_java:
        try:
            from tqdm import tqdm
            file_iter = tqdm(files, desc="Validating", unit="file")
        except ImportError:
            file_iter = files
            print(f"Testing {len(files)} files...")

        for filepath in file_iter:
            result = validate_file(filepath)
            if result is None:
                passed.append(filepath)
            else:
                failed.append(filepath)
                if result.get("errors"):
                    first_error = result["errors"][0]
                    key = error_cluster_key(first_error["message"])
                    cluster = error_clusters[key]
                    cluster["count"] += 1
                    cluster["files"].append(filepath)
                    if cluster["sample_error"] is None:
                        cluster["sample_error"] = first_error

    # Build report
    total = len(files)
    pass_rate = len(passed) / total * 100 if total > 0 else 0

    clusters_list = []
    for pattern, data in sorted(error_clusters.items(), key=lambda x: -x[1]["count"]):
        clusters_list.append({
            "pattern": pattern,
            "count": data["count"],
            "files": data["files"][:10],  # Limit to 10 files per cluster
            "sample_error": data["sample_error"],
        })

    report = {
        "timestamp": datetime.now().isoformat(),
        "folders": folders,
        "summary": {
            "total": total,
            "passed": len(passed),
            "failed": len(failed),
            "pass_rate": round(pass_rate, 1),
        },
        "passed": [os.path.relpath(f, PROJECT_DIR) for f in passed],
        "error_clusters": clusters_list,
    }

    return report


def print_summary(report: dict):
    """Print a human-readable summary of the report."""
    s = report["summary"]
    print(f"\n{'='*60}")
    print(f"Batch Test Results — {report['timestamp']}")
    print(f"{'='*60}")
    print(f"Folders: {', '.join(report['folders'])}")
    print(f"Total:   {s['total']} files")
    print(f"Passed:  {s['passed']} ({s['pass_rate']}%)")
    print(f"Failed:  {s['failed']}")
    print()

    if report["error_clusters"]:
        print(f"{'Error Clusters':}")
        print(f"{'-'*60}")
        for i, cluster in enumerate(report["error_clusters"], 1):
            print(f"\n  {i}. [{cluster['count']} files] {cluster['pattern']}")
            if cluster["sample_error"]:
                err = cluster["sample_error"]
                print(f"     Sample: line {err['line']}: {err['message'][:80]}")
                if err["source_line"]:
                    print(f"     Source:  {err['source_line'][:80]}")
            # Show first 3 files
            for f in cluster["files"][:3]:
                print(f"     - {os.path.relpath(f, PROJECT_DIR)}")
            if cluster["count"] > 3:
                print(f"     ... and {cluster['count'] - 3} more")


def main():
    if len(sys.argv) < 2:
        print("Usage: uv run python scripts/batch_test.py folder1 [folder2 ...]", file=sys.stderr)
        sys.exit(2)

    folders = sys.argv[1:]
    report = run_batch(folders)

    # Save report
    reports_dir = os.path.join(PROJECT_DIR, "reports")
    os.makedirs(reports_dir, exist_ok=True)
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    report_path = os.path.join(reports_dir, f"batch_{timestamp}.json")
    with open(report_path, "w") as f:
        json.dump(report, f, indent=2)

    print_summary(report)
    print(f"\nFull report: {report_path}")

    # Exit with 0 if all pass, 1 if any failures
    sys.exit(0 if report["summary"]["failed"] == 0 else 1)


if __name__ == "__main__":
    main()
