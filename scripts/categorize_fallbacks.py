#!/usr/bin/env python3
"""Categorize fallback rule occurrences and generate work orders for fixing.

Runs audit_fallbacks.py on all source folders, classifies each occurrence
into a semantic category using regex heuristics, extracts relevant grammar
context, and generates per-category work-order files for Claude Code subagents.

Usage:
    uv run python scripts/categorize_fallbacks.py
    uv run python scripts/categorize_fallbacks.py --audit-json reports/audit_XXXX.json
"""

import json
import os
import re
import sys
from collections import defaultdict
from datetime import datetime

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
GRAMMAR_DIR = os.path.join(PROJECT_DIR, "grammar")
REPORTS_DIR = os.path.join(PROJECT_DIR, "reports")
WORKORDERS_DIR = os.path.join(REPORTS_DIR, "workorders")
SOURCES_DIR = os.path.join(PROJECT_DIR, "sources", "sas_source_codes")

ALL_FOLDERS = [
    "sasjs_tests",
    "general_purpose_macros",
    "sasjs_base_macros",
    "sasjs_platform_macros",
    "sasjs_ddl_fcmp_lua",
    "packages_framework",
    "clinical_contributed",
    "clinical_examples",
    "clinical_tested",
    "clinical_whitepapers",
]

