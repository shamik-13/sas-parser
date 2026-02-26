import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Validates SAS files against the ANTLR grammar.
 *
 * Usage:
 *   java SasValidate <file.sas>           - validate single file
 *   java SasValidate --batch <file1> ...  - validate multiple files, output JSON per line
 */
public class SasValidate {

    static class ErrorInfo {
        int line, column;
        String message, sourceLine, context;

        ErrorInfo(int line, int column, String message, String sourceLine, String context) {
            this.line = line;
            this.column = column;
            this.message = message;
            this.sourceLine = sourceLine;
            this.context = context;
        }
    }

    static class CollectingErrorListener extends BaseErrorListener {
        List<ErrorInfo> errors = new ArrayList<>();
        List<String> sourceLines;

        CollectingErrorListener(List<String> sourceLines) {
            this.sourceLines = sourceLines;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e) {
            String sourceLine = (line > 0 && line <= sourceLines.size())
                ? sourceLines.get(line - 1) : "";
            String context = (offendingSymbol instanceof Token)
                ? ((Token) offendingSymbol).getText() : "";
            errors.add(new ErrorInfo(line, charPositionInLine, msg, sourceLine, context));
        }
    }

    public static List<ErrorInfo> validateFile(String filepath) throws IOException {
        List<String> sourceLines = Files.readAllLines(Path.of(filepath));
        CharStream input = CharStreams.fromPath(Path.of(filepath));

        SasLexer lexer = new SasLexer(input);
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass 1: SLL (fast)
        SasParser parser = new SasParser(tokens);
        parser.removeErrorListeners();
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.setErrorHandler(new BailErrorStrategy());
        try {
            parser.program();
            return null;
        } catch (Exception e) { /* fall through to LL */ }

        // Pass 2: LL (correct)
        tokens.seek(0);
        parser.reset();
        parser.getInterpreter().setPredictionMode(PredictionMode.LL);
        parser.setErrorHandler(new DefaultErrorStrategy());

        CollectingErrorListener el = new CollectingErrorListener(sourceLines);
        CollectingErrorListener ll = new CollectingErrorListener(sourceLines);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ll);
        parser.removeErrorListeners();
        parser.addErrorListener(el);

        try {
            parser.program();
        } catch (Exception e) {
            el.errors.add(new ErrorInfo(0, 0, "Parser exception: " + e.getMessage(), "", ""));
        }

        List<ErrorInfo> all = new ArrayList<>(ll.errors);
        all.addAll(el.errors);
        return all.isEmpty() ? null : all;
    }

    static String esc(String s) {
        return s == null ? "" : s.replace("\\","\\\\").replace("\"","\\\"")
            .replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java SasValidate <file.sas>");
            System.err.println("       java SasValidate --batch <f1> <f2> ...");
            System.exit(2);
        }

        if (args[0].equals("--batch")) {
            int passed = 0, failed = 0;
            int total_files = args.length - 1;
            long batchStart = System.currentTimeMillis();
            for (int i = 1; i < args.length; i++) {
                long fileStart = System.currentTimeMillis();
                try {
                    List<ErrorInfo> errs = validateFile(args[i]);
                    long elapsed = System.currentTimeMillis() - fileStart;
                    String timeStr = String.format("%.1fs", elapsed / 1000.0);
                    if (errs == null) {
                        passed++;
                        System.out.println("PASS: " + args[i] + " (" + timeStr + ") [" + (passed + failed) + "/" + total_files + "]");
                    } else {
                        failed++;
                        System.out.println("FAIL: " + args[i] + " (" + errs.size() + " errors, " + timeStr + ") [" + (passed + failed) + "/" + total_files + "]");
                        for (ErrorInfo e : errs) {
                            System.out.println("  L" + e.line + ": " + e.message);
                        }
                    }
                } catch (Exception e) {
                    long elapsed = System.currentTimeMillis() - fileStart;
                    failed++;
                    System.out.println("ERROR: " + args[i] + " - " + e.getMessage() + " (" + String.format("%.1fs", elapsed / 1000.0) + ")");
                }
                System.out.flush();
            }
            long batchElapsed = System.currentTimeMillis() - batchStart;
            int total = passed + failed;
            System.out.printf("%nPassed: %d/%d (%.1f%%) in %.1fs%n", passed, total,
                total > 0 ? 100.0 * passed / total : 0.0, batchElapsed / 1000.0);
            System.exit(failed > 0 ? 1 : 0);
        } else {
            List<ErrorInfo> errors = validateFile(args[0]);
            if (errors == null) {
                System.out.println("PASS: " + args[0]);
                System.exit(0);
            } else {
                System.out.println("FAIL: " + args[0] + " (" + errors.size() + " errors)");
                for (ErrorInfo e : errors) {
                    System.out.println("  L" + e.line + ": " + e.message);
                }
                System.exit(1);
            }
        }
    }
}
