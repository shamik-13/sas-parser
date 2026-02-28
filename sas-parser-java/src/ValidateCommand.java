import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Two-pass SLL->LL validation with JSON output matching Python validate.py format.
 *
 * Single file:  validate <file>       -> exit 0 (PASS) or exit 1 + JSON errors
 * Batch mode:   validate --batch <files...> -> JSONL on stdout (one line per file)
 */
public class ValidateCommand {

    public static void run(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: validate <file> | validate --batch <files...>");
            System.exit(2);
        }

        if ("--batch".equals(args[0])) {
            boolean showProgress = false;
            List<String> fileList = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                if ("--progress".equals(args[i])) {
                    showProgress = true;
                } else {
                    fileList.add(args[i]);
                }
            }
            runBatch(fileList.toArray(new String[0]), showProgress);
        } else {
            runSingle(args[0]);
        }
    }

    private static void runSingle(String filepath) {
        List<Map<String, Object>> errors = validateFile(filepath);
        if (errors == null) {
            System.out.println("PASS: " + filepath);
            System.exit(0);
        } else {
            String json = Json.object(
                "file", filepath,
                "error_count", errors.size(),
                "errors", errors
            );
            System.out.println(json);
            System.exit(1);
        }
    }

    private static void runBatch(String[] files, boolean showProgress) {
        int total = files.length;
        for (int i = 0; i < total; i++) {
            String filepath = files[i];
            if (showProgress) {
                String basename = Paths.get(filepath).getFileName().toString();
                System.err.printf("\r  [%d/%d] %s", i + 1, total, basename);
            }
            List<Map<String, Object>> errors = validateFile(filepath);
            if (errors == null) {
                System.out.println(Json.object("file", filepath, "status", "pass"));
            } else {
                System.out.println(Json.object(
                    "file", filepath,
                    "status", "fail",
                    "error_count", errors.size(),
                    "errors", errors
                ));
            }
        }
        if (showProgress) {
            System.err.printf("\r  [%d/%d] done.%s\n", total, total, " ".repeat(40));
        }
    }

    /**
     * Validate a single SAS file using two-pass SLL->LL parsing.
     * Returns null on success, or a list of error maps on failure.
     */
    static List<Map<String, Object>> validateFile(String filepath) {
        Path path = Paths.get(filepath);
        if (!Files.isRegularFile(path)) {
            System.err.println("ERROR: File not found: " + filepath);
            return List.of(Map.of(
                "line", 0, "column", 0,
                "message", "File not found: " + filepath,
                "source_line", "", "context", ""
            ));
        }

        // Read source lines for error context
        String[] sourceLines = readSourceLines(path);

        // Create ANTLR input stream with UTF-8/Latin-1 fallback
        CharStream input = createCharStream(path);
        if (input == null) {
            return List.of(Map.of(
                "line", 0, "column", 0,
                "message", "Cannot read file: " + filepath,
                "source_line", "", "context", ""
            ));
        }

        // Create lexer and token stream
        SasCustomLexer lexer = new SasCustomLexer(input);
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // --- Pass 1: Fast SLL prediction mode ---
        SasParser parser = new SasParser(tokens);
        parser.removeErrorListeners();
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.setErrorHandler(new BailErrorStrategy());

        try {
            parser.program();
            // SLL succeeded — PASS
            return null;
        } catch (Exception e) {
            // SLL failed — fall through to LL
        }

        // --- Pass 2: Full LL prediction mode with error collection ---
        // Reuse the same token stream (already fully tokenized during SLL pass).
        // This matches Python behavior: lexer errors are NOT re-collected since
        // the token stream is already populated. Only parser errors are reported.
        tokens.seek(0);
        parser.reset();
        parser.getInterpreter().setPredictionMode(PredictionMode.LL);
        parser.setErrorHandler(new DefaultErrorStrategy());

        CollectingErrorListener parserListener = new CollectingErrorListener(sourceLines);
        parser.removeErrorListeners();
        parser.addErrorListener(parserListener);

        try {
            parser.program();
        } catch (Exception e) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("line", 0);
            err.put("column", 0);
            err.put("message", "Parser exception: " + e.getMessage());
            err.put("source_line", "");
            err.put("context", "");
            parserListener.getErrors().add(err);
        }

        return parserListener.hasErrors() ? parserListener.getErrors() : null;
    }

    static String[] readSourceLines(Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8).split("\n", -1);
        } catch (Exception e1) {
            try {
                return Files.readString(path, StandardCharsets.ISO_8859_1).split("\n", -1);
            } catch (Exception e2) {
                return new String[0];
            }
        }
    }

    static CharStream createCharStream(Path path) {
        try {
            return CharStreams.fromPath(path, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            try {
                return CharStreams.fromPath(path, StandardCharsets.ISO_8859_1);
            } catch (Exception e2) {
                System.err.println("ERROR: Cannot read file: " + path);
                return null;
            }
        }
    }
}
