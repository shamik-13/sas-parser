parser grammar SasParser;

options { tokenVocab = SasLexer; }

// ═══════════════════════════════════════════════════════════════════════════
// Top-Level
// ═══════════════════════════════════════════════════════════════════════════

program
    : programStatement* trailingText? EOF
    ;

programStatement
    : macroDefinition
    | macroStatement
    | dataStep
    | procStep
    | globalStatement
    | cardsStatement
    | lineComment
    | emptyStatement
    | unknownStatement
    ;

emptyStatement
    : SEMI
    ;

// Catch-all for unrecognized statements (e.g., FCMP function bodies, FORMAT value stmts)
unknownStatement
    : ~(SEMI)+ SEMI
    ;

// Trailing non-SAS content (e.g., metadata files without semicolons)
trailingText
    : ~(SEMI)+
    ;

// Line comment: * text; — handled in parser because '*' is ambiguous in the lexer
// (multiplication vs. comment start). Only valid at statement boundaries.
lineComment
    : STAR ~(SEMI)* SEMI
    ;

// ═══════════════════════════════════════════════════════════════════════════
// Macro Layer
// ═══════════════════════════════════════════════════════════════════════════

macroDefinition
    : PERCENT_MACRO identifier (LPAREN macroParamList? RPAREN)? (SLASH macroDefOption)* SEMI
      macroBody*
      PERCENT_MEND identifier? SEMI
    ;

macroDefOption
    : macroDefOptionToken+
    ;

macroDefOptionToken
    : ~(SEMI | SLASH)
    ;

macroParamList
    : macroParam (COMMA macroParam)* COMMA?
    ;

macroParam
    : identifier (EQUALS macroParamDefault?)?
    ;

macroParamDefault
    : macroParamDefaultToken+
    ;

macroParamDefaultToken
    : nestedParens
    | ~(COMMA | RPAREN)
    ;

macroBody
    : macroDefinition
    | macroStatement
    | dataStep
    | procStep
    | globalStatement
    | lineComment
    | emptyStatement
    | macroGenericText
    ;

macroGenericText
    : macroGenericToken+ SEMI?
    ;

macroGenericToken
    : ~(SEMI | PERCENT_MACRO | PERCENT_MEND | PERCENT_IF | PERCENT_DO | PERCENT_END
       | PERCENT_LET | PERCENT_PUT | PERCENT_RETURN | PERCENT_GOTO | PERCENT_GLOBAL
       | PERCENT_LOCAL | PERCENT_INCLUDE | PERCENT_INC | PERCENT_ABORT
       | KW_DATA | KW_PROC | KW_LIBNAME | KW_FILENAME | KW_OPTIONS | KW_ODS
       | KW_TITLE | KW_FOOTNOTE | KW_DM | KW_SYSTASK | KW_ENDSAS | KW_LOCK)
    ;

// ─── Macro Statements ──────────────────────────────────────────────────────

macroStatement
    : macroLetStmt
    | macroIfStmt
    | macroDoBlock
    | macroPutStmt
    | macroReturnStmt
    | macroGotoStmt
    | macroLabelStmt
    | macroGlobalStmt
    | macroLocalStmt
    | macroIncludeStmt
    | macroAbortStmt
    | macroCallStmt
    ;

macroLetStmt
    : PERCENT_LET macroVarRef EQUALS macroExprText? SEMI
    ;

macroVarRef
    : (identifier | MACRO_VAR | AMP_AMP | INT_LITERAL | DOT)+
    ;

macroGlobalStmt
    : PERCENT_GLOBAL macroVarNameList SEMI
    ;

macroLocalStmt
    : PERCENT_LOCAL macroVarNameList SEMI
    ;

macroVarNameList
    : (identifier | MACRO_VAR)+
    ;

macroIfStmt
    : PERCENT_IF macroCondition PERCENT_THEN macroIfAction
      (PERCENT_ELSE macroIfAction)?
    ;

macroCondition
    : macroCondToken+
    ;

macroCondToken
    : ~(PERCENT_THEN)
    ;

macroIfAction
    : macroDoBlock
    | macroStatement
    | macroActionText SEMI
    ;

macroActionText
    : macroActionToken+
    ;

macroActionToken
    : ~(SEMI | PERCENT_ELSE | PERCENT_IF | PERCENT_DO | PERCENT_END
       | PERCENT_LET | PERCENT_PUT | PERCENT_RETURN | PERCENT_GOTO
       | PERCENT_MACRO | PERCENT_MEND | PERCENT_INCLUDE | PERCENT_INC)
    ;

macroDoBlock
    : PERCENT_DO macroDoSpec? SEMI
      macroDoBody*
      PERCENT_END SEMI
    ;

