#!/usr/bin/env python3
"""Verification helper for the fallback-fixing harness.

Runs between subagent iterations to check regression state, run targeted
audits, and compare before/after metrics.

Usage:
    uv run python scripts/run_fix_harness.py --status
    uv run python scripts/run_fix_harness.py --verify-fix <category_id> --folders folder1 folder2
    uv run python scripts/run_fix_harness.py --final-verify
"""

import json
import os
import subprocess
import sys
from datetime import datetime

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
REPORTS_DIR = os.path.join(PROJECT_DIR, "reports")
WORKORDERS_DIR = os.path.join(REPORTS_DIR, "workorders")

ALL_FOLDERS = [
    "sasjs_tests",
    "general_purpose_macros",
    "sasjs_base_macros",
    "sasjs_platform_macros",
    "sasjs_ddl_fcmp_lua",
    "packages_framework",
    "clinical_contributed",
    "clinical_examples",
    "clinical_tested",
    "clinical_whitepapers",
]


def run_batch_test(folders: list[str]) -> dict:
    """Run batch_test.py on folders and return the summary."""
    result = subprocess.run(
        [sys.executable, os.path.join(PROJECT_DIR, "scripts", "batch_test.py")] + folders,
        capture_output=True,
        text=True,
        timeout=600,
    )

    # Find latest batch report
    report_files = sorted(
        [f for f in os.listdir(REPORTS_DIR) if f.startswith("batch_")],
        reverse=True,
    )
    if not report_files:
        return {"error": "No batch report generated", "stdout": result.stdout[-300:]}

    with open(os.path.join(REPORTS_DIR, report_files[0])) as f:
        report = json.load(f)

    return report["summary"]


def run_audit(folders: list[str]) -> dict:
    """Run audit_fallbacks.py on folders and return occurrence counts by rule."""
    result = subprocess.run(
        [sys.executable, os.path.join(PROJECT_DIR, "scripts", "audit_fallbacks.py")] + folders,
        capture_output=True,
        text=True,
        timeout=600,
    )

    # Find latest audit report
    report_files = sorted(
        [f for f in os.listdir(REPORTS_DIR) if f.startswith("audit_")],
        reverse=True,
    )
    if not report_files:
        return {"error": "No audit report generated"}

    with open(os.path.join(REPORTS_DIR, report_files[0])) as f:
        report = json.load(f)

    # Count by rule
    counts = {}
    for occ in report["occurrences"]:
        rule = occ["rule"]
        counts[rule] = counts.get(rule, 0) + 1

    return {
        "total": len(report["occurrences"]),
        "by_rule": counts,
        "report_file": report_files[0],
    }


def load_baseline() -> dict | None:
    """Load the baseline metrics saved from the initial categorization."""
    baseline_path = os.path.join(REPORTS_DIR, "harness_baseline.json")
    if os.path.exists(baseline_path):
        with open(baseline_path) as f:
            return json.load(f)
    return None


def save_baseline(regression: dict, audit: dict):
    """Save baseline metrics for later comparison."""
    baseline = {
        "timestamp": datetime.now().isoformat(),
        "regression": regression,
        "audit": audit,
    }
    baseline_path = os.path.join(REPORTS_DIR, "harness_baseline.json")
    with open(baseline_path, "w") as f:
        json.dump(baseline, f, indent=2)


def load_processing_order() -> list[dict]:
    """Load the processing order manifest."""
    path = os.path.join(REPORTS_DIR, "processing_order.json")
    if os.path.exists(path):
        with open(path) as f:
            return json.load(f)
    return []


def load_fix_log() -> list[dict]:
    """Load the fix log tracking which categories have been processed."""
    path = os.path.join(REPORTS_DIR, "harness_fix_log.json")
    if os.path.exists(path):
        with open(path) as f:
            return json.load(f)
    return []


def save_fix_log(log: list[dict]):
    """Save the fix log."""
    path = os.path.join(REPORTS_DIR, "harness_fix_log.json")
    with open(path, "w") as f:
        json.dump(log, f, indent=2)


def log_fix_result(category_id: str, status: str, details: dict):
    """Append a fix result to the log."""
    log = load_fix_log()
    log.append({
        "timestamp": datetime.now().isoformat(),
        "category_id": category_id,
        "status": status,  # "success", "reverted", "skipped"
        **details,
    })
    save_fix_log(log)


def cmd_status():
    """Show current state (compact, for main agent context)."""
    processing_order = load_processing_order()
    fix_log = load_fix_log()
    baseline = load_baseline()

    fixed_ids = {entry["category_id"] for entry in fix_log if entry["status"] == "success"}
    reverted_ids = {entry["category_id"] for entry in fix_log if entry["status"] == "reverted"}
    remaining = [p for p in processing_order if p["category_id"] not in fixed_ids and p["category_id"] not in reverted_ids]

    status = {
        "baseline": baseline is not None,
        "categories_total": len(processing_order),
        "categories_fixed": sorted(fixed_ids),
        "categories_reverted": sorted(reverted_ids),
        "categories_remaining": [{"id": r["category_id"], "count": r["count"]} for r in remaining],
    }

    print(json.dumps(status, indent=2))


