#!/usr/bin/env python3
"""Orchestrate a full grammar development iteration.

Usage:
    uv run python scripts/orchestrate.py folder1 [folder2 ...]

Workflow:
    1. Build grammar (scripts/build.sh)
    2. Batch test (scripts/batch_test.py)
    3. Minimize top failing clusters
    4. Produce actionable iteration report
"""

import json
import os
import subprocess
import sys
from datetime import datetime

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# ANTLR error pattern hints for the agent
ERROR_HINTS = {
    "mismatched input": (
        "The parser expected a specific token but got something else. "
        "Likely fix: add the unexpected token as an alternative in the relevant parser rule, "
        "or add it to the 'identifier' rule if it's a keyword used as an identifier."
    ),
    "no viable alternative at input": (
        "No parser rule matches this input sequence. "
        "Likely fix: add a new parser rule or alternative to handle this construct. "
        "Check if this is a new SAS statement type that needs its own rule."
    ),
    "extraneous input": (
        "An extra token appeared where nothing was expected. "
        "Likely fix: the preceding rule needs to consume more tokens, "
        "or there's a missing alternative in the current rule."
    ),
    "missing": (
        "The parser expected a token that wasn't there. "
        "Likely fix: make the expected token optional in the rule, "
        "or add an alternative path that doesn't require it."
    ),
    "token recognition error": (
        "The lexer couldn't match a character/sequence. "
        "Likely fix: add a new lexer rule for this character or pattern."
    ),
}


def run_build() -> bool:
    """Run build.sh. Returns True on success."""
    print("=" * 60)
    print("Step 1: Building grammar...")
    print("=" * 60)

    result = subprocess.run(
        ["bash", os.path.join(PROJECT_DIR, "scripts", "build.sh")],
        capture_output=True,
        text=True,
    )

    if result.returncode != 0:
        print("GRAMMAR BUILD FAILED!", file=sys.stderr)
        print(result.stdout)
        print(result.stderr, file=sys.stderr)
        return False

    print(result.stdout)
    return True


def run_batch_test(folders: list[str]) -> dict | None:
    """Run batch_test.py. Returns the report dict or None on error."""
    print()
    print("=" * 60)
    print("Step 2: Batch testing...")
    print("=" * 60)

    # Run batch_test and capture its report
    result = subprocess.run(
        [sys.executable, os.path.join(PROJECT_DIR, "scripts", "batch_test.py")] + folders,
        capture_output=True,
        text=True,
    )

    # Print its output
    print(result.stdout)
    if result.stderr:
        print(result.stderr, file=sys.stderr)

    # Find the most recent report file
    reports_dir = os.path.join(PROJECT_DIR, "reports")
    if not os.path.isdir(reports_dir):
        return None

    report_files = sorted(
        [f for f in os.listdir(reports_dir) if f.startswith("batch_")],
        reverse=True,
    )
    if not report_files:
        return None

    with open(os.path.join(reports_dir, report_files[0])) as f:
        return json.load(f)


def run_minimize(filepath: str) -> dict | None:
    """Run minimize.py on a file. Returns info dict or None."""
    result = subprocess.run(
        [sys.executable, os.path.join(PROJECT_DIR, "scripts", "minimize.py"), filepath],
        capture_output=True,
        text=True,
        timeout=120,
    )

    # Find the repro file
    failures_dir = os.path.join(PROJECT_DIR, "failures")
    if os.path.isdir(failures_dir):
        repro_files = sorted(os.listdir(failures_dir), reverse=True)
        basename = os.path.splitext(os.path.basename(filepath))[0]
        for rf in repro_files:
            if basename in rf:
                repro_path = os.path.join(failures_dir, rf)
                with open(repro_path) as f:
                    content = f.read()
                return {
                    "original": filepath,
                    "repro_file": repro_path,
                    "repro_lines": content.count("\n") + 1,
                    "repro_content": content[:500],
                }

    return {"original": filepath, "repro_file": None, "repro_content": result.stdout[:300]}


def get_error_hint(pattern: str) -> str:
    """Get a hint for a given error pattern."""
    for key, hint in ERROR_HINTS.items():
        if key in pattern.lower():
            return hint
    return "Examine the error context and the relevant grammar rule."


