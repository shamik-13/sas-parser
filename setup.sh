#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

ANTLR_VERSION="4.13.2"
ANTLR_JAR="tools/antlr4.jar"
ANTLR_URL="https://www.antlr.org/download/antlr-${ANTLR_VERSION}-complete.jar"

# Check Java
if ! command -v java &>/dev/null; then
    echo "ERROR: Java is required but not found. Install Java 11+ and retry."
    exit 1
fi
echo "Java found: $(java -version 2>&1 | head -1)"

# Download ANTLR jar if missing
mkdir -p tools
if [ ! -f "$ANTLR_JAR" ]; then
    echo "Downloading ANTLR ${ANTLR_VERSION}..."
    curl -fSL -o "$ANTLR_JAR" "$ANTLR_URL"
    echo "Downloaded $ANTLR_JAR"
else
    echo "ANTLR jar already exists: $ANTLR_JAR"
fi

# Sync Python dependencies
echo "Syncing Python dependencies..."
uv sync

echo ""
echo "Setup complete. Next steps:"
echo "  bash scripts/build.sh          # Compile grammar"
echo "  uv run python scripts/validate.py <file.sas>  # Test a file"
