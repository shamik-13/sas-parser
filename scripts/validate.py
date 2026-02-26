#!/usr/bin/env python3
"""Validate a single SAS file against the current ANTLR grammar.

Usage:
    uv run python scripts/validate.py <file.sas>

Exit codes:
    0 = parse success (PASS)
    1 = parse errors found
    2 = system error (file not found, import error, etc.)
"""

import json
import sys
import os

# Increase recursion limit for deeply nested SAS macro if/else chains
sys.setrecursionlimit(5000)

# Add generated/ to Python path
PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
GENERATED_DIR = os.path.join(PROJECT_DIR, "generated")
sys.path.insert(0, GENERATED_DIR)


class CollectingErrorListener:
    def __init__(self, source_lines):
        self.errors = []
        self.source_lines = source_lines

    def syntaxError(self, recognizer, offendingSymbol, line, column, msg, e):
        source_line = ""
        if 0 < line <= len(self.source_lines):
            source_line = self.source_lines[line - 1].rstrip()

        context = ""
        if offendingSymbol is not None:
            context = offendingSymbol.text

        self.errors.append({
            "line": line,
            "column": column,
            "message": msg,
            "source_line": source_line,
            "context": context,
        })

    def reportAmbiguity(self, *args):
        pass

    def reportAttemptingFullContext(self, *args):
        pass

    def reportContextSensitivity(self, *args):
        pass


def validate_file(filepath: str) -> dict | None:
    """Parse a SAS file and return None on success, or error dict on failure."""
    try:
        from antlr4 import CommonTokenStream, FileStream, PredictionMode
        from antlr4.error.ErrorStrategy import BailErrorStrategy, DefaultErrorStrategy
        from SasLexer import SasLexer
        from SasParser import SasParser
    except ImportError as e:
        print(f"ERROR: Cannot import generated parser. Run 'bash scripts/build.sh' first.\n{e}", file=sys.stderr)
        sys.exit(2)

    if not os.path.isfile(filepath):
        print(f"ERROR: File not found: {filepath}", file=sys.stderr)
        sys.exit(2)

    # Read file content for error context
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            source_lines = f.readlines()
    except UnicodeDecodeError:
        with open(filepath, "r", encoding="latin-1") as f:
            source_lines = f.readlines()

    # Create ANTLR input stream
    try:
        input_stream = FileStream(filepath, encoding="utf-8")
    except UnicodeDecodeError:
        input_stream = FileStream(filepath, encoding="latin-1")

    # Create lexer and token stream
    lexer = SasLexer(input_stream)
    lexer.removeErrorListeners()
    tokens = CommonTokenStream(lexer)

    # --- Pass 1: Fast SLL prediction mode ---
    parser = SasParser(tokens)
    parser.removeErrorListeners()
    parser._interp.predictionMode = PredictionMode.SLL
    parser._errHandler = BailErrorStrategy()

    try:
        parser.program()
        # SLL succeeded with no exceptions — it's a PASS
        return None
    except Exception:
        pass

    # --- Pass 2: Full LL prediction mode with error collection ---
    tokens.seek(0)
    parser.reset()
    parser._interp.predictionMode = PredictionMode.LL
    parser._errHandler = DefaultErrorStrategy()

    error_listener = CollectingErrorListener(source_lines)
    lexer_listener = CollectingErrorListener(source_lines)
    lexer.removeErrorListeners()
    lexer.addErrorListener(lexer_listener)
    parser.removeErrorListeners()
    parser.addErrorListener(error_listener)

    try:
        parser.program()
    except Exception as e:
        error_listener.errors.append({
            "line": 0,
            "column": 0,
            "message": f"Parser exception: {e}",
            "source_line": "",
            "context": "",
        })

    all_errors = lexer_listener.errors + error_listener.errors
    if all_errors:
        return {
            "file": filepath,
            "error_count": len(all_errors),
            "errors": all_errors,
        }
    return None


def main():
    if len(sys.argv) != 2:
        print("Usage: uv run python scripts/validate.py <file.sas>", file=sys.stderr)
        sys.exit(2)

    filepath = sys.argv[1]
    result = validate_file(filepath)

    if result is None:
        print(f"PASS: {filepath}")
        sys.exit(0)
    else:
        print(json.dumps(result, indent=2))
        sys.exit(1)


if __name__ == "__main__":
    main()