macroDoSpec
    : identifier EQUALS expression PERCENT_TO expression (PERCENT_BY expression)?
    | PERCENT_WHILE macroCondWhile
    | PERCENT_UNTIL macroCondWhile
    ;

macroCondWhile
    : LPAREN macroCondWhileToken+ RPAREN
    ;

macroCondWhileToken
    : ~(RPAREN | LPAREN)
    | LPAREN macroCondWhileToken* RPAREN
    ;

macroDoBody
    : macroDefinition
    | macroStatement
    | dataStep
    | procStep
    | globalStatement
    | lineComment
    | emptyStatement
    | macroGenericText
    ;

macroPutStmt
    : PERCENT_PUT macroPutText? SEMI
    ;

macroPutText
    : ~(SEMI)+
    ;

macroReturnStmt
    : PERCENT_RETURN SEMI
    ;

macroGotoStmt
    : PERCENT_GOTO (identifier | MACRO_CALL_NAME | MACRO_VAR) SEMI
    ;

macroLabelStmt
    : PERCENT_LABEL (identifier | MACRO_CALL_NAME | MACRO_VAR) COLON
    | MACRO_CALL_NAME COLON     // Label definition: %name:
    ;

macroIncludeStmt
    : (PERCENT_INCLUDE | PERCENT_INC) macroIncludeTarget SEMI
    ;

macroIncludeTarget
    : ~(SEMI)+
    ;

macroAbortStmt
    : PERCENT_ABORT ~(SEMI)* SEMI
    ;

macroCallStmt
    : MACRO_CALL_NAME (LPAREN macroCallArgs? RPAREN)? SEMI?
    ;

macroCallArgs
    : macroCallArg (COMMA macroCallArg)*
    ;

macroCallArg
    : macroCallArgToken*
    ;

macroCallArgToken
    : nestedParens
    | ~(COMMA | RPAREN)
    ;

nestedParens
    : LPAREN (nestedParens | ~RPAREN)* RPAREN
    ;

// ─── Macro Expressions ─────────────────────────────────────────────────────

macroExprText
    : macroExprToken+
    ;

macroExprToken
    : ~(SEMI)
    ;

macroFuncCall
    : macroQuoteCall
    | (PERCENT_EVAL | PERCENT_SYSEVALF | PERCENT_SYSFUNC | PERCENT_QSYSFUNC
      | PERCENT_QSCAN | PERCENT_SCAN | PERCENT_SUBSTR | PERCENT_QSUBSTR
      | PERCENT_UPCASE | PERCENT_QUPCASE | PERCENT_LOWCASE | PERCENT_QLOWCASE
      | PERCENT_LEFT | PERCENT_TRIM | PERCENT_QTRIM | PERCENT_INDEX
      | PERCENT_LENGTH | PERCENT_CMPRES | PERCENT_QCMPRES | PERCENT_VERIFY
      | PERCENT_SYMEXIST | PERCENT_SYMGLOBL | PERCENT_SYMLOCAL
      | MACRO_CALL_NAME)
      LPAREN macroCallArgs? RPAREN
    ;

// ─── Macro Quoting Function Calls ────────────────────────────────────────
// These use lexer modes — the opening token includes '(' and pushes STR_MODE.
// Content inside is re-tokenized with STR_* tokens so semicolons are harmless.
macroQuoteCall
    : (PERCENT_STR_OPEN | PERCENT_NRSTR_OPEN | PERCENT_BQUOTE_OPEN
      | PERCENT_NRBQUOTE_OPEN | PERCENT_QUOTE_OPEN | PERCENT_NRQUOTE_OPEN
      | PERCENT_SUPERQ_OPEN | PERCENT_UNQUOTE_OPEN
      | STR_PERCENT_STR_OPEN | STR_PERCENT_NRSTR_OPEN | STR_PERCENT_BQUOTE_OPEN
      | STR_PERCENT_NRBQUOTE_OPEN | STR_PERCENT_QUOTE_OPEN | STR_PERCENT_NRQUOTE_OPEN
      | STR_PERCENT_SUPERQ_OPEN | STR_PERCENT_UNQUOTE_OPEN)
      strContent*
      STR_RPAREN
    ;

strContent
    : STR_TEXT | STR_SEMI | STR_WS | STR_ESCAPED | STR_COMMA
    | STR_MACRO_VAR | STR_AMP_AMP | STR_AMP | STR_STRING
    | STR_MACRO_CALL
    | STR_LPAREN strContent* STR_RPAREN   // nested real parentheses
    | macroQuoteCall                       // nested %str(%str(...))
    ;

