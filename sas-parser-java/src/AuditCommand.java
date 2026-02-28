import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.nio.file.*;
import java.util.*;

/**
 * Parses files and walks CST for fallback rules.
 * Outputs JSON array of occurrences to stdout.
 *
 * Usage: audit [--include-proc-body] <files...>
 */
public class AuditCommand {

    public static void run(String[] args) {
        boolean includeProcBody = false;
        boolean showProgress = false;
        List<String> files = new ArrayList<>();

        for (String arg : args) {
            if ("--include-proc-body".equals(arg)) {
                includeProcBody = true;
            } else if ("--progress".equals(arg)) {
                showProgress = true;
            } else {
                files.add(arg);
            }
        }

        if (files.isEmpty()) {
            System.err.println("Usage: audit [--include-proc-body] [--progress] <files...>");
            System.exit(2);
        }

        List<Map<String, Object>> allOccurrences = new ArrayList<>();
        int total = files.size();

        for (int i = 0; i < total; i++) {
            String filepath = files.get(i);
            if (showProgress) {
                String basename = Paths.get(filepath).getFileName().toString();
                System.err.printf("\r  [%d/%d] %s", i + 1, total, basename);
            }
            try {
                List<Map<String, Object>> occs = auditFile(filepath, includeProcBody);
                allOccurrences.addAll(occs);
            } catch (Exception e) {
                System.err.println("\nERROR auditing " + filepath + ": " + e.getMessage());
            }
        }
        if (showProgress) {
            System.err.printf("\r  [%d/%d] done.%s\n", total, total, " ".repeat(40));
        }

        System.out.println(Json.array(allOccurrences));
    }

    static List<Map<String, Object>> auditFile(String filepath, boolean includeProcBody) {
        Path path = Paths.get(filepath);
        if (!Files.isRegularFile(path)) {
            System.err.println("WARNING: File not found: " + filepath);
            return List.of();
        }

        CharStream input = ValidateCommand.createCharStream(path);
        if (input == null) return List.of();

        SasCustomLexer lexer = new SasCustomLexer(input);
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Try SLL first — use SLL tree if it succeeds (much faster)
        SasParser parser = new SasParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {});
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.setErrorHandler(new BailErrorStrategy());

        SasParser.ProgramContext tree;
        try {
            tree = parser.program();
        } catch (Exception e) {
            // SLL failed — re-parse in LL mode
            input = ValidateCommand.createCharStream(path);
            lexer = new SasCustomLexer(input);
            lexer.removeErrorListeners();
            tokens = new CommonTokenStream(lexer);

            parser = new SasParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {});
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            parser.setErrorHandler(new DefaultErrorStrategy());

            tree = parser.program();
        }

        FallbackAuditListener listener = new FallbackAuditListener(parser, filepath, includeProcBody);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);

        return listener.getOccurrences();
    }
}
