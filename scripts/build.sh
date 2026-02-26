#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_DIR"

ANTLR_JAR="tools/antlr4.jar"

if [ ! -f "$ANTLR_JAR" ]; then
    echo "ERROR: $ANTLR_JAR not found. Run 'bash setup.sh' first." >&2
    exit 1
fi

# Clean previous generated files
rm -rf generated/
mkdir -p generated

# Use absolute path for the jar
ANTLR_JAR_ABS="$(cd "$(dirname "$ANTLR_JAR")" && pwd)/$(basename "$ANTLR_JAR")"

echo "Compiling grammar..."
# Compile from grammar/ dir so .tokens files are found by the parser grammar
cd grammar
java -jar "$ANTLR_JAR_ABS" \
    -Dlanguage=Python3 \
    -o ../generated \
    -visitor \
    -listener \
    -package generated \
    SasLexer.g4 SasParser.g4 2>&1

STATUS=$?
cd "$PROJECT_DIR"

if [ $STATUS -ne 0 ]; then
    echo "ERROR: Grammar compilation failed." >&2
    exit 1
fi

echo "Grammar compiled successfully."

# Copy custom lexer class into generated/
cp "$PROJECT_DIR/grammar/SasCustomLexer.py" "$PROJECT_DIR/generated/SasCustomLexer.py"

echo "Generated files:"
ls -1 generated/*.py 2>/dev/null || echo "  (no .py files found)"
