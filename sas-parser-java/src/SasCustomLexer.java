import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import java.util.Set;

/**
 * Custom SAS lexer that handles DATALINES/CARDS raw data blocks.
 *
 * Overrides nextToken() to track the last two default-channel tokens.
 * When a SEMI is emitted and the previous token was a cards/datalines keyword
 * preceded by another SEMI (or start of input), pushes the appropriate
 * RAWDATA or RAWDATA4 lexer mode so raw data lines are skipped until the
 * terminator (; on its own line, or ;;;; for cards4/datalines4/parmcards4).
 */
public class SasCustomLexer extends SasLexer {

    private static final Set<Integer> RAWDATA_KW = Set.of(
        SasLexer.KW_CARDS, SasLexer.KW_DATALINES, SasLexer.KW_PARMCARDS
    );
    private static final Set<Integer> RAWDATA4_KW = Set.of(
        SasLexer.KW_CARDS4, SasLexer.KW_DATALINES4, SasLexer.KW_PARMCARDS4
    );

    private int penultimateType = -1;
    private int lastType = -1;

    public SasCustomLexer(CharStream input) {
        super(input);
    }

    @Override
    public Token nextToken() {
        Token token = super.nextToken();

        if (token.getChannel() == Token.DEFAULT_CHANNEL) {
            if (token.getType() == SasLexer.SEMI) {
                if (RAWDATA_KW.contains(lastType)) {
                    if (penultimateType == SasLexer.SEMI || penultimateType == -1) {
                        pushMode(SasLexer.RAWDATA);
                    }
                } else if (RAWDATA4_KW.contains(lastType)) {
                    if (penultimateType == SasLexer.SEMI || penultimateType == -1) {
                        pushMode(SasLexer.RAWDATA4);
                    }
                }
            }

            penultimateType = lastType;
            lastType = token.getType();
        }

        return token;
    }
}
