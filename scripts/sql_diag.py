#!/usr/bin/env python3
"""Diagnostic: show which SQL rules fire vs. sqlGenericStmt fallback.

Usage:
    uv run python scripts/sql_diag.py <file.sas>
"""

import sys
import os

sys.setrecursionlimit(5000)

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
GENERATED_DIR = os.path.join(PROJECT_DIR, "generated")
sys.path.insert(0, GENERATED_DIR)

from antlr4 import CommonTokenStream, FileStream, PredictionMode, ParseTreeWalker
from antlr4.error.ErrorStrategy import DefaultErrorStrategy
from SasCustomLexer import SasCustomLexer as SasLexer
from SasParser import SasParser
from SasParserListener import SasParserListener


class SqlDiagListener(SasParserListener):
    def __init__(self, parser):
        self.parser = parser
        self.sql_rules = []
        self.generic_stmts = []

    def enterProcSqlStep(self, ctx):
        start = ctx.start
        stop = ctx.stop
        self.sql_rules.append(("procSqlStep", start.line, stop.line if stop else "?"))

    def enterSqlSelectStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlSelectStmt", ctx.start.line, text))

    def enterSqlCreateStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlCreateStmt", ctx.start.line, text))

    def enterSqlInsertStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlInsertStmt", ctx.start.line, text))

    def enterSqlUpdateStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlUpdateStmt", ctx.start.line, text))

    def enterSqlDeleteStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlDeleteStmt", ctx.start.line, text))

    def enterSqlAlterStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlAlterStmt", ctx.start.line, text))

    def enterSqlDropStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlDropStmt", ctx.start.line, text))

    def enterSqlDescribeStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlDescribeStmt", ctx.start.line, text))

    def enterSqlResetStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlResetStmt", ctx.start.line, text))

    def enterSqlValidateStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlValidateStmt", ctx.start.line, text))

    def enterSqlConnectStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlConnectStmt", ctx.start.line, text))

    def enterSqlDisconnectStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlDisconnectStmt", ctx.start.line, text))

    def enterSqlExecuteStmt(self, ctx):
        text = self._snippet(ctx)
        self.sql_rules.append(("sqlExecuteStmt", ctx.start.line, text))

    def enterSqlGenericStmt(self, ctx):
        text = self._snippet(ctx)
        self.generic_stmts.append(("sqlGenericStmt", ctx.start.line, text))

    def _snippet(self, ctx):
        tokens = self.parser.getTokenStream()
        start = ctx.start.tokenIndex
        stop = ctx.stop.tokenIndex if ctx.stop else start
        text = tokens.getText(start, min(stop, start + 30))
        if len(text) > 80:
            text = text[:80] + "..."
        return text


class SilentErrorListener:
    def syntaxError(self, *args): pass
    def reportAmbiguity(self, *args): pass
    def reportAttemptingFullContext(self, *args): pass
    def reportContextSensitivity(self, *args): pass


def main():
    if len(sys.argv) != 2:
        print("Usage: uv run python scripts/sql_diag.py <file.sas>")
        sys.exit(2)

    filepath = sys.argv[1]
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

    listener = SqlDiagListener(parser)
    walker = ParseTreeWalker()
    walker.walk(listener, tree)

    if not listener.sql_rules and not listener.generic_stmts:
        print(f"No PROC SQL blocks found in {filepath}")
        return

    print(f"\n=== SQL Diagnostic: {os.path.basename(filepath)} ===\n")

    if listener.sql_rules:
        print("SPECIFIC SQL rules matched:")
        for rule, line, text in listener.sql_rules:
            print(f"  L{line:4d}  {rule:25s}  {text}")

    if listener.generic_stmts:
        print(f"\nGENERIC FALLBACK (sqlGenericStmt) — {len(listener.generic_stmts)} occurrences:")
        for rule, line, text in listener.generic_stmts:
            print(f"  L{line:4d}  {text}")
    else:
        print("\nNo sqlGenericStmt fallbacks — all SQL parsed by specific rules!")

    total = len(listener.sql_rules) + len(listener.generic_stmts)
    specific = len([r for r in listener.sql_rules if r[0] != "procSqlStep"])
    generic = len(listener.generic_stmts)
    print(f"\nSummary: {specific} specific / {generic} generic / {specific + generic} total SQL statements")


if __name__ == "__main__":
    main()