// ═══════════════════════════════════════════════════════════════════════════
// Data Step
// ═══════════════════════════════════════════════════════════════════════════

dataStep
    : KW_DATA dataStepHeaderItem* SEMI
      dataStepStatement*
      (KW_RUN SEMI)?
    ;

dataStepHeaderItem
    : datasetName
    | SLASH dataStepOption
    | macroIfStmt
    | macroDoBlock
    | MACRO_CALL_NAME (LPAREN macroCallArgs? RPAREN)?
    ;

datasetName
    : qualifiedName datasetOptions?
    | STRING_LITERAL datasetOptions?
    | macroDatasetRef datasetOptions?
    ;

// Macro-based dataset names: &lib..&ds, &pkds.1, &&_webin_name&i
macroDatasetRef
    : (MACRO_VAR | AMP_AMP) (identifier | INT_LITERAL | MACRO_VAR | AMP_AMP)*
      (DOT (identifier | MACRO_VAR | AMP_AMP) (identifier | INT_LITERAL | MACRO_VAR | AMP_AMP)*)?
    ;

qualifiedName
    : identifier (DOT identifier?)?
    ;

datasetOptions
    : nestedParens
    ;

dataStepOption
    : identifier (EQUALS (qualifiedName | MACRO_VAR | AMP_AMP))?
    ;

dataStepStatement
    : stmtLabel
    | setStatement
    | mergeStatement
    | byStatement
    | ifThenElseStmt
    | doBlock
    | selectBlock
    | cardsStatement
    | assignmentOrCallStmt
    | lengthStatement
    | formatStatement
    | informatStatement
    | labelStatement
    | attribStatement
    | retainStatement
    | arrayStatement
    | keepStatement
    | dropStatement
    | whereStatement
    | outputStatement
    | deleteStatement
    | putStatement
    | inputStatement
    | infileStatement
    | fileStatement
    | callStatement
    | returnStatement
    | abortStatement
    | stopStatement
    | errorStatement
    | gotoStatement
    | linkStatement
    | listStatement
    | putlogStatement
    | macroDefinition
    | macroStatement
    | lineComment
    | emptyStatement
    ;

stmtLabel
    : identifier COLON
    ;

setStatement
    : KW_SET datasetName* setOptions? (SLASH identifier)? SEMI
    ;

setOptions
    : (identifier EQUALS expression)+
    ;

mergeStatement
    : KW_MERGE datasetName+ SEMI
    ;

byStatement
    : KW_BY byVariable+ SEMI
    ;

byVariable
    : (identifier | KW_DESCENDING)? identifier
    ;

ifThenElseStmt
    : KW_IF expression KW_THEN doBlock (KW_ELSE (doBlock | actionStatement))?
    | KW_IF expression KW_THEN actionStatement (KW_ELSE (doBlock | actionStatement))?
    | KW_IF expression SEMI   // subsetting IF (filter, no THEN)
    ;

actionStatement
    : assignmentOrCallStmt
    | outputStatement
    | deleteStatement
    | returnStatement
    | callStatement
    | gotoStatement
    | linkStatement
    | putStatement
    | putlogStatement
    | leaveStatement
    | continueStatement
    | stopStatement
    | abortStatement
    | errorStatement
    | macroStatement
    | doBlock
    | selectBlock
    | ifThenElseStmt
    ;

doBlock
    : KW_DO doSpec? SEMI
      dataStepStatement*
      KW_END SEMI
    ;

doSpec
    : identifier EQUALS expression KW_TO expression (KW_BY expression)?
    | identifier EQUALS expression (COMMA expression)+   // DO over value list
    | KW_WHILE LPAREN expression RPAREN
    | KW_UNTIL LPAREN expression RPAREN
    ;

selectBlock
    : KW_SELECT (LPAREN expression RPAREN)? SEMI
      whenClause+
      otherwiseClause?
      KW_END SEMI
    ;

whenClause
    : KW_WHEN LPAREN expressionList RPAREN actionStatement
    ;

otherwiseClause
    : KW_OTHERWISE actionStatement?
    ;

assignmentOrCallStmt
    : expression SEMI
    ;

lengthStatement
    : KW_LENGTH ~(SEMI)* SEMI
    ;

// DOLLAR is defined in the lexer grammar

formatStatement
    : KW_FORMAT ~(SEMI)* SEMI
    ;

informatStatement
    : KW_INFORMAT ~(SEMI)* SEMI
    ;

labelStatement
    : KW_LABEL labelItem+ SEMI
    ;

labelItem
    : identifier EQUALS STRING_LITERAL
    ;

attribStatement
    : KW_ATTRIB attribItem+ SEMI
    ;

