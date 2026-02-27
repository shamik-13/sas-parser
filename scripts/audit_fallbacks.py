#!/usr/bin/env python3
"""Audit all fallback/catch-all rules across the grammar.

Walks the parse tree of every file and logs each occurrence of generic
fallback rules so they can be reviewed for missed grammar coverage.

Target rules:
  - unknownStatement    (top-level catch-all)
  - sqlGenericStmt      (unrecognized SQL inside PROC SQL)
  - macroGenericText    (unrecognized text inside macro bodies)
  - procBodyStatement   (generic proc body — opt-in via --include-proc-body)

Usage:
    uv run python scripts/audit_fallbacks.py folder1 [folder2 ...]
    uv run python scripts/audit_fallbacks.py --include-proc-body folder1 ...

Folder names are relative to sources/sas_source_codes/.
"""

import json
import os
import sys
from collections import defaultdict
from datetime import datetime

sys.setrecursionlimit(5000)

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
GENERATED_DIR = os.path.join(PROJECT_DIR, "generated")
SOURCES_DIR = os.path.join(PROJECT_DIR, "sources", "sas_source_codes")
sys.path.insert(0, GENERATED_DIR)


def find_sas_files(folders: list[str]) -> list[str]:
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


def get_rule_name(ctx):
    """Get the ANTLR rule name from a context object."""
    # Context class names are like 'ProcSqlStepContext' -> 'procSqlStep'
    name = type(ctx).__name__
    if name.endswith("Context"):
        name = name[:-7]
    # Lowercase first char
    return name[0].lower() + name[1:] if name else name


def get_snippet(parser, ctx, max_len=120):
    """Extract the matched text for a context, truncated."""
    tokens = parser.getTokenStream()
    start = ctx.start.tokenIndex
    stop = ctx.stop.tokenIndex if ctx.stop else start
    text = tokens.getText(start, min(stop, start + 60))
    # Collapse whitespace for readability
    text = " ".join(text.split())
    if len(text) > max_len:
        text = text[:max_len] + "..."
    return text


def get_sibling_info(ctx, parser):
    """Get info about the previous and next sibling rules."""
    parent = ctx.parentCtx
    if parent is None:
        return None, None

    children = list(parent.getChildren())
    idx = None
    for i, child in enumerate(children):
        if child is ctx:
            idx = i
            break

    prev_info = None
    next_info = None

    if idx is not None:
        # Search backwards for previous rule context (skip terminals)
        for i in range(idx - 1, -1, -1):
            if hasattr(children[i], 'start'):
                name = get_rule_name(children[i])
                line = children[i].start.line
                prev_info = f"{name} (L{line})"
                break

        # Search forwards for next rule context
        for i in range(idx + 1, len(children)):
            if hasattr(children[i], 'start'):
                name = get_rule_name(children[i])
                line = children[i].start.line
                next_info = f"{name} (L{line})"
                break

    return prev_info, next_info


def read_source_lines(filepath):
    """Read source lines for context display."""
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            return f.readlines()
    except UnicodeDecodeError:
        with open(filepath, "r", encoding="latin-1") as f:
            return f.readlines()


class FallbackAuditListener:
    """Listener that collects all fallback rule occurrences."""

    def __init__(self, parser, filepath, source_lines, include_proc_body=False):
        self.parser = parser
        self.filepath = filepath
        self.source_lines = source_lines
        self.include_proc_body = include_proc_body
        self.occurrences = []

    def _record(self, rule_name, ctx):
        start_line = ctx.start.line
        end_line = ctx.stop.line if ctx.stop else start_line
        snippet = get_snippet(self.parser, ctx)
        parent_name = get_rule_name(ctx.parentCtx) if ctx.parentCtx else "root"
        prev_sib, next_sib = get_sibling_info(ctx, self.parser)

        # Get source context: the actual source lines around the occurrence
        context_lines = []
        for i in range(max(0, start_line - 2), min(len(self.source_lines), end_line + 1)):
            line_text = self.source_lines[i].rstrip()
            marker = ">>>" if start_line <= (i + 1) <= end_line else "   "
            context_lines.append(f"  {marker} {i+1:4d} | {line_text}")

        self.occurrences.append({
            "rule": rule_name,
            "file": self.filepath,
            "start_line": start_line,
            "end_line": end_line,
            "snippet": snippet,
            "parent": parent_name,
            "prev_sibling": prev_sib,
            "next_sibling": next_sib,
            "source_context": context_lines,
        })

    def enterUnknownStatement(self, ctx):
        self._record("unknownStatement", ctx)

    def enterSqlGenericStmt(self, ctx):
        self._record("sqlGenericStmt", ctx)

    def enterMacroGenericText(self, ctx):
        self._record("macroGenericText", ctx)

    def enterProcBodyStatement(self, ctx):
        if self.include_proc_body:
            self._record("procBodyStatement", ctx)

    # Required no-op methods for the walker
    def enterEveryRule(self, ctx):
        pass

    def exitEveryRule(self, ctx):
        pass

    def visitTerminal(self, node):
        pass

    def visitErrorNode(self, node):
        pass