# ---------------------------------------------------------------------------
# Category classification rules
# Each entry: (category_id, rule_name, snippet_regex, description, fixability)
# First match wins within each rule group.
# ---------------------------------------------------------------------------
# NOTE: Snippets from audit have NO spaces between tokens (ANTLR getText
# concatenates). So `symbol2 v=star` becomes `symbol2v=star`.
# Use prefix-only matching (no \b word boundaries).
CATEGORY_RULES = [
    # sqlGenericStmt categories
    ("sql_macro_in_sql", "sqlGenericStmt", r"^(%end|%else|%do|%if|%let|%put)", "Macro statements leaking into SQL as sqlGenericStmt", "fixable"),
    ("sql_create_complex", "sqlGenericStmt", r"(?i)^create", "CREATE statements not matching sqlCreateStmt", "fixable"),
    ("sql_alter_complex", "sqlGenericStmt", r"(?i)^alter", "ALTER statements not matching sqlAlterStmt", "fixable"),
    ("sql_star_comment", "sqlGenericStmt", r"^\*", "SAS-style * comments inside PROC SQL", "fixable"),
    ("sql_select_complex", "sqlGenericStmt", r"(?i)^select", "SELECT statements not matching sqlSelectStmt", "fixable"),
    ("sql_insert_complex", "sqlGenericStmt", r"(?i)^insert", "INSERT statements not matching sqlInsertStmt", "fixable"),
    ("sql_update_complex", "sqlGenericStmt", r"(?i)^update", "UPDATE statements not matching sqlUpdateStmt", "fixable"),
    ("sql_delete_complex", "sqlGenericStmt", r"(?i)^delete", "DELETE statements not matching sqlDeleteStmt", "fixable"),
    ("sql_drop_complex", "sqlGenericStmt", r"(?i)^drop", "DROP statements not matching sqlDropStmt", "fixable"),
    ("sql_connect_complex", "sqlGenericStmt", r"(?i)^(connect|disconnect|execute)", "CONNECT/DISCONNECT/EXECUTE pass-through not matching", "fixable"),
    ("sql_option_stmt", "sqlGenericStmt", r"(?i)^options?\b", "OPTION/OPTIONS inside PROC SQL", "fixable"),
    ("sql_dynamic_macro", "sqlGenericStmt", r"^[&%]", "Dynamic macro variable/call as entire SQL statement", "expected"),
    ("sql_boundary_leak", "sqlGenericStmt", r"(?i)^(set|if|do|end|file|put|input|call|retain|merge|data)\b", "Data step statement stranded in SQL context (boundary confusion)", "needs_investigation"),
    ("sql_misc", "sqlGenericStmt", r".", "Other unrecognized SQL statements", "needs_investigation"),

    # unknownStatement categories
    ("unknown_star_comment", "unknownStatement", r"^\*", "SAS-style * or ** comment at top level", "expected"),
    ("unknown_run_quit", "unknownStatement", r"(?i)^(run|quit)\s*;?\s*$", "Orphaned RUN/QUIT statement", "fixable"),
    ("unknown_graph_stmt", "unknownStatement", r"(?i)^(goptions|symbol|axis|pattern|legend)", "Graph/ODS global statements not in grammar", "fixable"),
    ("unknown_title_footnote", "unknownStatement", r"(?i)^(title|footnote)", "Title/footnote with complex options not matching", "fixable"),
    ("unknown_ods_stmt", "unknownStatement", r"(?i)^ods", "ODS statements not matching odsStatement", "fixable"),
    ("unknown_dm_stmt", "unknownStatement", r"(?i)^dm", "DM (display manager) statements", "fixable"),
    ("unknown_endsas", "unknownStatement", r"(?i)^endsas", "ENDSAS statement not in grammar", "fixable"),
    ("unknown_lock_stmt", "unknownStatement", r"(?i)^lock", "LOCK statement not in grammar", "fixable"),
    ("unknown_systask_stmt", "unknownStatement", r"(?i)^systask", "SYSTASK statement not in grammar", "fixable"),
    ("unknown_x_stmt", "unknownStatement", r"(?i)^x ", "X (system command) statement", "fixable"),
    ("unknown_cards_stmt", "unknownStatement", r"(?i)^(cards|datalines|lines|cards4|datalines4)", "CARDS/DATALINES not matching", "fixable"),
    ("unknown_else", "unknownStatement", r"(?i)^else", "Orphaned else statement", "fixable"),
    ("unknown_data_step_stmt", "unknownStatement", r"(?i)^(put|input|if|do|end|output|delete|return|link|goto|stop|abort|error|list|call|array|retain|drop|keep|length|format|informat|label|attrib|where|merge|set|update|modify|by|select|when|otherwise|infile|file)", "Data step statement at top level (outside data step — cascading parse failure)", "fixable"),
    ("unknown_metadata", "unknownStatement", r"(?i)^(type|title|version|license|author|description|required|encoding|macro|homepage|package):", "Non-SAS metadata/documentation in .sas file", "expected"),
    ("unknown_assignment", "unknownStatement", r"(?i)^[\w&%.]+\s*=", "Assignment statement at top level (cascading parse failure)", "fixable"),
    ("unknown_fcmp_fragment", "unknownStatement", r"(?i)^(function|subroutine|endsub|return)\b", "PROC FCMP function fragment file", "expected"),
    ("unknown_format_fragment", "unknownStatement", r"(?i)^(value|invalue|picture)\b", "PROC FORMAT value fragment file", "expected"),
    ("unknown_misc", "unknownStatement", r".", "Other unrecognized top-level statements", "needs_investigation"),

    # macroGenericText categories
    ("macro_star_comment", "macroGenericText", r"^\*", "SAS-style * or ** comment in macro body", "expected"),
    ("macro_data_step", "macroGenericText", r"(?i)^data[^=]", "DATA step inside macro body parsed as generic text", "fixable"),
    ("macro_proc_step", "macroGenericText", r"(?i)^proc", "PROC step inside macro body parsed as generic text", "fixable"),
    ("macro_put_stmt", "macroGenericText", r"(?i)^put[^_]", "PUT statement in macro body", "fixable"),
    ("macro_input_stmt", "macroGenericText", r"(?i)^input[^_]", "INPUT statement in macro body", "fixable"),
    ("macro_if_stmt", "macroGenericText", r"(?i)^if[^_]", "IF-THEN-ELSE in macro body", "fixable"),
    ("macro_select_when", "macroGenericText", r"(?i)^(select|when|otherwise)", "SELECT/WHEN/OTHERWISE in macro body", "fixable"),
    ("macro_do_end", "macroGenericText", r"(?i)^(do[^_a-z]|end[^_a-z]|else)", "DO/END/ELSE blocks in macro body", "fixable"),
    ("macro_call_stmt", "macroGenericText", r"(?i)^call", "CALL statements in macro body", "fixable"),
    ("macro_output_run", "macroGenericText", r"(?i)^(output|run|quit)", "OUTPUT/RUN/QUIT statements in macro body", "fixable"),
    ("macro_retain_stmt", "macroGenericText", r"(?i)^retain", "RETAIN statements in macro body", "fixable"),
    ("macro_data_step_stmt", "macroGenericText", r"(?i)^(merge|set|delete|return|link|goto|stop|abort|error|drop|keep|length|format|informat|label|attrib|where|by|array|infile|file|modify|page)", "Data step statements in macro body", "fixable"),
    ("macro_global_stmt", "macroGenericText", r"(?i)^(libname|filename|options|ods|title|footnote)", "Global statements in macro body", "fixable"),
    ("macro_assignment", "macroGenericText", r"(?i)^[\w&%.]+\s*[=+\-]", "Assignment/accumulator statement in macro body", "fixable"),
    ("macro_expected_text", "macroGenericText", r"(?i)^[&A-Z0-9_/\\@#${}.<>]+$", "Short identifier/path/macro-var text — likely expected macro template text", "expected"),
    ("macro_str_return", "macroGenericText", r"(?i)^%(n?r?str|quote|bquote|superq|nrbquote|nrquote)\(", "Macro quoting function return — expected template text", "expected"),
    ("macro_dynamic_call", "macroGenericText", r"^%&", "Dynamic macro call %&varname — expected macro dispatch", "expected"),
    ("macro_value_return", "macroGenericText", r"(?i)^(%eval|%sysevalf|%sysfunc|%scan|%substr|%upcase|%lowcase|%unquote|%qsysfunc|%qscan|%qsubstr|%qupcase|%qlowcase|%cmpres|%qcmpres|%left|%qleft|%trim|%qtrim)\(", "Macro function call as value-returning expression — expected", "expected"),
    ("macro_html_xml", "macroGenericText", r"(?i)^<", "HTML/XML markup in macro body — expected template text", "expected"),
    ("macro_code_fragment", "macroGenericText", r"(?i)^(where|and |or |on |having |order |group |from |into |join |left |right |inner |outer |cross |natural |union |except |intersect |set |not |when |then |else |otherwise |,)", "Code fragment (no trailing SEMI) inside macro %do/%end — expected template injection", "expected"),
    ("macro_string_fragment", "macroGenericText", r'^["\']', "String literal fragment in macro %do/%end — expected template injection", "expected"),
    ("macro_paren_fragment", "macroGenericText", r'^[()]', "Bare parenthesis/punctuation injection in macro %do/%end — expected template injection", "expected"),
    ("macro_identifier_list", "macroGenericText", r"(?i)^[\w&%.]+(\s+[\w&%.]+)+\s*$", "Space-separated identifier/variable list — expected template injection", "expected"),
    ("macro_expression_fragment", "macroGenericText", r"(?i)^[\w&%.(]+", "Expression fragment in macro body — expected template text", "expected"),
    ("macro_negative_literal", "macroGenericText", r"^-", "Negative number return value in macro body", "expected"),
    ("macro_slash_fragment", "macroGenericText", r"^/", "Slash-prefix option or put-continuation injection", "expected"),
    ("macro_operator_fragment", "macroGenericText", r"^[|!]", "String concatenation operator (|| or !!) injection", "expected"),
    ("macro_tilde_template", "macroGenericText", r"^~", "Tilde-delimited template string (custom CSV format)", "expected"),
    ("macro_colon_fragment", "macroGenericText", r"^:", "PROC SQL INTO :macrovar reference injection", "expected"),
    ("macro_css_js_fragment", "macroGenericText", r"^[}$]", "CSS/jQuery fragment in HTML-generating macro", "expected"),
    ("macro_misc", "macroGenericText", r".", "Other macro body text needing investigation", "needs_investigation"),
]


