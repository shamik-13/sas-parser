lexer grammar SasLexer;

options { caseInsensitive = true; }

// ─── Comments ──────────────────────────────────────────────────────────────
BLOCK_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

// LINE_COMMENT removed from lexer — handled as parser rule (lineComment)
// because '*' is ambiguous: it's a comment at statement start but a
// multiplication operator inside expressions like count(*)

MACRO_COMMENT
    : '%*' ~[;]* ';' -> channel(HIDDEN)
    ;

// ─── Strings ───────────────────────────────────────────────────────────────
// Strings with optional type suffix (d=date, t=time, dt=datetime, x=hex)
STRING_LITERAL
    : '\'' ( ~['\r\n] | '\'\'' )* '\'' ( 'dt' | [dtx] )?
    | '"'  ( ~["\r\n] | '""'   )* '"'  ( 'dt' | [dtx] )?
    ;

// ─── Macro Keywords (must come before ID) ──────────────────────────────────
PERCENT_MACRO   : '%macro'   ;
PERCENT_MEND    : '%mend'    ;
PERCENT_LET     : '%let'     ;
PERCENT_GLOBAL  : '%global'  ;
PERCENT_LOCAL   : '%local'   ;
PERCENT_IF      : '%if'      ;
PERCENT_THEN    : '%then'    ;
PERCENT_ELSE    : '%else'    ;
PERCENT_DO      : '%do'      ;
PERCENT_END     : '%end'     ;
PERCENT_PUT     : '%put'     ;
PERCENT_RETURN  : '%return'  ;
PERCENT_GOTO    : '%goto'    ;
PERCENT_LABEL   : '%label'   ;
PERCENT_INCLUDE : '%include' ;
PERCENT_INC     : '%inc'     ;
PERCENT_EVAL    : '%eval'    ;
PERCENT_SYSEVALF: '%sysevalf';
PERCENT_SYSFUNC : '%sysfunc' ;
PERCENT_QSYSFUNC: '%qsysfunc' ;
// Macro quoting functions — open paren pushes into STR_MODE
// so semicolons/parens inside are harmless to the outer grammar
PERCENT_STR_OPEN      : '%str'      LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_NRSTR_OPEN    : '%nrstr'    LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_BQUOTE_OPEN   : '%bquote'   LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_NRBQUOTE_OPEN : '%nrbquote' LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_QUOTE_OPEN    : '%quote'    LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_NRQUOTE_OPEN  : '%nrquote'  LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_SUPERQ_OPEN  : '%superq'  LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_UNQUOTE_OPEN : '%unquote' LWSP? '(' -> pushMode(STR_MODE) ;
PERCENT_QSCAN   : '%qscan'  ;
PERCENT_SCAN    : '%scan'    ;
PERCENT_SUBSTR  : '%substr'  ;
PERCENT_QSUBSTR : '%qsubstr' ;
PERCENT_UPCASE  : '%upcase'  ;
PERCENT_QUPCASE : '%qupcase' ;
PERCENT_LOWCASE : '%lowcase' ;
PERCENT_QLOWCASE: '%qlowcase';
PERCENT_LEFT    : '%left'    ;
PERCENT_TRIM    : '%trim'    ;
PERCENT_QTRIM   : '%qtrim'   ;
PERCENT_INDEX   : '%index'   ;
PERCENT_LENGTH  : '%length'  ;
PERCENT_CMPRES  : '%cmpres'  ;
PERCENT_QCMPRES : '%qcmpres' ;
PERCENT_VERIFY  : '%verify'  ;
PERCENT_SYMEXIST: '%symexist';
PERCENT_SYMGLOBL: '%symglobl';
PERCENT_SYMLOCAL: '%symlocal';
PERCENT_ABORT   : '%abort'   ;
PERCENT_COPY    : '%copy'    ;
PERCENT_DISPLAY : '%display' ;
PERCENT_INPUT   : '%input'   ;
PERCENT_LIST    : '%list'    ;
PERCENT_RUN     : '%run'     ;
PERCENT_SYSEXEC : '%sysexec' ;
PERCENT_SYSCALL : '%syscall' ;
PERCENT_WINDOW  : '%window'  ;
PERCENT_WHILE   : '%while'   ;
PERCENT_UNTIL   : '%until'   ;
PERCENT_TO      : '%to'      ;
PERCENT_BY      : '%by'      ;

// ─── Macro call: %identifier ───────────────────────────────────────────────
// Must come after all specific %keyword tokens
MACRO_CALL_NAME
    : '%' [a-z_] [a-z0-9_]*
    ;

// ─── Macro variable reference ──────────────────────────────────────────────
MACRO_VAR
    : '&' [a-z_] [a-z0-9_]* '.'?
    ;

// Double ampersand (indirect reference)
AMP_AMP
    : '&&'
    ;

// Bare ampersand (used as AND operator in some SAS contexts: if x & y)
AMP
    : '&'
    ;

// ─── SAS Keywords ──────────────────────────────────────────────────────────
KW_DATA       : 'data'       ;
KW_SET        : 'set'        ;
KW_MERGE      : 'merge'      ;
KW_UPDATE     : 'update'     ;
KW_MODIFY     : 'modify'     ;
KW_BY         : 'by'         ;
KW_RUN        : 'run'        ;
KW_QUIT       : 'quit'       ;
KW_PROC       : 'proc'       ;
KW_LIBNAME    : 'libname'    ;
KW_FILENAME   : 'filename'   ;
KW_OPTIONS    : 'options'    ;
KW_ODS        : 'ods'        ;
KW_TITLE      : 'title'      ;
KW_FOOTNOTE   : 'footnote'   ;
KW_IF         : 'if'         ;
KW_THEN       : 'then'       ;
KW_ELSE       : 'else'       ;
KW_DO         : 'do'         ;
KW_END        : 'end'        ;
KW_OUTPUT     : 'output'     ;
KW_INPUT      : 'input'      ;
KW_INFILE     : 'infile'     ;
KW_FILE       : 'file'       ;
KW_PUT        : 'put'        ;
KW_LENGTH     : 'length'     ;
KW_FORMAT     : 'format'     ;
KW_INFORMAT   : 'informat'   ;
KW_LABEL      : 'label'      ;
KW_ATTRIB     : 'attrib'     ;
KW_RETAIN     : 'retain'     ;
KW_ARRAY      : 'array'      ;
KW_DROP       : 'drop'       ;
KW_KEEP       : 'keep'       ;
KW_WHERE      : 'where'      ;
KW_DELETE     : 'delete'     ;
KW_CALL       : 'call'       ;
KW_RETURN     : 'return'     ;
KW_CARDS      : 'cards'      ;
KW_CARDS4     : 'cards4'     ;
KW_DATALINES  : 'datalines'  ;
KW_DATALINES4 : 'datalines4' ;
KW_PARMCARDS  : 'parmcards'  ;
KW_PARMCARDS4 : 'parmcards4' ;
KW_ABORT      : 'abort'      ;
KW_STOP       : 'stop'       ;
KW_ERROR      : 'error'      ;
KW_LIST       : 'list'       ;
KW_GOTO       : 'goto'       ;
KW_LINK       : 'link'       ;
KW_SELECT     : 'select'     ;
KW_WHEN       : 'when'       ;
KW_OTHERWISE  : 'otherwise'  ;
KW_LEAVE      : 'leave'      ;
KW_CONTINUE   : 'continue'   ;
KW_TO         : 'to'         ;
KW_UNTIL      : 'until'      ;
KW_WHILE      : 'while'      ;
KW_NOT        : 'not'        ;
KW_AND        : 'and'        ;
KW_OR         : 'or'         ;
KW_IN         : 'in'         ;
KW_EQ         : 'eq'         ;
KW_NE         : 'ne'         ;
KW_LT         : 'lt'         ;
KW_LE         : 'le'         ;
KW_GT         : 'gt'         ;
KW_GE         : 'ge'         ;
KW_OF         : 'of'         ;
KW_AS         : 'as'         ;
KW_FROM       : 'from'       ;
KW_BETWEEN    : 'between'    ;
KW_LIKE       : 'like'       ;
KW_NULL       : 'null'       ;
KW_MISSING    : 'missing'    ;
KW_CLASS      : 'class'      ;
KW_VAR        : 'var'        ;
KW_TABLE      : 'table'      ;
KW_TABLES     : 'tables'     ;
KW_MODEL      : 'model'      ;
KW_WEIGHT     : 'weight'     ;
KW_FREQ       : 'freq'       ;
KW_NOPRINT    : 'noprint'    ;
KW_OUT        : 'out'        ;
KW_REPLACE    : 'replace'    ;
KW_RENAME     : 'rename'     ;
KW_DECLARE    : 'declare'    ;
KW_COMPRESS   : 'compress'   ;
KW_DM         : 'dm'         ;
KW_SYSTASK    : 'systask'    ;
KW_ENDSAS     : 'endsas'     ;
KW_GLOBAL     : 'global'     ;
KW_LOCAL      : 'local'      ;
KW_OUTER      : 'outer'      ;
KW_INNER      : 'inner'      ;
KW_LEFT       : 'left'       ;
KW_RIGHT      : 'right'      ;
KW_FULL       : 'full'       ;
KW_JOIN       : 'join'       ;
KW_ON         : 'on'         ;
KW_UNION      : 'union'      ;
KW_ALL        : 'all'        ;
KW_EXCEPT     : 'except'     ;
KW_INTERSECT  : 'intersect'  ;
KW_INTO       : 'into'       ;
KW_CREATE     : 'create'     ;
KW_INSERT     : 'insert'     ;
KW_VALUES     : 'values'     ;
KW_ALTER      : 'alter'      ;
KW_ADD        : 'add'        ;
KW_COLUMN     : 'column'     ;
KW_CASE       : 'case'       ;
KW_GROUP      : 'group'      ;
KW_HAVING     : 'having'     ;
KW_ORDER      : 'order'      ;
KW_DISTINCT   : 'distinct'   ;
KW_EXISTS     : 'exists'     ;
KW_DESCRIBE   : 'describe'   ;
KW_CALCULATED : 'calculated' ;
KW_VIEW       : 'view'       ;
KW_NOOBS      : 'noobs'      ;
KW_NWAY       : 'nway'       ;
KW_DUPOUT     : 'dupout'     ;
KW_NODUPKEY   : 'nodupkey'   ;
KW_NODUPRECS  : 'noduprecs'  ;
KW_FORCE      : 'force'      ;
KW_OVERWRITE  : 'overwrite'  ;
KW_CONTENTS   : 'contents'   ;
KW_DESCENDING : 'descending' ;
KW_ALSO       : 'also'       ;
KW_PUTLOG     : 'putlog'     ;
KW_LOCK       : 'lock'       ;
KW_UNLOCK     : 'unlock'     ;
KW_CATNAME    : 'catname'    ;

// ─── Numbered Title/Footnote/Graph Keywords ─────────────────────────
// Must come BEFORE ID so title1, footnote2, symbol3 etc. don't match ID
KW_TITLE_N    : 'title' [0-9]+    ;
KW_FOOTNOTE_N : 'footnote' [0-9]+ ;
KW_GOPTIONS   : 'goptions' | 'goption' ;
KW_SYMBOL_N   : 'symbol' [0-9]*   ;
KW_AXIS_N     : 'axis' [0-9]*     ;
KW_PATTERN_N  : 'pattern' [0-9]*  ;
KW_LEGEND_N   : 'legend' [0-9]*   ;

// ─── SQL-specific Keywords ────────────────────────────────────────────
KW_SQL           : 'sql'           ;
KW_ASC           : 'asc'           ;
KW_DESC          : 'desc'          ;
KW_CROSS         : 'cross'         ;
KW_NATURAL       : 'natural'       ;
KW_CORR          : 'corr'          ;
KW_CORRESPONDING : 'corresponding' ;
KW_IS            : 'is'            ;
KW_CONTAINS      : 'contains'      ;
KW_CONNECTION    : 'connection'     ;
KW_CONNECT       : 'connect'       ;
KW_DISCONNECT    : 'disconnect'    ;
KW_EXECUTE       : 'execute'       ;
KW_USING         : 'using'         ;
KW_RESET         : 'reset'         ;
KW_VALIDATE      : 'validate'      ;
KW_INDEX         : 'index'         ;
KW_PRIMARY       : 'primary'       ;
KW_KEY           : 'key'           ;
KW_FOREIGN       : 'foreign'       ;
KW_REFERENCES    : 'references'    ;
KW_UNIQUE        : 'unique'        ;
KW_CONSTRAINT    : 'constraint'    ;
KW_CHECK         : 'check'         ;
KW_CASCADE       : 'cascade'       ;
KW_RESTRICT      : 'restrict'      ;
KW_SEPARATED     : 'separated'     ;
KW_TRIMMED       : 'trimmed'       ;
KW_NOTRIM        : 'notrim'        ;
KW_NEW           : '_new_'         ;

// ─── Operators & Punctuation ───────────────────────────────────────────────
DOLLAR        : '$'   ;
LBRACE        : '{'   ;
RBRACE        : '}'   ;
SEMI          : ';'   ;
COMMA         : ','   ;
LPAREN        : '('   ;
RPAREN        : ')'   ;
EQUALS        : '='   ;
PLUS          : '+'   ;
MINUS         : '-'   ;
STAR          : '*'   ;
SLASH         : '/'   ;
DOT           : '.'   ;
COLON         : ':'   ;
HASH          : '#'   ;
AT            : '@'   ;
EXCL          : '!'   ;
TILDE         : '~'   ;
CARET         : '^'   ;
PIPE          : '|'   ;
QUESTION      : '?'   ;
BACKSLASH     : '\\'  ;
LT_OP         : '<'   ;
GT_OP         : '>'   ;
LE_OP         : '<='  ;
GE_OP         : '>='  ;
NE_OP         : '<>'  ;
NE_HASH       : '^='  ;
NE_TILDE      : '~='  ;
CONCAT_OP     : '||'  ;
CONCAT_EXCL   : '!!'  ;
POWER_OP      : '**'  ;

// ─── Numeric Literals ──────────────────────────────────────────────────────
FLOAT_LITERAL
    : DIGIT+ '.' DIGIT* ( [e] [+-]? DIGIT+ )?
    | DIGIT* '.' DIGIT+ ( [e] [+-]? DIGIT+ )?
    | DIGIT+ [e] [+-]? DIGIT+
    ;

INT_LITERAL
    : DIGIT+
    ;

HEX_LITERAL
    : DIGIT [0-9a-f]* 'x'
    ;

// ─── Identifiers ──────────────────────────────────────────────────────────
ID  : [a-z_] [a-z0-9_]*
    ;

// ─── Whitespace ────────────────────────────────────────────────────────────
WS  : [ \t\r\n]+ -> skip
    ;

// ─── Catch-all ─────────────────────────────────────────────────────────────
ANY_CHAR
    : .
    ;

fragment DIGIT : [0-9] ;
fragment LWSP  : [ \t\r\n]+ ;  // optional whitespace before '(' in quoting functions

// ═══════════════════════════════════════════════════════════════════════════
// Lexer Mode for macro quoting functions: %str(), %nrstr(), %bquote(), etc.
// Semicolons inside become STR_SEMI (harmless to outer grammar).
// Parentheses are tracked for nesting via pushMode/popMode.
// Macro variable references (&var) are preserved as distinct tokens.
// ═══════════════════════════════════════════════════════════════════════════

mode STR_MODE;

// Macro variable references — preserved for structural info
STR_MACRO_VAR   : '&' [a-z_] [a-z0-9_]* '.'? ;
STR_AMP_AMP     : '&&' ;
STR_AMP         : '&' ;  // bare ampersand (e.g., before %% in &%%)

// Macro-escaped characters: %( %) %' %" %% %semicolon
STR_ESCAPED     : '%(' | '%)' | '%' '\'' | '%' '"' | '%%' | '%;' ;

// Nested quoting function calls — push another STR_MODE level
STR_PERCENT_STR_OPEN      : '%str'      LWSP? '(' -> pushMode(STR_MODE) ;
STR_PERCENT_NRSTR_OPEN    : '%nrstr'    LWSP? '(' -> pushMode(STR_MODE) ;
STR_PERCENT_BQUOTE_OPEN   : '%bquote'   LWSP? '(' -> pushMode(STR_MODE) ;
STR_PERCENT_NRBQUOTE_OPEN : '%nrbquote' LWSP? '(' -> pushMode(STR_MODE) ;
STR_PERCENT_QUOTE_OPEN    : '%quote'    LWSP? '(' -> pushMode(STR_MODE) ;
STR_PERCENT_NRQUOTE_OPEN  : '%nrquote'  LWSP? '(' -> pushMode(STR_MODE) ;
STR_PERCENT_SUPERQ_OPEN   : '%superq'   LWSP? '(' -> pushMode(STR_MODE) ;
STR_PERCENT_UNQUOTE_OPEN  : '%unquote'  LWSP? '(' -> pushMode(STR_MODE) ;

// Other macro calls inside quoting: %sysfunc(...), %scan(...), etc.
STR_MACRO_CALL  : '%' [a-z_] [a-z0-9_]* ;

// Semicolons — the key fix: these are NOT SEMI tokens
STR_SEMI        : ';' ;

// Nested real parens: push another STR_MODE level
STR_LPAREN      : '(' -> pushMode(STR_MODE) ;

// Closing paren: pop one level — when last level pops, back to default mode
STR_RPAREN      : ')' -> popMode ;

// String literals inside quoting
STR_STRING      : '\'' ( ~['\r\n] | '\'\'' )* '\''
                | '"'  ( ~["\r\n] | '""'   )* '"'
                ;

// Whitespace — preserved (not skipped) so content is faithful
STR_WS          : [ \t\r\n]+ ;

// Comma
STR_COMMA       : ',' ;

// Everything else — text content
STR_TEXT        : ~[();&%,' \t\r\n"]+ ;

// Note: cards4/datalines4/parmcards4 raw data blocks (terminated by ;;;;)
// are handled at the parser level. The parser collects tokens via cardsData
// (up to the first SEMI) and the remaining ;;; are parsed as emptyStatements.

// ═══════════════════════════════════════════════════════════════════════════
// Lexer Mode for DATALINES/CARDS/PARMCARDS raw data blocks.
// Entered programmatically by SasCustomLexer when it detects a standalone
// cards/datalines statement (e.g., KW_DATALINES SEMI at a statement boundary).
// Data lines are skipped until the terminator: a line containing only ";".
// ═══════════════════════════════════════════════════════════════════════════

mode RAWDATA;

// Terminator line: semicolon alone on a line (optional whitespace around it)
RAWDATA_END  : [ \t]* ';' [ \t]* ('\r'? '\n') -> type(SEMI), popMode ;

// Non-terminator line: anything up to and including newline
RAWDATA_LINE : ~[\r\n]* ('\r'? '\n') -> skip ;

// Catch-all for EOF without trailing newline (malformed input)
RAWDATA_CHAR : . -> skip ;

// ═══════════════════════════════════════════════════════════════════════════
// Lexer Mode for DATALINES4/CARDS4/PARMCARDS4 raw data blocks.
// Same as RAWDATA but terminated by ";;;;" instead of lone ";".
// ═══════════════════════════════════════════════════════════════════════════

mode RAWDATA4;

RAWDATA4_END     : ';;;;' -> type(SEMI), popMode ;
RAWDATA4_CONTENT : ~[;]+ -> skip ;
RAWDATA4_SEMI    : ';' -> skip ;
