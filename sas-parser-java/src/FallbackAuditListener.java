import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.*;
import java.util.*;

/**
 * ParseTreeListener that records occurrences of fallback/catch-all rules:
 *   - unknownStatement
 *   - sqlGenericStmt
 *   - macroGenericText
 *   - procBodyStatement (opt-in)
 *
 * Output format matches the Python audit_fallbacks.py JSON structure.
 */
public class FallbackAuditListener extends SasParserBaseListener {

    private final SasParser parser;
    private final String filepath;
    private final boolean includeProcBody;
    private final List<Map<String, Object>> occurrences = new ArrayList<>();

    public FallbackAuditListener(SasParser parser, String filepath, boolean includeProcBody) {
        this.parser = parser;
        this.filepath = filepath;
        this.includeProcBody = includeProcBody;
    }

    public List<Map<String, Object>> getOccurrences() {
        return occurrences;
    }

    @Override
    public void enterUnknownStatement(SasParser.UnknownStatementContext ctx) {
        record("unknownStatement", ctx);
    }

    @Override
    public void enterSqlGenericStmt(SasParser.SqlGenericStmtContext ctx) {
        record("sqlGenericStmt", ctx);
    }

    @Override
    public void enterMacroGenericText(SasParser.MacroGenericTextContext ctx) {
        record("macroGenericText", ctx);
    }

    @Override
    public void enterProcBodyStatement(SasParser.ProcBodyStatementContext ctx) {
        if (includeProcBody) {
            record("procBodyStatement", ctx);
        }
    }

    private void record(String ruleName, ParserRuleContext ctx) {
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop() != null ? ctx.getStop().getLine() : startLine;
        String snippet = getSnippet(ctx, 120);
        String parentName = ctx.getParent() != null ? getRuleName(ctx.getParent()) : "root";

        String[] siblings = getSiblingInfo(ctx);

        Map<String, Object> occ = new LinkedHashMap<>();
        occ.put("rule", ruleName);
        occ.put("file", filepath);
        occ.put("start_line", startLine);
        occ.put("end_line", endLine);
        occ.put("snippet", snippet);
        occ.put("parent", parentName);
        occ.put("prev_sibling", siblings[0]);
        occ.put("next_sibling", siblings[1]);
        occurrences.add(occ);
    }

    private String getSnippet(ParserRuleContext ctx, int maxLen) {
        // Use the underlying CharStream to preserve whitespace between tokens
        CharStream cs = ctx.getStart().getInputStream();
        int startIdx = ctx.getStart().getStartIndex();
        int stopIdx = ctx.getStop() != null ? ctx.getStop().getStopIndex() : startIdx;
        // Limit to ~200 chars from the source
        stopIdx = Math.min(stopIdx, startIdx + 200);
        String text = cs.getText(Interval.of(startIdx, stopIdx));
        // Collapse whitespace for readability
        text = text.replaceAll("\\s+", " ").trim();
        if (text.length() > maxLen) {
            text = text.substring(0, maxLen) + "...";
        }
        return text;
    }

    static String getRuleName(RuleContext ctx) {
        String name = ctx.getClass().getSimpleName();
        if (name.endsWith("Context")) {
            name = name.substring(0, name.length() - 7);
        }
        if (!name.isEmpty()) {
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
        return name;
    }

    private String[] getSiblingInfo(ParserRuleContext ctx) {
        String prevInfo = null;
        String nextInfo = null;

        RuleContext parent = ctx.getParent();
        if (parent instanceof ParserRuleContext parentCtx) {
            int childCount = parentCtx.getChildCount();
            int idx = -1;
            for (int i = 0; i < childCount; i++) {
                if (parentCtx.getChild(i) == ctx) {
                    idx = i;
                    break;
                }
            }

            if (idx >= 0) {
                // Search backwards for previous rule context
                for (int i = idx - 1; i >= 0; i--) {
                    ParseTree child = parentCtx.getChild(i);
                    if (child instanceof ParserRuleContext ruleCtx) {
                        String name = getRuleName(ruleCtx);
                        int line = ruleCtx.getStart().getLine();
                        prevInfo = name + " (L" + line + ")";
                        break;
                    }
                }
                // Search forwards for next rule context
                for (int i = idx + 1; i < childCount; i++) {
                    ParseTree child = parentCtx.getChild(i);
                    if (child instanceof ParserRuleContext ruleCtx) {
                        String name = getRuleName(ruleCtx);
                        int line = ruleCtx.getStart().getLine();
                        nextInfo = name + " (L" + line + ")";
                        break;
                    }
                }
            }
        }

        return new String[]{prevInfo, nextInfo};
    }
}
