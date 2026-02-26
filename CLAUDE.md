# SAS ANTLR Grammar Development

## Project Overview

This project develops an ANTLR4 grammar for the SAS programming language. The grammar is iteratively refined using a test-driven loop: compile the grammar, test against real SAS source files, analyze failures, fix the grammar, repeat.

The goal is to parse real-world SAS code including macros, data steps, proc steps, and global statements.

## Quick Start

```bash
# One-time setup (downloads ANTLR jar, installs Python deps)
bash setup.sh

# Run a single development iteration
uv run python scripts/orchestrate.py sasjs_tests

# Or target multiple folders
uv run python scripts/orchestrate.py general_purpose_macros sasjs_tests
```

## The Grammar Development Loop

This is your primary workflow. Repeat until the pass rate is acceptable:

1. **Run orchestrate.py** on target folder(s):
   ```bash
   uv run python scripts/orchestrate.py <folder1> [folder2...]
   ```
2. **Read the output** — it shows pass rate, error clusters, and minimal reproductions
3. **Fix the grammar** (`grammar/SasLexer.g4` and/or `grammar/SasParser.g4`) based on the errors
4. **Run orchestrate.py again** — verify pass rate improved and no regressions
5. **Repeat** until pass rate is acceptable (target: >95%)

### Individual Tools

| Command | Purpose |
|---------|---------|
| `bash scripts/build.sh` | Compile grammar only |
| `uv run python scripts/validate.py <file.sas>` | Test a single file |
| `uv run python scripts/minimize.py <file.sas>` | Minimize a failing file |
| `uv run python scripts/batch_test.py folder1 [...]` | Batch test without minimization |
| `uv run python scripts/orchestrate.py folder1 [...]` | Full loop: build + test + minimize |

Folder names are relative to `sources/sas_source_codes/`.

## How to Fix Common ANTLR Errors

### "mismatched input 'X' expecting Y"
The parser expected token Y but got X. Common fixes:
- Add X as an alternative in the relevant parser rule
- If X is a keyword used as an identifier, add it to the `identifier` rule
- If the rule is too strict, add the missing alternative

### "no viable alternative at input 'X'"
No parser rule matches this input sequence. Common fixes:
- Add a new parser rule for this SAS construct
- Add an alternative to an existing rule
- Check if it's a statement type not yet supported

### "extraneous input 'X'"
Token X appeared where nothing was expected. Common fixes:
- The preceding rule needs to consume more tokens
- A rule ended too early — add more alternatives
- A `~(SEMI)*` pattern might need to be added

### "missing 'X' at 'Y'"
The parser expected token X before Y. Common fixes:
- Make X optional in the rule
- Add an alternative path that doesn't require X
- The rule structure might be wrong

### "token recognition error at: 'X'"
The lexer couldn't recognize character X. Fix: add a new lexer rule.

## The Keyword-as-Identifier Problem

SAS has very few truly reserved words. Most keywords can appear as variable names, dataset names, or macro parameters. The `identifier` rule in the parser grammar lists ALL keywords that can also be identifiers:

```antlr
identifier
    : ID
    | KW_DATA | KW_SET | KW_MERGE | ...
    ;
```

When you add a new keyword to the lexer, you almost always need to add it to the `identifier` rule too, or existing code that uses that word as a variable name will break.

**Strategy:** Keep keyword tokens in the lexer (so parser rules can reference them), but also allow them as identifiers in the parser.

## Curriculum Phases

Develop the grammar in phases, starting with simpler files:

### Phase 1: Foundation (sasjs_tests)
- Target: `sasjs_tests` (146 files)
- Focus: macro calls, %let, %put, %mp_assert, basic data steps
- These are short test files with common patterns

### Phase 2: Macros (general_purpose_macros)
- Target: `general_purpose_macros` (77 files)
- Focus: full %macro/%mend definitions, macro parameters, %if/%then/%do, macro quoting
- More complex macro patterns

### Phase 3: SASjs Base (sasjs_base_macros)
- Target: `sasjs_base_macros`
- Focus: framework macros, proc SQL, complex macro logic