def main():
    if len(sys.argv) < 2:
        print("Usage: uv run python scripts/orchestrate.py folder1 [folder2 ...]", file=sys.stderr)
        sys.exit(2)

    folders = sys.argv[1:]

    # Step 1: Build
    if not run_build():
        sys.exit(1)

    # Step 2: Batch test
    report = run_batch_test(folders)
    if report is None:
        print("ERROR: Could not read batch test report.", file=sys.stderr)
        sys.exit(1)

    summary = report["summary"]

    # If all pass, we're done
    if summary["failed"] == 0:
        print("\nAll files pass! Grammar is complete for these folders.")
        sys.exit(0)

    # Step 3: Minimize top error clusters
    print()
    print("=" * 60)
    print("Step 3: Minimizing top failures...")
    print("=" * 60)

    minimizations = []
    clusters = report.get("error_clusters", [])

    for cluster in clusters[:5]:  # Top 5 clusters
        if not cluster["files"]:
            continue
        # Pick the smallest file as representative
        rep_file = cluster["files"][0]
        print(f"\nMinimizing for pattern: '{cluster['pattern']}' ({cluster['count']} files)")
        print(f"  Representative: {os.path.relpath(rep_file, PROJECT_DIR)}")

        try:
            mini = run_minimize(rep_file)
            if mini:
                minimizations.append({
                    "cluster_pattern": cluster["pattern"],
                    "cluster_count": cluster["count"],
                    **mini,
                })
        except subprocess.TimeoutExpired:
            print(f"  Minimize timed out for {rep_file}")
        except Exception as e:
            print(f"  Minimize failed: {e}")

    # Step 4: Build iteration report
    print()
    print("=" * 60)
    print("ITERATION SUMMARY")
    print("=" * 60)

    iteration_report = {
        "timestamp": datetime.now().isoformat(),
        "folders": folders,
        "summary": summary,
        "error_clusters": [],
        "minimizations": minimizations,
    }

    for cluster in clusters:
        iteration_report["error_clusters"].append({
            "pattern": cluster["pattern"],
            "count": cluster["count"],
            "hint": get_error_hint(cluster["pattern"]),
            "sample_error": cluster.get("sample_error"),
            "sample_files": [os.path.relpath(f, PROJECT_DIR) for f in cluster["files"][:5]],
        })

    # Save iteration report
    reports_dir = os.path.join(PROJECT_DIR, "reports")
    os.makedirs(reports_dir, exist_ok=True)
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    report_path = os.path.join(reports_dir, f"iteration_{timestamp}.json")
    with open(report_path, "w") as f:
        json.dump(iteration_report, f, indent=2)

    # Print actionable summary
    print(f"\nPass rate: {summary['passed']}/{summary['total']} ({summary['pass_rate']}%)")
    print(f"Failed:    {summary['failed']} files")
    print()

    print("ACTION ITEMS (fix in priority order):")
    print("-" * 60)
    for i, cluster in enumerate(iteration_report["error_clusters"][:5], 1):
        print(f"\n{i}. [{cluster['count']} files] {cluster['pattern']}")
        print(f"   Hint: {cluster['hint']}")
        if cluster["sample_error"]:
            err = cluster["sample_error"]
            print(f"   Sample error at line {err['line']}: {err['message'][:100]}")
            if err["source_line"]:
                print(f"   Source: {err['source_line'][:100]}")

    if minimizations:
        print()
        print("MINIMAL REPRODUCTIONS:")
        print("-" * 60)
        for mini in minimizations:
            print(f"\n  Pattern: {mini['cluster_pattern']} ({mini['cluster_count']} files)")
            if mini.get("repro_file"):
                print(f"  Repro:   {os.path.relpath(mini['repro_file'], PROJECT_DIR)} ({mini['repro_lines']} lines)")
                print(f"  Content preview:")
                for line in mini["repro_content"].split("\n")[:10]:
                    print(f"    {line}")
            else:
                print(f"  (minimization did not produce a file)")

    print(f"\nFull report: {report_path}")
    print(f"\nNext: Fix the grammar rules based on the above, then run this script again.")


if __name__ == "__main__":
    main()