attribItem
    : identifier+ attribSpec*
    ;

attribSpec
    : KW_LENGTH EQUALS DOLLAR? numericLiteral
    | KW_FORMAT EQUALS DOLLAR? identifier? numericLiteral? DOT numericLiteral?
    | KW_INFORMAT EQUALS DOLLAR? identifier? numericLiteral? DOT numericLiteral?
    | KW_LABEL EQUALS STRING_LITERAL
    ;

retainStatement
    : KW_RETAIN retainItem* SEMI
    ;

retainItem
    : identifier+ expression?
    ;

arrayStatement
    : KW_ARRAY ~(SEMI)* SEMI
    ;

keepStatement
    : KW_KEEP ~(SEMI)* SEMI
    ;

dropStatement
    : KW_DROP ~(SEMI)* SEMI
    ;

whereStatement
    : KW_WHERE KW_ALSO? ~(SEMI)* SEMI
    ;

outputStatement
    : KW_OUTPUT ~(SEMI)* SEMI
    ;

deleteStatement
    : KW_DELETE SEMI
    ;

putStatement
    : KW_PUT ~(SEMI)* SEMI
    ;

putlogStatement
    : KW_PUTLOG ~(SEMI)* SEMI
    ;

inputStatement
    : KW_INPUT ~(SEMI)* SEMI
    ;

infileStatement
    : KW_INFILE expression infileOptions* SEMI
    ;

infileOptions
    : identifier EQUALS expression
    | identifier
    ;

fileStatement
    : KW_FILE expression fileOptions* SEMI
    ;

fileOptions
    : identifier EQUALS expression
    | identifier
    ;

callStatement
    : KW_CALL identifier LPAREN expressionList? RPAREN SEMI
    ;

returnStatement
    : KW_RETURN expression? SEMI
    ;

abortStatement
    : KW_ABORT expression? SEMI
    ;

stopStatement
    : KW_STOP SEMI
    ;

errorStatement
    : KW_ERROR ~(SEMI)* SEMI
    ;

gotoStatement
    : KW_GOTO identifier SEMI
    ;

linkStatement
    : KW_LINK identifier SEMI
    ;

leaveStatement
    : KW_LEAVE SEMI
    ;

continueStatement
    : KW_CONTINUE SEMI
    ;

listStatement
    : KW_LIST SEMI
    ;

cardsStatement
    : (KW_CARDS | KW_CARDS4 | KW_DATALINES | KW_DATALINES4 | KW_PARMCARDS | KW_PARMCARDS4) SEMI
      cardsData
    ;

cardsData
    : ~(SEMI)*  SEMI
    ;

// ═══════════════════════════════════════════════════════════════════════════
// PROC Step
// ═══════════════════════════════════════════════════════════════════════════

procStep
    : procSqlStep
    | KW_PROC identifier procOptionToken* SEMI
      procBody*
      (KW_RUN SEMI | KW_QUIT SEMI)?
    ;

procOptionToken
    : ~(SEMI | LPAREN | RPAREN)
    | nestedParens
    ;

procBody
    : procBodyStatement
    ;

procBodyStatement
    : ~(KW_RUN | KW_QUIT | SEMI)* SEMI
    ;

// ═══════════════════════════════════════════════════════════════════════════
// PROC SQL
// ═══════════════════════════════════════════════════════════════════════════

procSqlStep
    : KW_PROC KW_SQL procSqlOption* SEMI
      sqlStatement*
      (KW_QUIT SEMI | KW_RUN SEMI)?
    ;

procSqlOption
    : identifier (EQUALS expression)?
    | MACRO_VAR
    | MACRO_CALL_NAME (LPAREN macroCallArgs? RPAREN)?
    ;

sqlStatement
    : sqlSelectStmt
    | sqlCreateStmt
    | sqlInsertStmt
    | sqlUpdateStmt
    | sqlDeleteStmt
    | sqlAlterStmt
    | sqlDropStmt
    | sqlDescribeStmt
    | sqlResetStmt
    | sqlValidateStmt
    | sqlConnectStmt
    | sqlDisconnectStmt
    | sqlExecuteStmt
    | macroStatement
    | lineComment
    | emptyStatement
    | sqlGenericStmt
    ;

sqlGenericStmt
    : ~(KW_QUIT | KW_RUN | KW_DATA | KW_PROC | KW_LIBNAME | KW_FILENAME
      | KW_OPTIONS | KW_ODS | KW_TITLE | KW_FOOTNOTE | KW_ENDSAS | SEMI)+ SEMI
    ;

// ─── SELECT ──────────────────────────────────────────────────────────────

sqlSelectStmt
    : sqlQueryExpression SEMI
    ;

