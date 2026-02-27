#!/usr/bin/env python3
"""Inspect the Concrete Syntax Tree (CST) for specific files.

Shows the tree structure to verify correct nesting of:
- if/then/else chains inside data steps
- PROC SQL blocks not consuming non-SQL statements
- Program-level statement boundaries

Usage:
    uv run python scripts/inspect_cst.py <file.sas> [--depth N]
"""

import sys
import os

sys.setrecursionlimit(5000)

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
GENERATED_DIR = os.path.join(PROJECT_DIR, "generated")
sys.path.insert(0, GENERATED_DIR)

from antlr4 import CommonTokenStream, FileStream, PredictionMode
from antlr4.error.ErrorStrategy import DefaultErrorStrategy
from SasCustomLexer import SasCustomLexer as SasLexer
from SasParser import SasParser


class SilentErrorListener:
    def syntaxError(self, *args): pass
    def reportAmbiguity(self, *args): pass
    def reportAttemptingFullContext(self, *args): pass
    def reportContextSensitivity(self, *args): pass


def get_rule_name(ctx):
    name = type(ctx).__name__
    if name.endswith("Context"):
        name = name[:-7]
    return name[0].lower() + name[1:] if name else name


def get_text_snippet(parser, ctx, max_len=60):
    tokens = parser.getTokenStream()
    start = ctx.start.tokenIndex
    stop = ctx.stop.tokenIndex if ctx.stop else start
    text = tokens.getText(start, min(stop, start + 30))
    text = " ".join(text.split())
    if len(text) > max_len:
        text = text[:max_len] + "..."
    return text


# Rules we want to show structure for (expanded when encountered)
EXPAND_RULES = {
    "program", "programStatement",
    "dataStep", "dataStepStatement",
    "ifThenElseStmt", "doBlock", "actionStatement",
    "procStep", "procSqlStep",
    "sqlStatement", "sqlSelectStmt", "sqlCreateStmt", "sqlInsertStmt",
    "sqlUpdateStmt", "sqlDeleteStmt", "sqlAlterStmt", "sqlDropStmt",
    "sqlDescribeStmt", "sqlGenericStmt",
    "macroDefinition", "macroBody", "macroBodyStatement",
    "globalStatement", "unknownStatement",
}


def print_tree(parser, ctx, indent=0, max_depth=None):
    if max_depth is not None and indent > max_depth:
        return

    rule_name = get_rule_name(ctx)
    start_line = ctx.start.line if ctx.start else "?"
    end_line = ctx.stop.line if ctx.stop else "?"
    line_info = f"L{start_line}" if start_line == end_line else f"L{start_line}-{end_line}"

    prefix = "  " * indent

    # For leaf-like rules (not in expand set), show snippet and stop
    if rule_name not in EXPAND_RULES:
        snippet = get_text_snippet(parser, ctx, max_len=80)
        print(f"{prefix}{rule_name} [{line_info}]: {snippet}")
        return

    # For expandable rules, show rule name and recurse
    children = list(ctx.getChildren()) if hasattr(ctx, "getChildren") else []
    rule_children = [c for c in children if hasattr(c, "start")]

    if rule_name == "program":
        # Just recurse into children
        for child in rule_children:
            print_tree(parser, child, indent, max_depth)
        return

    if rule_name == "programStatement":
        # Skip the wrapper, show the inner rule directly
        for child in rule_children:
            print_tree(parser, child, indent, max_depth)
        return

    # Print this node
    if rule_name in ("unknownStatement", "sqlGenericStmt"):
        snippet = get_text_snippet(parser, ctx, max_len=80)
        print(f"{prefix}\033[91m{rule_name}\033[0m [{line_info}]: {snippet}")
    elif rule_name in ("dataStep", "procStep", "procSqlStep", "macroDefinition", "globalStatement"):
        snippet = get_text_snippet(parser, ctx, max_len=80)
        print(f"{prefix}\033[1m{rule_name}\033[0m [{line_info}]: {snippet}")
    elif rule_name == "ifThenElseStmt":
        snippet = get_text_snippet(parser, ctx, max_len=100)
        print(f"{prefix}\033[94m{rule_name}\033[0m [{line_info}]: {snippet}")
    elif rule_name in ("sqlSelectStmt", "sqlCreateStmt", "sqlInsertStmt", "sqlUpdateStmt",
                        "sqlDeleteStmt", "sqlAlterStmt", "sqlDropStmt", "sqlDescribeStmt"):
        snippet = get_text_snippet(parser, ctx, max_len=80)
        print(f"{prefix}\033[92m{rule_name}\033[0m [{line_info}]: {snippet}")
    else:
        print(f"{prefix}{rule_name} [{line_info}]")

    # Recurse into children
    for child in rule_children:
        child_name = get_rule_name(child)
        if child_name in EXPAND_RULES:
            print_tree(parser, child, indent + 1, max_depth)
        elif child_name in ("ifThenElseStmt", "doBlock"):
            print_tree(parser, child, indent + 1, max_depth)
        elif child_name in ("sqlStatement",):
            print_tree(parser, child, indent + 1, max_depth)
        elif rule_name in EXPAND_RULES and child_name not in EXPAND_RULES:
            # Show leaf children of expanded nodes
            snippet = get_text_snippet(parser, child, max_len=80)
            child_start = child.start.line if child.start else "?"
            child_end = child.stop.line if child.stop else "?"
            child_line = f"L{child_start}" if child_start == child_end else f"L{child_start}-{child_end}"
            child_prefix = "  " * (indent + 1)
            print(f"{child_prefix}{child_name} [{child_line}]: {snippet}")


def main():
    args = sys.argv[1:]
    max_depth = None

    if "--depth" in args:
        idx = args.index("--depth")
        max_depth = int(args[idx + 1])
        args = args[:idx] + args[idx + 2:]

    if not args:
        print("Usage: uv run python scripts/inspect_cst.py <file.sas> [--depth N]")
        sys.exit(2)

    filepath = args[0]
    try:
        input_stream = FileStream(filepath, encoding="utf-8")
    except UnicodeDecodeError:
        input_stream = FileStream(filepath, encoding="latin-1")

    lexer = SasLexer(input_stream)
    lexer.removeErrorListeners()
    tokens = CommonTokenStream(lexer)

    parser = SasParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(SilentErrorListener())
    parser._interp.predictionMode = PredictionMode.LL

    tree = parser.program()

    print(f"\n=== CST: {os.path.basename(filepath)} ===\n")
    print_tree(parser, tree, max_depth=max_depth)
    print()


if __name__ == "__main__":
    main()
