#!/usr/bin/env python3
"""Minimize a failing SAS file to a minimal reproduction.

Usage:
    uv run python scripts/minimize.py <file.sas>

Algorithm:
    1. Verify the file fails validation
    2. Binary search: remove chunks, check if same error type persists
    3. Single-line removal pass
    4. Save minimal snippet to failures/repro_FILENAME_HASH.sas
"""

import hashlib
import json
import os
import subprocess
import sys
import tempfile

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def run_validate(filepath: str) -> tuple[bool, str]:
    """Run validate.py on a file. Returns (has_errors, first_error_message)."""
    result = subprocess.run(
        [sys.executable, os.path.join(PROJECT_DIR, "scripts", "validate.py"), filepath],
        capture_output=True,
        text=True,
    )
    if result.returncode == 0:
        return False, ""

    try:
        data = json.loads(result.stdout)
        if data.get("errors"):
            return True, data["errors"][0]["message"]
    except (json.JSONDecodeError, KeyError):
        pass

    return result.returncode != 0, result.stdout[:200]


def error_pattern(msg: str) -> str:
    """Extract a normalized error pattern from an error message."""
    # Keep the general structure but normalize specific tokens
    for prefix in ["mismatched input", "no viable alternative", "extraneous input", "missing"]:
        if msg.startswith(prefix):
            return prefix
    return msg[:50]


def write_temp(lines: list[str]) -> str:
    """Write lines to a temp file, return its path."""
    fd, path = tempfile.mkstemp(suffix=".sas")
    with os.fdopen(fd, "w") as f:
        f.writelines(lines)
    return path


def minimize(filepath: str) -> str | None:
    """Minimize a failing SAS file. Returns path to minimal repro, or None."""
    # Read the file
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            lines = f.readlines()
    except UnicodeDecodeError:
        with open(filepath, "r", encoding="latin-1") as f:
            lines = f.readlines()

    # Verify it fails
    has_errors, orig_msg = run_validate(filepath)
    if not has_errors:
        print(f"File already passes: {filepath}")
        return None

    orig_pattern = error_pattern(orig_msg)
    print(f"Original error pattern: '{orig_pattern}' ({len(lines)} lines)")

    # Phase 1: Binary chunk removal
    chunk_size = max(1, len(lines) // 2)
    while chunk_size >= 1:
        i = 0
        while i + chunk_size <= len(lines):
            candidate = lines[:i] + lines[i + chunk_size:]
            if not candidate:
                i += chunk_size
                continue

            tmp = write_temp(candidate)
            try:
                has_err, msg = run_validate(tmp)
                if has_err and error_pattern(msg) == orig_pattern:
                    lines = candidate
                    print(f"  Removed chunk at {i}, size {chunk_size} → {len(lines)} lines")
                    # Don't advance i — try removing from same position again
                else:
                    i += chunk_size
            finally:
                os.unlink(tmp)

        chunk_size //= 2

    # Phase 2: Single-line removal
    i = 0
    while i < len(lines):
        candidate = lines[:i] + lines[i + 1:]
        if not candidate:
            i += 1
            continue

        tmp = write_temp(candidate)
        try:
            has_err, msg = run_validate(tmp)
            if has_err and error_pattern(msg) == orig_pattern:
                lines = candidate
                print(f"  Removed line {i} → {len(lines)} lines")
            else:
                i += 1
        finally:
            os.unlink(tmp)

    # Save result
    basename = os.path.splitext(os.path.basename(filepath))[0]
    content = "".join(lines)
    content_hash = hashlib.md5(content.encode()).hexdigest()[:8]

    failures_dir = os.path.join(PROJECT_DIR, "failures")
    os.makedirs(failures_dir, exist_ok=True)
    repro_path = os.path.join(failures_dir, f"repro_{basename}_{content_hash}.sas")

    with open(repro_path, "w") as f:
        f.write(content)

    print(f"\nMinimized: {len(lines)} lines → {repro_path}")
    return repro_path


def main():
    if len(sys.argv) != 2:
        print("Usage: uv run python scripts/minimize.py <file.sas>", file=sys.stderr)
        sys.exit(2)

    result = minimize(sys.argv[1])
    if result:
        # Print the minimized content
        with open(result) as f:
            print(f"\n--- Minimal reproduction ({result}) ---")
            print(f.read())
    sys.exit(0 if result else 1)


if __name__ == "__main__":
    main()