sqlQueryExpression
    : sqlQueryTerm ((KW_UNION | KW_EXCEPT | KW_INTERSECT | KW_OUTER KW_UNION)
                    (KW_ALL | KW_CORR | KW_CORRESPONDING)* sqlQueryTerm)*
    ;

sqlQueryTerm
    : KW_SELECT KW_DISTINCT? sqlSelectList
      sqlIntoClause?
      sqlFromClause?
      sqlWhereClause?
      sqlGroupByClause?
      sqlHavingClause?
      sqlOrderByClause?
    | LPAREN sqlQueryExpression RPAREN
    ;

sqlSelectList
    : STAR
    | sqlSelectItem (COMMA sqlSelectItem)*
    ;

sqlSelectItem
    : STAR
    | sqlExpression (KW_AS? sqlAlias)? (KW_FORMAT EQUALS expression)? (KW_LABEL EQUALS expression)?
    | macroIfStmt
    | macroDoBlock
    | MACRO_CALL_NAME (LPAREN macroCallArgs? RPAREN)?
    ;

sqlAlias
    : identifier
    | STRING_LITERAL
    ;

// ─── INTO (SAS-specific) ─────────────────────────────────────────────────

sqlIntoClause
    : KW_INTO sqlIntoTarget (COMMA sqlIntoTarget)*
    ;

sqlIntoTarget
    : COLON (identifier | MACRO_VAR) (KW_SEPARATED KW_BY expression)? (KW_TRIMMED | KW_NOTRIM)?
    | MACRO_VAR
    ;

// ─── FROM ────────────────────────────────────────────────────────────────

sqlFromClause
    : KW_FROM sqlTableSource (COMMA sqlTableSource)*
    ;

sqlTableSource
    : sqlTableTerm sqlJoinChain*
    ;

sqlJoinChain
    : sqlJoinType? KW_JOIN sqlTableTerm sqlJoinCondition?
    | COMMA sqlTableTerm
    ;

sqlJoinType
    : KW_INNER
    | KW_LEFT KW_OUTER?
    | KW_RIGHT KW_OUTER?
    | KW_FULL KW_OUTER?
    | KW_CROSS
    | KW_NATURAL (KW_INNER | KW_LEFT KW_OUTER? | KW_RIGHT KW_OUTER? | KW_FULL KW_OUTER?)?
    ;

sqlJoinCondition
    : KW_ON sqlExpression
    | KW_USING LPAREN identifierList RPAREN
    ;

sqlTableTerm
    : LPAREN sqlQueryExpression RPAREN (KW_AS? sqlAlias)?
    | KW_CONNECTION KW_TO identifier (LPAREN sqlPassthroughContent RPAREN)?
    | macroDatasetRef (KW_AS? sqlAlias)?
    | MACRO_CALL_NAME (LPAREN macroCallArgs? RPAREN)? (KW_AS? sqlAlias)?
    | qualifiedName (KW_AS? sqlAlias)?
    ;

// ─── WHERE / GROUP BY / HAVING / ORDER BY ────────────────────────────────

sqlWhereClause
    : KW_WHERE sqlExpression
    ;

sqlGroupByClause
    : KW_GROUP KW_BY sqlGroupByItem (COMMA sqlGroupByItem)*
    ;

sqlGroupByItem
    : sqlExpression
    | macroIfStmt
    | macroDoBlock
    ;

sqlHavingClause
    : KW_HAVING sqlExpression
    ;

sqlOrderByClause
    : KW_ORDER KW_BY sqlOrderByItem (COMMA sqlOrderByItem)*
    ;

sqlOrderByItem
    : sqlExpression (KW_ASC | KW_DESC)?
    ;

// ─── SQL Expression ──────────────────────────────────────────────────────

sqlExpression
    : expression sqlExpressionSuffix*
    ;

sqlExpressionSuffix
    : KW_IS KW_NOT? (KW_NULL | KW_MISSING)
    | KW_NOT? KW_BETWEEN expression KW_AND expression
    | KW_NOT? KW_IN LPAREN (sqlQueryExpression | expressionList) RPAREN
    | KW_NOT? KW_LIKE expression
    | KW_NOT? KW_CONTAINS expression
    | KW_NOT? QUESTION expression
    ;

sqlWhenClause
    : KW_WHEN sqlExpression KW_THEN sqlExpression
    ;

// ─── SQL Table Name (supports macros) ────────────────────────────────────

sqlTableName
    : qualifiedName datasetOptions?
    | macroDatasetRef datasetOptions?
    | MACRO_CALL_NAME (LPAREN macroCallArgs? RPAREN)? datasetOptions?
    ;