def classify_occurrence(occ: dict) -> str:
    """Return the category_id for an occurrence."""
    rule = occ["rule"]
    snippet = occ["snippet"].strip()

    for cat_id, cat_rule, pattern, _desc, _fix in CATEGORY_RULES:
        if cat_rule != rule:
            continue
        if re.search(pattern, snippet):
            return cat_id

    # Shouldn't reach here — last rule per group matches "."
    return f"{rule}_uncategorized"


def get_category_info(cat_id: str) -> tuple[str, str]:
    """Return (description, fixability) for a category."""
    for cid, _rule, _pat, desc, fix in CATEGORY_RULES:
        if cid == cat_id:
            return desc, fix
    return "Uncategorized", "needs_investigation"


def get_rule_name_from_category(cat_id: str) -> str:
    """Return the fallback rule name for a category."""
    for cid, rule, _pat, _desc, _fix in CATEGORY_RULES:
        if cid == cat_id:
            return rule
    return "unknown"


def run_full_audit() -> dict:
    """Run audit on all folders, using Java parser when available."""
    sys.path.insert(0, os.path.join(PROJECT_DIR, "scripts"))
    import audit_fallbacks

    folders = ALL_FOLDERS
    files = audit_fallbacks.find_sas_files(folders)
    if not files:
        print("ERROR: No .sas files found.", file=sys.stderr)
        sys.exit(1)

    print(f"Auditing {len(files)} files across {len(folders)} folders...")

    all_occurrences = []
    errors = []
    start_time = datetime.now()

    # Try Java audit first (single JVM, all files at once — much faster)
    use_java = audit_fallbacks._java_available()
    if use_java:
        try:
            print("  (using Java parser)")
            all_occurrences = audit_fallbacks.audit_files_java(files, include_proc_body=False)
        except Exception as e:
            print(f"  Java audit failed ({e}), falling back to Python...", file=sys.stderr)
            use_java = False

    if not use_java:
        print("  (using Python parser — this will be slow)")
        try:
            from tqdm import tqdm
            pbar = tqdm(files, desc="Auditing", unit="file", dynamic_ncols=True)
            for filepath in pbar:
                basename = os.path.basename(filepath)
                pbar.set_postfix_str(basename, refresh=True)
                try:
                    occurrences = audit_fallbacks.audit_file(filepath)
                    all_occurrences.extend(occurrences)
                except Exception as e:
                    errors.append({"file": filepath, "error": str(e)})
        except ImportError:
            for i, filepath in enumerate(files):
                if (i + 1) % 50 == 0 or i == 0:
                    basename = os.path.basename(filepath)
                    print(f"  [{i+1}/{len(files)}] {basename}...")
                try:
                    occurrences = audit_fallbacks.audit_file(filepath)
                    all_occurrences.extend(occurrences)
                except Exception as e:
                    errors.append({"file": filepath, "error": str(e)})

    elapsed = (datetime.now() - start_time).total_seconds()
    print(f"  Done: {len(all_occurrences)} occurrences found in {elapsed:.1f}s")

    if errors:
        print(f"  Errors during audit: {len(errors)} files")

    # Build report in same format as audit_fallbacks.py JSON output
    json_occurrences = []
    for occ in all_occurrences:
        entry = dict(occ)
        entry["file"] = os.path.relpath(entry["file"], PROJECT_DIR)
        if "source_context" in entry:
            del entry["source_context"]
        json_occurrences.append(entry)

    report = {
        "timestamp": datetime.now().isoformat(),
        "folders": folders,
        "total_files": len(files),
        "elapsed_secs": round(elapsed, 1),
        "occurrences": json_occurrences,
    }

    # Save to reports dir
    os.makedirs(REPORTS_DIR, exist_ok=True)
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    report_path = os.path.join(REPORTS_DIR, f"audit_{timestamp}.json")
    with open(report_path, "w") as f:
        json.dump(report, f, indent=2)
    print(f"  Audit report saved: {report_path}")

    return report