def audit_file(filepath, include_proc_body=False):
    """Parse a file and return list of fallback occurrences."""
    from antlr4 import CommonTokenStream, FileStream, PredictionMode, ParseTreeWalker
    from antlr4.error.ErrorStrategy import BailErrorStrategy, DefaultErrorStrategy
    from SasCustomLexer import SasCustomLexer as SasLexer
    from SasParser import SasParser

    try:
        input_stream = FileStream(filepath, encoding="utf-8")
    except UnicodeDecodeError:
        input_stream = FileStream(filepath, encoding="latin-1")

    source_lines = read_source_lines(filepath)

    lexer = SasLexer(input_stream)
    lexer.removeErrorListeners()
    tokens = CommonTokenStream(lexer)

    # Try SLL first — if it succeeds we still need the tree, so we always do LL
    # But check for errors: if the file has parse errors, skip the audit
    parser = SasParser(tokens)
    parser.removeErrorListeners()

    # Use SLL first to check for errors quickly
    parser._interp.predictionMode = PredictionMode.SLL
    parser._errHandler = BailErrorStrategy()
    sll_ok = False
    try:
        parser.program()
        sll_ok = True
    except Exception:
        pass

    # Re-parse in LL mode to get the full tree for walking
    tokens.seek(0)
    parser.reset()
    parser._interp.predictionMode = PredictionMode.LL
    parser._errHandler = DefaultErrorStrategy()

    # Silence error output
    class SilentListener:
        def syntaxError(self, *a): pass
        def reportAmbiguity(self, *a): pass
        def reportAttemptingFullContext(self, *a): pass
        def reportContextSensitivity(self, *a): pass

    parser.removeErrorListeners()
    parser.addErrorListener(SilentListener())

    tree = parser.program()

    listener = FallbackAuditListener(parser, filepath, source_lines, include_proc_body)
    walker = ParseTreeWalker()
    walker.walk(listener, tree)

    return listener.occurrences