// ─── CREATE ──────────────────────────────────────────────────────────────

sqlCreateStmt
    : KW_CREATE KW_TABLE sqlTableName (LPAREN sqlColumnDefList RPAREN)?
      (KW_AS sqlQueryExpression)? SEMI
    | KW_CREATE KW_UNIQUE? KW_INDEX identifier KW_ON sqlTableName
      LPAREN sqlOrderByItem (COMMA sqlOrderByItem)* RPAREN KW_UNIQUE? SEMI
    | KW_CREATE KW_VIEW sqlTableName KW_AS sqlQueryExpression
      (KW_USING identifier)? SEMI
    ;

sqlColumnDefList
    : sqlColumnDef (COMMA sqlColumnDef)*
    ;

sqlColumnDef
    : identifier sqlDataType? sqlColumnConstraint*
    ;

sqlDataType
    : identifier (LPAREN INT_LITERAL (COMMA INT_LITERAL)? RPAREN)?
    ;

sqlColumnConstraint
    : KW_NOT? KW_NULL
    | KW_UNIQUE
    | KW_PRIMARY KW_KEY
    | KW_CHECK LPAREN sqlExpression RPAREN
    | KW_REFERENCES qualifiedName (LPAREN identifier RPAREN)?
    | KW_LABEL EQUALS expression
    | KW_FORMAT EQUALS expression
    | KW_INFORMAT EQUALS expression
    ;

// ─── ALTER ───────────────────────────────────────────────────────────────

sqlAlterStmt
    : KW_ALTER KW_TABLE sqlTableName sqlAlterAction (COMMA sqlAlterAction)* SEMI
    ;

sqlAlterAction
    : KW_ADD KW_COLUMN? sqlColumnDef
    | KW_DROP KW_COLUMN? identifier
    | KW_MODIFY sqlColumnDef
    | KW_ADD KW_CONSTRAINT identifier sqlTableConstraint
    | KW_DROP KW_CONSTRAINT identifier
    | KW_DROP KW_PRIMARY KW_KEY
    | KW_DROP KW_FOREIGN KW_KEY identifier
    ;

sqlTableConstraint
    : KW_PRIMARY KW_KEY LPAREN identifierList RPAREN
    | KW_UNIQUE LPAREN identifierList RPAREN
    | KW_CHECK LPAREN sqlExpression RPAREN
    | KW_FOREIGN KW_KEY LPAREN identifierList RPAREN
      KW_REFERENCES qualifiedName LPAREN identifierList RPAREN
    ;

// ─── DROP ────────────────────────────────────────────────────────────────

sqlDropStmt
    : KW_DROP (KW_TABLE | KW_VIEW | KW_INDEX) sqlTableName (COMMA sqlTableName)* SEMI
    ;

// ─── INSERT ──────────────────────────────────────────────────────────────

sqlInsertStmt
    : KW_INSERT KW_INTO sqlTableName (LPAREN identifierList RPAREN)?
      (KW_VALUES LPAREN expressionList RPAREN (COMMA LPAREN expressionList RPAREN)*
      | KW_SET sqlSetClause (COMMA sqlSetClause)*
      | sqlQueryExpression)
      SEMI
    ;

sqlSetClause
    : identifier EQUALS sqlExpression
    ;

// ─── UPDATE ──────────────────────────────────────────────────────────────

sqlUpdateStmt
    : KW_UPDATE sqlTableName (KW_AS? identifier)?
      KW_SET sqlSetClause (COMMA sqlSetClause)*
      sqlWhereClause?
      SEMI
    ;

// ─── DELETE ──────────────────────────────────────────────────────────────

sqlDeleteStmt
    : KW_DELETE KW_FROM sqlTableName (KW_AS? identifier)?
      sqlWhereClause?
      SEMI
    ;

// ─── DESCRIBE / RESET / VALIDATE ─────────────────────────────────────────

sqlDescribeStmt
    : KW_DESCRIBE (KW_TABLE | KW_VIEW) sqlTableName (COMMA sqlTableName)* SEMI
    | KW_DESCRIBE KW_TABLE KW_CONTENTS KW_OF sqlTableName SEMI
    ;

sqlResetStmt
    : KW_RESET procSqlOption* SEMI
    ;

sqlValidateStmt
    : KW_VALIDATE sqlQueryExpression SEMI
    ;

// ─── Pass-Through (CONNECT / DISCONNECT / EXECUTE) ───────────────────────

sqlConnectStmt
    : KW_CONNECT KW_TO identifier (KW_AS identifier)?
      (LPAREN sqlPassthroughContent RPAREN)? SEMI
    ;

