import org.antlr.v4.runtime.*;
import java.util.*;

/**
 * Error listener that collects syntax errors into a list of maps
 * matching the Python validate.py JSON output format.
 */
public class CollectingErrorListener extends BaseErrorListener {

    private final String[] sourceLines;
    private final List<Map<String, Object>> errors = new ArrayList<>();

    public CollectingErrorListener(String[] sourceLines) {
        this.sourceLines = sourceLines;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        String sourceLine = "";
        if (line > 0 && line <= sourceLines.length) {
            sourceLine = sourceLines[line - 1].stripTrailing();
        }

        String context = "";
        if (offendingSymbol instanceof Token) {
            context = ((Token) offendingSymbol).getText();
        }

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("line", line);
        error.put("column", charPositionInLine);
        error.put("message", msg);
        error.put("source_line", sourceLine);
        error.put("context", context);
        errors.add(error);
    }

    public List<Map<String, Object>> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