def load_audit_json(path: str) -> dict:
    """Load an existing audit JSON."""
    with open(path) as f:
        return json.load(f)


def read_source_context(filepath: str, start_line: int, end_line: int, context=3) -> list[str]:
    """Read source lines around an occurrence."""
    abs_path = os.path.join(PROJECT_DIR, filepath) if not os.path.isabs(filepath) else filepath
    try:
        with open(abs_path, encoding="utf-8") as f:
            lines = f.readlines()
    except (UnicodeDecodeError, FileNotFoundError):
        try:
            with open(abs_path, encoding="latin-1") as f:
                lines = f.readlines()
        except FileNotFoundError:
            return [f"  (file not found: {filepath})"]

    result = []
    for i in range(max(0, start_line - context - 1), min(len(lines), end_line + context)):
        line_text = lines[i].rstrip()
        marker = ">>>" if start_line <= (i + 1) <= end_line else "   "
        result.append(f"  {marker} {i+1:4d} | {line_text}")
    return result


def extract_grammar_rules(rule_names: list[str]) -> dict[str, tuple[int, int, list[str]]]:
    """Extract grammar rules and their line ranges from SasParser.g4.

    Returns {rule_name: (start_line, end_line, lines)}.
    """
    parser_path = os.path.join(GRAMMAR_DIR, "SasParser.g4")
    with open(parser_path) as f:
        all_lines = f.readlines()

    results = {}
    # Find rule definitions: "ruleName" at start of line (with optional whitespace)
    rule_starts = {}
    for i, line in enumerate(all_lines):
        m = re.match(r'^(\w+)\s*$', line.rstrip())
        if m:
            rule_starts[m.group(1)] = i
        # Also match "ruleName :" on same line
        m = re.match(r'^(\w+)\s*:', line.rstrip())
        if m:
            rule_starts[m.group(1)] = i

    for rule_name in rule_names:
        if rule_name not in rule_starts:
            continue
        start = rule_starts[rule_name]
        # Find end: next line with ';' at start or a new rule definition
        end = start
        for i in range(start + 1, len(all_lines)):
            stripped = all_lines[i].strip()
            if stripped == ";":
                end = i
                break
            # Another rule definition starts
            if re.match(r'^\w+\s*$', stripped) or re.match(r'^\w+\s*:', stripped):
                end = i - 1
                break
            end = i

        # Include a few lines of context before
        ctx_start = max(0, start - 2)
        ctx_end = min(len(all_lines) - 1, end + 2)
        lines = [all_lines[j].rstrip() for j in range(ctx_start, ctx_end + 1)]
        results[rule_name] = (ctx_start + 1, ctx_end + 1, lines)

    return results