sqlDisconnectStmt
    : KW_DISCONNECT KW_FROM identifier SEMI
    ;

sqlExecuteStmt
    : KW_EXECUTE LPAREN sqlPassthroughContent RPAREN KW_BY identifier SEMI
    ;

sqlPassthroughContent
    : (nestedParens | ~RPAREN)*
    ;

// ═══════════════════════════════════════════════════════════════════════════
// Global Statements
// ═══════════════════════════════════════════════════════════════════════════

globalStatement
    : libnameStatement
    | filenameStatement
    | optionsStatement
    | odsStatement
    | titleStatement
    | footnoteStatement
    | dmStatement
    | systaskStatement
    | endsasStatement
    | lockStatement
    | genericGlobalStatement
    ;

libnameStatement
    : KW_LIBNAME ~(SEMI)* SEMI
    ;

filenameStatement
    : KW_FILENAME ~(SEMI)* SEMI
    ;

optionsStatement
    : KW_OPTIONS optionsItem+ SEMI
    ;

optionsItem
    : macroIfStmt
    | macroDoBlock
    | MACRO_CALL_NAME (LPAREN macroCallArgs? RPAREN)?
    | ~(SEMI | PERCENT_IF | PERCENT_DO | PERCENT_END | PERCENT_THEN | PERCENT_ELSE | MACRO_CALL_NAME)
    ;

odsStatement
    : KW_ODS ~(SEMI)* SEMI
    ;

titleStatement
    : KW_TITLE INT_LITERAL? ~(SEMI)* SEMI
    ;

footnoteStatement
    : KW_FOOTNOTE INT_LITERAL? ~(SEMI)* SEMI
    ;

dmStatement
    : KW_DM ~(SEMI)* SEMI
    ;

systaskStatement
    : KW_SYSTASK ~(SEMI)* SEMI
    ;

endsasStatement
    : KW_ENDSAS SEMI
    ;

lockStatement
    : KW_LOCK ~(SEMI)* SEMI
    ;

// Catch-all for unrecognized global statements (lock, unlock, catname, etc.)
genericGlobalStatement
    : KW_UNLOCK ~(SEMI)* SEMI
    | KW_CATNAME ~(SEMI)* SEMI
    ;

// ═══════════════════════════════════════════════════════════════════════════
// Expressions
// ═══════════════════════════════════════════════════════════════════════════

expressionList
    : expression (COMMA expression)*
    ;

expression
    : orExpression
    ;

orExpression
    : andExpression ((KW_OR | PIPE PIPE) andExpression)*
    ;

andExpression
    : notExpression ((KW_AND | AT AT) notExpression)*
    ;

notExpression
    : (KW_NOT | CARET | TILDE)? comparisonExpression
    ;

comparisonExpression
    : addExpression (comparisonOp addExpression)*
    ;

comparisonOp
    : EQUALS COLON?   // = or =: (truncated comparison)
    | NE_OP | NE_HASH | NE_TILDE | LT_OP COLON? | GT_OP COLON?
    | LE_OP | GE_OP
    | KW_EQ COLON? | KW_NE COLON? | KW_LT COLON? | KW_LE COLON?
    | KW_GT COLON? | KW_GE COLON?
    | KW_IN COLON? | KW_LIKE | KW_BETWEEN
    ;

addExpression
    : mulExpression ((PLUS | MINUS | CONCAT_OP | CONCAT_EXCL) mulExpression)*
    ;

mulExpression
    : powerExpression ((STAR | SLASH) powerExpression)*
    ;

powerExpression
    : unaryExpression (POWER_OP unaryExpression)*
    ;

unaryExpression
    : (PLUS | MINUS)? primaryExpression
    ;

primaryExpression
    : numericLiteral
    | STRING_LITERAL
    | macroFuncCall
    | functionCall
    | identifier LBRACE expressionList RBRACE   // array subscript: lev{i}
    | macroConcatName                            // sasjs&i.data, &lib.&ds, &&var
    | qualifiedName
    | LPAREN expression RPAREN
    | KW_MISSING LPAREN expression RPAREN
    | KW_OF identifierList
    | DOT identifier   // special missing values: .A through .Z and ._
    | KW_CASE expression? sqlWhenClause+ (KW_ELSE expression)? KW_END   // SQL CASE
    | KW_CALCULATED identifier                                           // SQL CALCULATED
    | KW_EXISTS LPAREN sqlQueryExpression RPAREN                         // SQL EXISTS
    ;

// Macro-concatenated name: var&i, &lib.name, sasjs&i.data, &&ref&i
// At least one MACRO_VAR or AMP_AMP must be present
macroConcatName
    : (identifier | INT_LITERAL)* (MACRO_VAR | AMP_AMP) (identifier | MACRO_VAR | AMP_AMP | INT_LITERAL)*
    ;

