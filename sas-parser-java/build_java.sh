#!/usr/bin/env bash
# Build the Java SAS parser CLI tool.
#
# Compiles .g4 → Java source → .class files → fat JAR (tools/sas-parser.jar)
# Requires: java, javac, jar (JDK 17+), tools/antlr4.jar
#
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$SCRIPT_DIR"

ANTLR_JAR="$PROJECT_DIR/tools/antlr4.jar"
OUTPUT_JAR="$PROJECT_DIR/tools/sas-parser.jar"

if [ ! -f "$ANTLR_JAR" ]; then
    echo "ERROR: $ANTLR_JAR not found. Run 'bash setup.sh' first." >&2
    exit 1
fi

# Check for java/javac
if ! command -v javac &>/dev/null; then
    echo "WARNING: javac not found — skipping Java parser build." >&2
    exit 0
fi

echo "Building Java SAS parser..."

# ── Step 1: Generate Java source from ANTLR grammars ──
rm -rf build
mkdir -p build/gen

cd "$PROJECT_DIR/grammar"
java -jar "$ANTLR_JAR" \
    -Dlanguage=Java \
    -o "$SCRIPT_DIR/build/gen" \
    -visitor \
    -listener \
    SasLexer.g4 SasParser.g4 2>&1
cd "$SCRIPT_DIR"

echo "  ANTLR generation done."

# ── Step 2: Compile all Java sources ──
mkdir -p build/classes

# Gather all .java files (generated + our source)
find build/gen -name '*.java' > build/java_sources.txt
find src -name '*.java' >> build/java_sources.txt

javac \
    -cp "$ANTLR_JAR" \
    -d build/classes \
    --source-path "build/gen:src" \
    @build/java_sources.txt 2>&1

echo "  Compilation done."

# ── Step 3: Extract ANTLR runtime classes for fat JAR ──
cd build/classes
jar xf "$ANTLR_JAR" org/ 2>/dev/null || true
cd "$SCRIPT_DIR"

echo "  ANTLR runtime extracted."

# ── Step 4: Package into fat JAR with manifest ──
mkdir -p build/META-INF
echo "Main-Class: Main" > build/META-INF/MANIFEST.MF

cd build/classes
# Copy manifest
mkdir -p META-INF
cp "$SCRIPT_DIR/build/META-INF/MANIFEST.MF" META-INF/

jar cfm "$OUTPUT_JAR" META-INF/MANIFEST.MF \
    *.class \
    org/ 2>&1

cd "$SCRIPT_DIR"

echo "  JAR packaged: $OUTPUT_JAR"
echo "Java SAS parser build complete."