def cmd_verify_fix(category_id: str, folders: list[str]):
    """Verify a fix: run regression + audit on affected folders."""
    print(f"Verifying fix for: {category_id}")
    print(f"Folders: {', '.join(folders)}")
    print()

    # Run regression test
    print("Running regression test...")
    regression = run_batch_test(folders)
    regression_ok = regression.get("failed", 1) == 0

    print(f"  Regression: {regression.get('passed', '?')}/{regression.get('total', '?')} — {'OK' if regression_ok else 'FAILED'}")

    # Run audit
    print("Running audit...")
    audit = run_audit(folders)
    print(f"  Fallbacks: {audit.get('total', '?')} total")
    if "by_rule" in audit:
        for rule, count in sorted(audit["by_rule"].items()):
            print(f"    {rule}: {count}")

    # Build result
    result = {
        "category_id": category_id,
        "regression": regression,
        "regression_ok": regression_ok,
        "audit": audit,
    }

    # Log result
    if regression_ok:
        log_fix_result(category_id, "success", {
            "regression": regression,
            "audit_total": audit.get("total"),
        })
    else:
        log_fix_result(category_id, "reverted", {
            "regression": regression,
            "reason": "regression detected",
        })
        print(f"\nREGRESSION DETECTED — revert grammar changes:")
        print(f"  git checkout grammar/SasParser.g4 grammar/SasLexer.g4")

    print(f"\n{json.dumps(result, indent=2)}")


def cmd_init_baseline():
    """Initialize baseline by running full regression + audit."""
    print("Initializing baseline (full regression + audit on all folders)...")
    print()

    print("Running full regression test...")
    regression = run_batch_test(ALL_FOLDERS)
    print(f"  Regression: {regression.get('passed', '?')}/{regression.get('total', '?')}")
    print()

    print("Running full audit...")
    audit = run_audit(ALL_FOLDERS)
    print(f"  Fallbacks: {audit.get('total', '?')} total")
    if "by_rule" in audit:
        for rule, count in sorted(audit["by_rule"].items()):
            print(f"    {rule}: {count}")

    save_baseline(regression, audit)
    print(f"\nBaseline saved to reports/harness_baseline.json")


def cmd_final_verify():
    """Run full verification: all folders regression + audit + comparison."""
    baseline = load_baseline()
    fix_log = load_fix_log()

    print("Running FINAL verification on ALL folders...")
    print(f"{'='*60}")
    print()

    # Full regression
    print("Running full regression test...")
    regression = run_batch_test(ALL_FOLDERS)
    passed = regression.get("passed", 0)
    total = regression.get("total", 0)
    failed = regression.get("failed", 0)
    regression_ok = failed == 0
    print(f"  Result: {passed}/{total} — {'OK' if regression_ok else 'FAILED'}")
    print()

    # Full audit
    print("Running full audit...")
    audit = run_audit(ALL_FOLDERS)
    print(f"  Total fallbacks: {audit.get('total', '?')}")
    if "by_rule" in audit:
        for rule, count in sorted(audit["by_rule"].items()):
            print(f"    {rule}: {count}")
    print()

    # Comparison with baseline
    if baseline:
        print(f"{'─'*60}")
        print("Comparison with baseline:")
        base_audit = baseline.get("audit", {})
        base_total = base_audit.get("total", 0)
        curr_total = audit.get("total", 0)
        delta = curr_total - base_total
        direction = "fewer" if delta < 0 else "more" if delta > 0 else "same"
        print(f"  Fallbacks: {base_total} -> {curr_total} ({abs(delta)} {direction})")

        if "by_rule" in base_audit and "by_rule" in audit:
            for rule in sorted(set(list(base_audit["by_rule"].keys()) + list(audit.get("by_rule", {}).keys()))):
                before = base_audit["by_rule"].get(rule, 0)
                after = audit.get("by_rule", {}).get(rule, 0)
                d = after - before
                if d != 0:
                    print(f"    {rule}: {before} -> {after} ({d:+d})")

        base_reg = baseline.get("regression", {})
        print(f"  Regression: {base_reg.get('passed', '?')}/{base_reg.get('total', '?')} -> {passed}/{total}")
    print()

    # Fix summary
    if fix_log:
        print(f"{'─'*60}")
        print("Fix log summary:")
        for entry in fix_log:
            print(f"  [{entry['status']:>8s}] {entry['category_id']}")
    print()

    # Final result
    final = {
        "regression": {"total": total, "passed": passed, "failed": failed, "status": "OK" if regression_ok else "FAILED"},
        "fallbacks": audit.get("by_rule", {}),
        "fallbacks_total": audit.get("total", 0),
        "baseline_fallbacks_total": baseline.get("audit", {}).get("total") if baseline else None,
        "categories_processed": len(fix_log) if fix_log else 0,
    }
    print(json.dumps(final, indent=2))


def main():
    if len(sys.argv) < 2:
        print("Usage:", file=sys.stderr)
        print("  uv run python scripts/run_fix_harness.py --status", file=sys.stderr)
        print("  uv run python scripts/run_fix_harness.py --init-baseline", file=sys.stderr)
        print("  uv run python scripts/run_fix_harness.py --verify-fix <category_id> --folders folder1 folder2", file=sys.stderr)
        print("  uv run python scripts/run_fix_harness.py --final-verify", file=sys.stderr)
        sys.exit(2)

    if "--status" in sys.argv:
        cmd_status()
    elif "--init-baseline" in sys.argv:
        cmd_init_baseline()
    elif "--verify-fix" in sys.argv:
        idx = sys.argv.index("--verify-fix")
        if idx + 1 >= len(sys.argv):
            print("ERROR: --verify-fix requires a category_id", file=sys.stderr)
            sys.exit(2)
        category_id = sys.argv[idx + 1]

        folders = ALL_FOLDERS  # default
        if "--folders" in sys.argv:
            fidx = sys.argv.index("--folders")
            folders = sys.argv[fidx + 1:]
            if not folders:
                print("ERROR: --folders requires at least one folder", file=sys.stderr)
                sys.exit(2)

        cmd_verify_fix(category_id, folders)
    elif "--final-verify" in sys.argv:
        cmd_final_verify()
    else:
        print(f"Unknown command: {sys.argv[1]}", file=sys.stderr)
        sys.exit(2)


if __name__ == "__main__":
    main()
