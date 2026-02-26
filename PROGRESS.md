# Grammar Development Progress

## Current Status

| Phase | Folder | Files | Pass Rate | Status |
|-------|--------|-------|-----------|--------|
| 1 | sasjs_tests | 146 | 100% (146/146) | Complete |
| 2 | general_purpose_macros | 93 | 100% (93/93) | Complete |
| 3 | sasjs_base_macros | 191 | 100% (191/191) | Complete |
| 3b | sasjs_platform_macros | 46 | 100% (46/46) | Complete |
| 4 | clinical_* | — | Not tested | Pending |

**Combined: 476/476 (100.0%)**

## Supported Constructs

### Macro Language
- [x] `%macro/%mend` definitions with parameters
- [x] `%let` assignments
- [x] `%global`, `%local` declarations
- [x] `%if/%then/%else` (basic)
- [x] `%do/%end` blocks (basic, iterative, %while, %until)
- [x] `%put` statements
- [x] `%return`, `%goto`, `%label`
- [x] `%include`
- [x] `%abort`
- [x] Macro function calls (`%sysfunc`, `%str`, `%bquote`, etc.)
- [x] Macro variable references (`&var`, `&var.`)
- [x] User-defined macro calls (`%macroname(args)`)
- [x] Inline macro code generation (macros used as rvalues without semicolons)
- [x] Macro `%if/%then/%do` in data step and proc step headers
- [x] `%macro/%mend` definitions nested inside data steps
- [x] `%str()`, `%nrstr()`, `%bquote()`, etc. with semicolons inside (lexer mode)
- [x] Nested quoting: `%str(%str(ERR)OR)`, `%str(&%%)`
- [x] Escaped parens in quoting: `%str(%(`, `%str(%))`
- [ ] Complex nested macro quoting (edge cases)

### Data Step
- [x] `data/run` structure
- [x] Dataset names with library prefix
- [x] Dataset options `(keep= drop= where= rename=)`
- [x] `set`, `merge`, `by` statements
- [x] `if/then/else`
- [x] `do/end` blocks (simple, iterative, while, until)
- [x] `select/when/otherwise`
- [x] Assignment statements
- [x] `length`, `format`, `informat`, `label`, `attrib`, `retain`
- [x] `array` declarations
- [x] `keep`, `drop`, `where`
- [x] `output`, `delete`, `stop`, `abort`, `return`
- [x] `put`, `input`, `infile`, `file`
- [x] `call` routines
- [x] `goto`, `link`, `leave`, `continue`
- [x] `putlog` statements
- [x] Statement labels (goto targets)
- [x] Array subscripts with curly braces `lev{i}`
- [x] Variable range notation `Var1-VarN`
- [x] Colon variable prefix `Var:`
- [ ] `cards`/`datalines` inline data
- [ ] Subsetting IF (without THEN)

### PROC Step
- [x] Generic `proc name; ... run/quit;` structure
- [x] Proc options
- [ ] PROC SQL (dedicated rules)
- [ ] PROC SORT, PROC PRINT, PROC DATASETS, etc. (dedicated rules)

### Global Statements
- [x] `libname`
- [x] `filename`
- [x] `options`
- [x] `ods`
- [x] `title`, `footnote`
- [x] `dm`, `systask`
- [x] `endsas`

### Expressions
- [x] Arithmetic (+, -, *, /, **)
- [x] Comparison (=, <>, <, >, <=, >=, eq, ne, lt, gt, le, ge)
- [x] Logical (and, or, not)
- [x] String concatenation (||, !!)
- [x] Function calls
- [x] Parenthesized expressions
- [x] Macro variable references in expressions
- [x] Macro function calls in expressions
- [x] String literals with date/time/hex suffixes
- [ ] IN operator with value lists: `x in (1, 2, 3)`
- [ ] BETWEEN operator: `x between 1 and 10`
- [ ] Named literal: `.A` through `.Z` (special missing values)

## Iteration Log

### Iteration 0 — Bootstrap (2026-02-25)
- Created initial grammar with lexer and parser
- sasjs_tests: 78/146 passed (53.4%)
- Top error clusters:
  1. extraneous input (35 files) — macro calls without semicolons, inline expressions
  2. mismatched input (15 files) — function calls in unexpected places
  3. missing semicolons (13 files) — putlog and special statement forms
  4. no viable alternative (5 files) — unrecognized statement patterns

### Session 2 — Core fixes (2026-02-25)
- Major grammar improvements across macro, data step, proc step, and expression rules
- Added proc SQL support, inline macro calls, generic proc body handling
- sasjs_tests + general_purpose_macros: 230/239 (96.2%)

### Session 3 — Final refinements (2026-02-25)
- Made `length`, `array`, `keep`, `drop`, `output` statements generic (`~(SEMI)* SEMI`)
- Added macro `%if/%do` and macro calls to data step headers
- Simplified proc body to fully generic token collector
- Added array subscript expressions, statement labels, nested macro definitions
- Fixed regression with transpose.sas (reverted overly-specific proc option token exclusions)
- **Combined: 238/239 (99.6%)**
- 1 remaining failure: mv_getjobcode.test.sas — `%str()` with embedded SAS code

### Session 4 — Lexer modes for macro quoting (2026-02-25)
- Implemented STR_MODE lexer mode for `%str()`, `%nrstr()`, `%bquote()`, `%nrbquote()`, `%quote()`, `%nrquote()`, `%superq()`, `%unquote()`
- Semicolons inside quoting functions become STR_SEMI (harmless to outer grammar)
- Macro variable references (&var) preserved as distinct STR_MACRO_VAR tokens
- Nested parentheses tracked via pushMode/popMode
- Added STR_AMP for bare ampersands (e.g., `&%%` pattern in parmv.sas)
- **Combined: 239/239 (100.0%)** (sasjs_tests + general_purpose_macros)

### Session 5 — Phase 3: sasjs_base_macros + sasjs_platform_macros (2026-02-26)
- Added sasjs_base_macros (191 files) and sasjs_platform_macros (46 files) — baseline: 449/476 (94.3%)
- Fixed `data ;` null data step (changed `dataStepHeaderItem+` to `*`)
- Added `macroDatasetRef` for macro-based dataset names: `&lib..&ds`, `&pkds.1`, `&&_webin_name&i`
- Made `macroDefOption` generic (`macroDefOptionToken+`) for `/minoperator mindelimiter=' '`
- Made `macroParamDefaultToken` allow SEMI (for `&#x0a;` HTML entities in defaults)
- Added `optionsItem` rule with macro-awareness for inline `%if/%do/%end` in `options` statements
- Added `macroConcatName` for `sasjs&i.data` — identifiers concatenated with macro var references
- Implemented two-pass parsing: SLL first (fast), LL fallback (correct) — handles 372-deep `%else %if` chains
- **Combined: 476/476 (100.0%)** (all four folders)
