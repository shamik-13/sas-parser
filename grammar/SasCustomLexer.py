"""Custom SAS lexer that handles DATALINES/CARDS raw data blocks.

Overrides nextToken() to track the last two default-channel tokens.
When a SEMI is emitted and the previous token was a cards/datalines keyword
preceded by another SEMI (or start of input), pushes the appropriate
RAWDATA or RAWDATA4 lexer mode so raw data lines are skipped until the
terminator (; on its own line, or ;;;; for cards4/datalines4/parmcards4).
"""

from SasLexer import SasLexer


class SasCustomLexer(SasLexer):

    # Sets of token types — populated lazily on first instantiation
    _RAWDATA_KW = None    # cards, datalines, parmcards
    _RAWDATA4_KW = None   # cards4, datalines4, parmcards4

    def __init__(self, input_stream):
        super().__init__(input_stream)
        self._penultimate_type = -1   # token before _last_type
        self._last_type = -1          # most recent default-channel token type

        # Resolve token type constants once
        if SasCustomLexer._RAWDATA_KW is None:
            SasCustomLexer._RAWDATA_KW = frozenset({
                SasLexer.KW_CARDS,
                SasLexer.KW_DATALINES,
                SasLexer.KW_PARMCARDS,
            })
            SasCustomLexer._RAWDATA4_KW = frozenset({
                SasLexer.KW_CARDS4,
                SasLexer.KW_DATALINES4,
                SasLexer.KW_PARMCARDS4,
            })

    def nextToken(self):
        token = super().nextToken()

        # Only track default-channel tokens (ignore hidden/skipped)
        if token.channel == self.DEFAULT_TOKEN_CHANNEL:
            if token.type == self.SEMI:
                # Check: is this the SEMI that closes a standalone cards/datalines stmt?
                # Condition: previous token is a cards keyword, and the one before that
                # is SEMI (statement boundary) or -1 (start of input).
                if self._last_type in self._RAWDATA_KW:
                    if self._penultimate_type == self.SEMI or self._penultimate_type == -1:
                        self.pushMode(self.RAWDATA)
                elif self._last_type in self._RAWDATA4_KW:
                    if self._penultimate_type == self.SEMI or self._penultimate_type == -1:
                        self.pushMode(self.RAWDATA4)

            self._penultimate_type = self._last_type
            self._last_type = token.type

        return token