def determine_relevant_rules(cat_id: str, parent_rules: set[str]) -> list[str]:
    """Determine which grammar rules are relevant for a category."""
    rule = get_rule_name_from_category(cat_id)
    rules = [rule]

    # Add parent rules
    for p in parent_rules:
        if p not in rules:
            rules.append(p)

    # Add related rules based on category
    related = {
        "sql_": ["procSqlStep", "sqlStatement", "sqlSelectStmt", "sqlCreateStmt",
                 "sqlInsertStmt", "sqlUpdateStmt", "sqlDeleteStmt", "sqlAlterStmt",
                 "sqlDropStmt", "sqlGenericStmt"],
        "unknown_graph": ["globalStatement", "unknownStatement"],
        "unknown_title": ["globalStatement", "titleStatement", "unknownStatement"],
        "unknown_ods": ["globalStatement", "odsStatement", "unknownStatement"],
        "unknown_": ["globalStatement", "programStatement", "unknownStatement"],
        "macro_data": ["macroBody", "macroDoBody", "macroGenericText", "macroBodyStatement",
                       "macroDoBodyStatement"],
        "macro_proc": ["macroBody", "macroDoBody", "macroGenericText", "macroBodyStatement",
                       "macroDoBodyStatement"],
        "macro_put": ["macroBody", "macroDoBody", "macroGenericText", "macroDoBodyStatement"],
        "macro_if": ["macroBody", "macroDoBody", "macroGenericText", "macroDoBodyStatement"],
        "macro_assignment": ["macroBody", "macroDoBody", "macroGenericText", "macroDoBodyStatement"],
        "macro_do": ["macroBody", "macroDoBody", "macroGenericText", "macroDoBodyStatement"],
        "macro_call": ["macroBody", "macroDoBody", "macroGenericText", "macroDoBodyStatement"],
        "macro_output": ["macroBody", "macroDoBody", "macroGenericText", "macroDoBodyStatement"],
        "macro_global": ["macroBody", "macroDoBody", "macroGenericText", "macroDoBodyStatement"],
    }

    for prefix, related_rules in related.items():
        if cat_id.startswith(prefix):
            for r in related_rules:
                if r not in rules:
                    rules.append(r)
            break

    return rules


