import java.util.List;
import java.util.Map;

/**
 * Minimal JSON encoder — no external dependencies needed.
 */
public final class Json {

    private Json() {}

    public static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public static String string(String s) {
        if (s == null) return "null";
        return "\"" + escape(s) + "\"";
    }

    @SuppressWarnings("unchecked")
    public static String value(Object v) {
        if (v == null) return "null";
        if (v instanceof String)  return string((String) v);
        if (v instanceof Number)  return v.toString();
        if (v instanceof Boolean) return v.toString();
        if (v instanceof List)    return array((List<?>) v);
        if (v instanceof Map)     return map((Map<String, Object>) v);
        return string(v.toString());
    }

    public static String array(List<?> items) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(value(items.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    public static String map(Map<String, Object> m) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (var entry : m.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append(string(entry.getKey()));
            sb.append(":");
            sb.append(value(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    /** Build a JSON object from key-value pairs: key1, val1, key2, val2, ... */
    public static String object(Object... kvPairs) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < kvPairs.length; i += 2) {
            if (i > 0) sb.append(",");
            sb.append(string((String) kvPairs[i]));
            sb.append(":");
            sb.append(value(kvPairs[i + 1]));
        }
        sb.append("}");
        return sb.toString();
    }
}