### Phase 4: Clinical (clinical_*)
- Target: `clinical_contributed`, `clinical_examples`, `clinical_tested`, `clinical_whitepapers`
- Focus: data step features, proc steps, ODS, advanced SAS

## Architecture

### File Structure

```
grammar/
├── SasLexer.g4           # Token definitions (keywords, operators, strings, etc.)
└── SasParser.g4          # Parse rules (program structure)
generated/                # ANTLR-generated Python code (auto-generated, gitignored)
scripts/
├── build.sh              # Compiles .g4 → Python parser
├── validate.py           # Parses one file, reports errors as JSON
├── minimize.py           # Binary-search minimizes a failing file
├── batch_test.py         # Tests folder(s), clusters errors, produces report
└── orchestrate.py        # Full iteration: build + batch test + minimize top failures
sources/sas_source_codes/ # Real SAS files organized by category
failures/                 # Minimized reproduction snippets
reports/                  # JSON reports from batch/iteration runs
```

### How the Scripts Work Together

```
orchestrate.py
├── build.sh          → compiles grammar, exits if errors
├── batch_test.py     → validates all files, clusters errors
│   └── validate.py   → parses each file individually
└── minimize.py       → minimizes top-5 cluster representatives
    └── validate.py   → used internally for each minimization attempt
```

## Grammar Structure

### Lexer (SasLexer.g4)
- Uses `options { caseInsensitive = true; }` — SAS is case-insensitive
- Comments are skipped (`BLOCK_COMMENT`, `LINE_COMMENT`, `MACRO_COMMENT`)
- Whitespace is skipped
- Macro keywords (`%macro`, `%let`, etc.) are separate tokens from SAS keywords (`data`, `proc`, etc.)
- `MACRO_CALL_NAME` catches `%identifier` patterns not matched by specific macro keywords
- `MACRO_VAR` catches `&identifier` patterns

### Parser (SasParser.g4)
Key rules:
- `program` → top-level list of statements
- `macroDefinition` → `%macro name(...); ... %mend;`
- `macroStatement` → `%let`, `%if/%then`, `%do/%end`, `%put`, macro calls
- `dataStep` → `data name; ... run;`
- `procStep` → `proc name ...; ... run;` or `quit;`
- `globalStatement` → `libname`, `filename`, `options`, `ods`, `title`, etc.
- `expression` → full expression grammar with operator precedence
- `identifier` → ID plus all unreserved keywords

### How to Extend the Grammar

**Adding a new SAS statement:**
1. Add any new keywords to `SasLexer.g4`
2. Add those keywords to the `identifier` rule in `SasParser.g4`
3. Create a parser rule for the statement
4. Add it as an alternative in the appropriate parent rule

**Making proc bodies more specific:**
The default `procBodyStatement` is a generic token-collector. To parse specific procs (like PROC SQL), create a dedicated `procSql` rule and route to it from `procStep` when the proc name matches.

## Available SAS Source Folders

| Folder | Files | Description |
|--------|-------|-------------|
| `sasjs_tests` | 146 | Short test files, macro calls |
| `general_purpose_macros` | 77 | Utility macro definitions |
| `sasjs_base_macros` | ~100 | Framework macros |
| `sasjs_ddl_fcmp_lua` | ~50 | DDL, FCMP, Lua integration |
| `sasjs_platform_macros` | ~50 | Platform-specific macros |
| `packages_framework` | ~30 | Package system |
| `clinical_contributed` | ~50 | Clinical SAS macros |
| `clinical_examples` | ~30 | Clinical examples |
| `clinical_tested` | ~50 | Tested clinical code |
| `clinical_whitepapers` | ~15 | Whitepaper code |

## Tips

- Always check minimal reproductions in `failures/` — they show the smallest code that triggers each error
- After fixing grammar, run `bash scripts/build.sh` first to catch grammar syntax errors before a full test run
- If the grammar has ambiguity warnings, they're usually fine — but excessive ambiguity can slow parsing
- The `procBodyStatement` rule is intentionally generic — it collects tokens until `run;` or `quit;`. This is by design for the bootstrap phase.
- Use `uv run python scripts/validate.py <file>` to quickly test your fix against a specific failing file
- When the pass rate plateaus, examine the remaining failures — they may need fundamentally new grammar rules rather than tweaks