functionCall
    : qualifiedName LPAREN functionCallArgs? RPAREN
    ;

functionCallArgs
    : KW_DISTINCT expression                     // SQL: COUNT(DISTINCT col)
    | STAR                                        // SQL: COUNT(*)
    | functionCallArg (COMMA functionCallArg)*
    ;

functionCallArg
    : identifier COLON expression   // named parameter (hash object methods: key:value)
    | expression
    ;

numericLiteral
    : INT_LITERAL
    | FLOAT_LITERAL
    | HEX_LITERAL
    | DOT   // SAS missing value
    ;

// ═══════════════════════════════════════════════════════════════════════════
// Identifiers — THE critical rule that includes unreserved keywords
// ═══════════════════════════════════════════════════════════════════════════

identifierList
    : identifier+
    ;

identifier
    : ID
    // The following keywords can appear as identifiers in various contexts
    | KW_DATA | KW_SET | KW_MERGE | KW_UPDATE | KW_MODIFY
    | KW_BY | KW_IF | KW_THEN | KW_ELSE
    | KW_DO | KW_END | KW_OUTPUT | KW_INPUT
    | KW_FILE | KW_PUT | KW_LENGTH | KW_FORMAT
    | KW_INFORMAT | KW_LABEL | KW_ATTRIB | KW_RETAIN
    | KW_ARRAY | KW_DROP | KW_KEEP | KW_WHERE
    | KW_DELETE | KW_CALL | KW_RETURN | KW_ABORT
    | KW_STOP | KW_ERROR | KW_LIST | KW_GOTO | KW_LINK
    | KW_SELECT | KW_WHEN | KW_OTHERWISE | KW_LEAVE | KW_CONTINUE
    | KW_TO | KW_UNTIL | KW_WHILE | KW_NOT
    | KW_AND | KW_OR | KW_IN | KW_EQ | KW_NE
    | KW_LT | KW_LE | KW_GT | KW_GE | KW_OF
    | KW_AS | KW_FROM | KW_BETWEEN | KW_LIKE
    | KW_NULL | KW_MISSING | KW_CLASS | KW_VAR
    | KW_TABLE | KW_TABLES | KW_MODEL | KW_WEIGHT | KW_FREQ
    | KW_NOPRINT | KW_OUT | KW_REPLACE | KW_RENAME
    | KW_COMPRESS | KW_INFILE | KW_RUN | KW_QUIT
    | KW_PROC | KW_LIBNAME | KW_FILENAME | KW_OPTIONS
    | KW_ODS | KW_TITLE | KW_FOOTNOTE
    | KW_DM | KW_SYSTASK | KW_GLOBAL | KW_LOCAL
    | KW_OUTER | KW_INNER | KW_LEFT | KW_RIGHT | KW_FULL
    | KW_JOIN | KW_ON | KW_UNION | KW_ALL | KW_EXCEPT
    | KW_INTERSECT | KW_INTO | KW_CREATE | KW_INSERT
    | KW_VALUES | KW_ALTER | KW_ADD | KW_COLUMN
    | KW_CASE | KW_GROUP | KW_HAVING | KW_ORDER
    | KW_DISTINCT | KW_EXISTS | KW_DESCRIBE
    | KW_CALCULATED | KW_VIEW | KW_NOOBS | KW_NWAY
    | KW_DUPOUT | KW_NODUPKEY | KW_NODUPRECS
    | KW_FORCE | KW_OVERWRITE | KW_CONTENTS
    | KW_CARDS | KW_CARDS4 | KW_DATALINES | KW_DATALINES4
    | KW_PARMCARDS | KW_PARMCARDS4
    | KW_ENDSAS | KW_DESCENDING | KW_ALSO
    | KW_PUTLOG | KW_LOCK | KW_UNLOCK | KW_CATNAME
    | KW_SQL | KW_ASC | KW_DESC | KW_CROSS | KW_NATURAL
    | KW_CORR | KW_CORRESPONDING | KW_IS | KW_CONTAINS
    | KW_CONNECTION | KW_CONNECT | KW_DISCONNECT
    | KW_EXECUTE | KW_USING | KW_RESET | KW_VALIDATE
    | KW_INDEX | KW_PRIMARY | KW_KEY | KW_FOREIGN
    | KW_REFERENCES | KW_UNIQUE | KW_CONSTRAINT
    | KW_CHECK | KW_CASCADE | KW_RESTRICT
    | KW_SEPARATED | KW_TRIMMED | KW_NOTRIM
    ;