def generate_work_order(cat_id: str, cat_data: dict, grammar_context: dict) -> str:
    """Generate a work-order markdown file for a category."""
    desc, fixability = get_category_info(cat_id)
    rule = get_rule_name_from_category(cat_id)
    folders_affected = sorted(cat_data["folders_affected"])

    lines = []
    lines.append(f"# Work Order: {cat_id}")
    lines.append("")
    lines.append("## Summary")
    lines.append(f"- **Rule**: `{rule}` (fallback being triggered)")
    lines.append(f"- **Count**: {cat_data['count']} occurrences in {cat_data['file_count']} files")
    lines.append(f"- **Parent rules**: {', '.join(sorted(cat_data['parent_rules']))}")
    lines.append(f"- **Folders affected**: {', '.join(folders_affected)}")
    lines.append(f"- **Fixability**: {fixability}")
    lines.append(f"- **Description**: {desc}")
    lines.append("")

    # Grammar context
    lines.append("## Grammar Context")
    for rule_name, (start, end, rule_lines) in grammar_context.items():
        lines.append(f"\n### `{rule_name}` (SasParser.g4, lines {start}-{end})")
        lines.append("```antlr")
        for rl in rule_lines:
            lines.append(rl)
        lines.append("```")
    lines.append("")

    # Sample occurrences
    samples = cat_data["samples"][:10]
    lines.append(f"## Sample Occurrences ({len(samples)} of {cat_data['count']})")
    for i, sample in enumerate(samples, 1):
        relpath = sample["file"]
        lines.append(f"\n### Sample {i}: {relpath}:L{sample['start_line']}")
        lines.append(f"- **Snippet**: `{sample['snippet']}`")
        lines.append(f"- **Parent**: `{sample['parent']}`")
        if sample.get("prev_sibling"):
            lines.append(f"- **Prev sibling**: `{sample['prev_sibling']}`")
        if sample.get("next_sibling"):
            lines.append(f"- **Next sibling**: `{sample['next_sibling']}`")
        lines.append("```sas")
        for ctx_line in sample.get("source_context", []):
            lines.append(ctx_line)
        lines.append("```")
    lines.append("")

    # Fix instructions
    lines.append("## Fix Instructions")
    lines.append("1. Analyze the samples above to understand what syntax is being missed")
    lines.append("2. Propose and apply a grammar change to `grammar/SasParser.g4`")
    lines.append("3. If adding new keywords to `SasLexer.g4`, also add them to the `identifier` rule in `SasParser.g4`")
    lines.append("4. Run: `bash scripts/build.sh`")
    lines.append(f"5. Run: `uv run python scripts/audit_fallbacks.py {' '.join(folders_affected)}`")
    lines.append(f"6. Verify the `{cat_id}` category count decreased")
    lines.append(f"7. Run: `uv run python scripts/batch_test.py {' '.join(folders_affected)}`")
    lines.append("8. Verify 0 regressions (same or better pass count as before)")
    lines.append("9. Report back: what changed, before/after count, any issues")

    return "\n".join(lines)


def categorize(audit_data: dict) -> dict:
    """Categorize all occurrences and build category summaries."""
    occurrences = audit_data["occurrences"]
    categories = defaultdict(lambda: {
        "count": 0,
        "file_count": 0,
        "files": set(),
        "folders_affected": set(),
        "parent_rules": set(),
        "samples": [],
    })

    for occ in occurrences:
        cat_id = classify_occurrence(occ)
        cat = categories[cat_id]
        cat["count"] += 1
        cat["files"].add(occ["file"])
        cat["parent_rules"].add(occ["parent"])

        # Extract folder name from file path
        # e.g. "sources/sas_source_codes/sasjs_tests/foo.sas" -> "sasjs_tests"
        parts = occ["file"].replace("\\", "/").split("/")
        for i, p in enumerate(parts):
            if p == "sas_source_codes" and i + 1 < len(parts):
                cat["folders_affected"].add(parts[i + 1])
                break

        # Keep first 10 samples with source context
        if len(cat["samples"]) < 10:
            sample = dict(occ)
            # Add source context if not present
            if "source_context" not in sample:
                sample["source_context"] = read_source_context(
                    sample["file"], sample["start_line"], sample["end_line"]
                )
            cat["samples"].append(sample)

    # Compute file counts
    for cat in categories.values():
        cat["file_count"] = len(cat["files"])

    return dict(categories)


def build_manifest(categories: dict) -> list[dict]:
    """Build sorted manifest of categories for output."""
    fixability_order = {"fixable": 0, "needs_investigation": 1, "expected": 2}
    manifest = []

    for cat_id, cat_data in categories.items():
        desc, fixability = get_category_info(cat_id)
        manifest.append({
            "category_id": cat_id,
            "rule": get_rule_name_from_category(cat_id),
            "description": desc,
            "fixability": fixability,
            "count": cat_data["count"],
            "file_count": cat_data["file_count"],
            "folders_affected": sorted(cat_data["folders_affected"]),
            "parent_rules": sorted(cat_data["parent_rules"]),
        })

    # Sort: fixable first, then by count descending
    manifest.sort(key=lambda x: (fixability_order.get(x["fixability"], 9), -x["count"]))
    return manifest