def print_report(all_occurrences, folders, total_files, elapsed_secs):
    """Print human-readable report to stdout."""
    # Group by rule
    by_rule = defaultdict(list)
    for occ in all_occurrences:
        by_rule[occ["rule"]].append(occ)

    # Group by rule+file for file counts
    files_by_rule = defaultdict(set)
    for occ in all_occurrences:
        files_by_rule[occ["rule"]].add(occ["file"])

    rule_order = ["unknownStatement", "sqlGenericStmt", "macroGenericText", "procBodyStatement"]

    print(f"\n{'='*70}")
    print(f"Fallback Audit Report — {datetime.now().isoformat()}")
    print(f"{'='*70}")
    print(f"Folders:  {', '.join(folders)}")
    print(f"Files:    {total_files}")
    print(f"Time:     {elapsed_secs:.1f}s")
    print()
    print("Summary:")
    print(f"  {'Rule':<25s} {'Occurrences':>12s} {'Files':>8s}")
    print(f"  {'-'*25} {'-'*12} {'-'*8}")
    for rule in rule_order:
        if rule in by_rule:
            count = len(by_rule[rule])
            fcount = len(files_by_rule[rule])
            print(f"  {rule:<25s} {count:>12d} {fcount:>8d}")
    print()

    # Detail for each non-procBodyStatement rule
    for rule in rule_order:
        if rule == "procBodyStatement" or rule not in by_rule:
            continue

        occurrences = by_rule[rule]
        fcount = len(files_by_rule[rule])
        print(f"{'─'*70}")
        print(f" {rule} — {len(occurrences)} occurrences in {fcount} files")
        print(f"{'─'*70}")

        # Group by file for readability
        by_file = defaultdict(list)
        for occ in occurrences:
            by_file[occ["file"]].append(occ)

        for filepath in sorted(by_file.keys()):
            relpath = os.path.relpath(filepath, PROJECT_DIR)
            file_occs = by_file[filepath]
            print(f"\n  {relpath} ({len(file_occs)} occurrence{'s' if len(file_occs) != 1 else ''}):")

            for occ in file_occs:
                line_range = (f"L{occ['start_line']}"
                              if occ['start_line'] == occ['end_line']
                              else f"L{occ['start_line']}-{occ['end_line']}")
                print(f"\n    [{line_range}] parent={occ['parent']}")
                if occ["prev_sibling"]:
                    print(f"      prev: {occ['prev_sibling']}")
                if occ["next_sibling"]:
                    print(f"      next: {occ['next_sibling']}")
                for cl in occ["source_context"]:
                    print(f"    {cl}")
        print()

    # procBodyStatement summary (if included) — just counts per file
    if "procBodyStatement" in by_rule:
        occurrences = by_rule["procBodyStatement"]
        by_file = defaultdict(int)
        for occ in occurrences:
            by_file[occ["file"]] += 1

        print(f"{'─'*70}")
        print(f" procBodyStatement — {len(occurrences)} occurrences in {len(by_file)} files")
        print(f"{'─'*70}")
        print(f"  (Showing counts per file — these are expected for non-SQL procs)")
        for filepath in sorted(by_file.keys()):
            relpath = os.path.relpath(filepath, PROJECT_DIR)
            print(f"    {by_file[filepath]:4d}  {relpath}")
        print()


def save_json_report(all_occurrences, folders, total_files, elapsed_secs, report_path):
    """Save detailed JSON report."""
    # Strip source_context from JSON (it's for human display only)
    json_occurrences = []
    for occ in all_occurrences:
        entry = dict(occ)
        entry["file"] = os.path.relpath(entry["file"], PROJECT_DIR)
        del entry["source_context"]
        json_occurrences.append(entry)

    report = {
        "timestamp": datetime.now().isoformat(),
        "folders": folders,
        "total_files": total_files,
        "elapsed_secs": round(elapsed_secs, 1),
        "occurrences": json_occurrences,
    }

    with open(report_path, "w") as f:
        json.dump(report, f, indent=2)


def main():
    args = sys.argv[1:]

    include_proc_body = False
    if "--include-proc-body" in args:
        include_proc_body = True
        args.remove("--include-proc-body")

    if not args:
        print("Usage: uv run python scripts/audit_fallbacks.py [--include-proc-body] folder1 [folder2 ...]",
              file=sys.stderr)
        sys.exit(2)

    folders = args
    files = find_sas_files(folders)
    if not files:
        print("No .sas files found.", file=sys.stderr)
        sys.exit(1)

    print(f"Auditing {len(files)} files across {len(folders)} folders...")
    if include_proc_body:
        print("  (including procBodyStatement)")

    all_occurrences = []
    errors = []
    start_time = datetime.now()

    try:
        from tqdm import tqdm
        file_iter = tqdm(files, desc="Auditing", unit="file")
    except ImportError:
        file_iter = files

    for filepath in file_iter:
        try:
            occurrences = audit_file(filepath, include_proc_body)
            all_occurrences.extend(occurrences)
        except Exception as e:
            errors.append({"file": filepath, "error": str(e)})

    elapsed = (datetime.now() - start_time).total_seconds()

    # Print human-readable report
    print_report(all_occurrences, folders, len(files), elapsed)

    if errors:
        print(f"\nErrors during audit ({len(errors)} files):")
        for err in errors:
            relpath = os.path.relpath(err["file"], PROJECT_DIR)
            print(f"  {relpath}: {err['error'][:100]}")

    # Save JSON report
    reports_dir = os.path.join(PROJECT_DIR, "reports")
    os.makedirs(reports_dir, exist_ok=True)
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    report_path = os.path.join(reports_dir, f"audit_{timestamp}.json")
    save_json_report(all_occurrences, folders, len(files), elapsed, report_path)
    print(f"\nJSON report: {report_path}")


if __name__ == "__main__":
    main()