def main():
    args = sys.argv[1:]
    audit_json_path = None

    if "--audit-json" in args:
        idx = args.index("--audit-json")
        if idx + 1 >= len(args):
            print("ERROR: --audit-json requires a path argument", file=sys.stderr)
            sys.exit(2)
        audit_json_path = args[idx + 1]
        args = args[:idx] + args[idx + 2:]

    # Load or run audit
    if audit_json_path:
        print(f"Loading audit from: {audit_json_path}")
        audit_data = load_audit_json(audit_json_path)
    else:
        audit_data = run_full_audit()

    total_occs = len(audit_data["occurrences"])
    print(f"\nTotal fallback occurrences: {total_occs}")

    if total_occs == 0:
        print("No fallback occurrences found. Nothing to categorize.")
        sys.exit(0)

    # Categorize
    categories = categorize(audit_data)
    manifest = build_manifest(categories)

    # Print summary
    print(f"\n{'='*70}")
    print(f"Fallback Categories — {datetime.now().isoformat()}")
    print(f"{'='*70}")
    print(f"{'Category':<30s} {'Fix?':<20s} {'Count':>8s} {'Files':>8s}")
    print(f"{'-'*30} {'-'*20} {'-'*8} {'-'*8}")

    total_fixable = 0
    total_expected = 0
    total_investigate = 0

    for entry in manifest:
        print(f"{entry['category_id']:<30s} {entry['fixability']:<20s} {entry['count']:>8d} {entry['file_count']:>8d}")
        if entry["fixability"] == "fixable":
            total_fixable += entry["count"]
        elif entry["fixability"] == "expected":
            total_expected += entry["count"]
        else:
            total_investigate += entry["count"]

    print(f"\nFixable: {total_fixable}, Expected: {total_expected}, Needs investigation: {total_investigate}")

    # Save category manifest JSON
    os.makedirs(REPORTS_DIR, exist_ok=True)
    manifest_path = os.path.join(REPORTS_DIR, "fallback_categories.json")
    with open(manifest_path, "w") as f:
        json.dump({
            "timestamp": datetime.now().isoformat(),
            "total_occurrences": total_occs,
            "total_fixable": total_fixable,
            "total_expected": total_expected,
            "total_investigate": total_investigate,
            "categories": manifest,
        }, f, indent=2)
    print(f"\nManifest: {manifest_path}")

    # Generate work orders for fixable categories
    os.makedirs(WORKORDERS_DIR, exist_ok=True)

    # Clean old work orders
    for f in os.listdir(WORKORDERS_DIR):
        os.remove(os.path.join(WORKORDERS_DIR, f))

    fixable_count = 0
    for i, entry in enumerate(manifest):
        if entry["fixability"] != "fixable":
            continue
        fixable_count += 1

        cat_id = entry["category_id"]
        cat_data = categories[cat_id]

        # Extract relevant grammar rules
        relevant_rules = determine_relevant_rules(cat_id, cat_data["parent_rules"])
        grammar_context = extract_grammar_rules(relevant_rules)

        # Generate work order
        work_order = generate_work_order(cat_id, cat_data, grammar_context)
        wo_path = os.path.join(WORKORDERS_DIR, f"{fixable_count:02d}_{cat_id}.md")
        with open(wo_path, "w") as f:
            f.write(work_order)

    print(f"Work orders: {fixable_count} files in {WORKORDERS_DIR}")

    # Also save a compact processing order for the main agent
    processing_order_path = os.path.join(REPORTS_DIR, "processing_order.json")
    processing_order = []
    for i, entry in enumerate(manifest):
        if entry["fixability"] != "fixable":
            continue
        processing_order.append({
            "category_id": entry["category_id"],
            "count": entry["count"],
            "file_count": entry["file_count"],
            "folders": entry["folders_affected"],
            "work_order": f"reports/workorders/{len(processing_order)+1:02d}_{entry['category_id']}.md",
        })

    with open(processing_order_path, "w") as f:
        json.dump(processing_order, f, indent=2)
    print(f"Processing order: {processing_order_path}")


if __name__ == "__main__":
    main()
