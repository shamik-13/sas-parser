// Generated from SasParser.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SasParser}.
 */
public interface SasParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SasParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(SasParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(SasParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#programStatement}.
	 * @param ctx the parse tree
	 */
	void enterProgramStatement(SasParser.ProgramStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#programStatement}.
	 * @param ctx the parse tree
	 */
	void exitProgramStatement(SasParser.ProgramStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(SasParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(SasParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#lineComment}.
	 * @param ctx the parse tree
	 */
	void enterLineComment(SasParser.LineCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#lineComment}.
	 * @param ctx the parse tree
	 */
	void exitLineComment(SasParser.LineCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroDefinition}.
	 * @param ctx the parse tree
	 */
	void enterMacroDefinition(SasParser.MacroDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroDefinition}.
	 * @param ctx the parse tree
	 */
	void exitMacroDefinition(SasParser.MacroDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroDefOption}.
	 * @param ctx the parse tree
	 */
	void enterMacroDefOption(SasParser.MacroDefOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroDefOption}.
	 * @param ctx the parse tree
	 */
	void exitMacroDefOption(SasParser.MacroDefOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroDefOptionToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroDefOptionToken(SasParser.MacroDefOptionTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroDefOptionToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroDefOptionToken(SasParser.MacroDefOptionTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroParamList}.
	 * @param ctx the parse tree
	 */
	void enterMacroParamList(SasParser.MacroParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroParamList}.
	 * @param ctx the parse tree
	 */
	void exitMacroParamList(SasParser.MacroParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroParam}.
	 * @param ctx the parse tree
	 */
	void enterMacroParam(SasParser.MacroParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroParam}.
	 * @param ctx the parse tree
	 */
	void exitMacroParam(SasParser.MacroParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroParamDefault}.
	 * @param ctx the parse tree
	 */
	void enterMacroParamDefault(SasParser.MacroParamDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroParamDefault}.
	 * @param ctx the parse tree
	 */
	void exitMacroParamDefault(SasParser.MacroParamDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroParamDefaultToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroParamDefaultToken(SasParser.MacroParamDefaultTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroParamDefaultToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroParamDefaultToken(SasParser.MacroParamDefaultTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroBody}.
	 * @param ctx the parse tree
	 */
	void enterMacroBody(SasParser.MacroBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroBody}.
	 * @param ctx the parse tree
	 */
	void exitMacroBody(SasParser.MacroBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroGenericText}.
	 * @param ctx the parse tree
	 */
	void enterMacroGenericText(SasParser.MacroGenericTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroGenericText}.
	 * @param ctx the parse tree
	 */
	void exitMacroGenericText(SasParser.MacroGenericTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroGenericToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroGenericToken(SasParser.MacroGenericTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroGenericToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroGenericToken(SasParser.MacroGenericTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroStatement}.
	 * @param ctx the parse tree
	 */
	void enterMacroStatement(SasParser.MacroStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroStatement}.
	 * @param ctx the parse tree
	 */
	void exitMacroStatement(SasParser.MacroStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroLetStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroLetStmt(SasParser.MacroLetStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroLetStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroLetStmt(SasParser.MacroLetStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroVarRef}.
	 * @param ctx the parse tree
	 */
	void enterMacroVarRef(SasParser.MacroVarRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroVarRef}.
	 * @param ctx the parse tree
	 */
	void exitMacroVarRef(SasParser.MacroVarRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroGlobalStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroGlobalStmt(SasParser.MacroGlobalStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroGlobalStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroGlobalStmt(SasParser.MacroGlobalStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroLocalStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroLocalStmt(SasParser.MacroLocalStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroLocalStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroLocalStmt(SasParser.MacroLocalStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroVarNameList}.
	 * @param ctx the parse tree
	 */
	void enterMacroVarNameList(SasParser.MacroVarNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroVarNameList}.
	 * @param ctx the parse tree
	 */
	void exitMacroVarNameList(SasParser.MacroVarNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroIfStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroIfStmt(SasParser.MacroIfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroIfStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroIfStmt(SasParser.MacroIfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCondition}.
	 * @param ctx the parse tree
	 */
	void enterMacroCondition(SasParser.MacroConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCondition}.
	 * @param ctx the parse tree
	 */
	void exitMacroCondition(SasParser.MacroConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCondToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroCondToken(SasParser.MacroCondTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCondToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroCondToken(SasParser.MacroCondTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroIfAction}.
	 * @param ctx the parse tree
	 */
	void enterMacroIfAction(SasParser.MacroIfActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroIfAction}.
	 * @param ctx the parse tree
	 */
	void exitMacroIfAction(SasParser.MacroIfActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroActionText}.
	 * @param ctx the parse tree
	 */
	void enterMacroActionText(SasParser.MacroActionTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroActionText}.
	 * @param ctx the parse tree
	 */
	void exitMacroActionText(SasParser.MacroActionTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroActionToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroActionToken(SasParser.MacroActionTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroActionToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroActionToken(SasParser.MacroActionTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroDoBlock}.
	 * @param ctx the parse tree
	 */
	void enterMacroDoBlock(SasParser.MacroDoBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroDoBlock}.
	 * @param ctx the parse tree
	 */
	void exitMacroDoBlock(SasParser.MacroDoBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroDoSpec}.
	 * @param ctx the parse tree
	 */
	void enterMacroDoSpec(SasParser.MacroDoSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroDoSpec}.
	 * @param ctx the parse tree
	 */
	void exitMacroDoSpec(SasParser.MacroDoSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCondWhile}.
	 * @param ctx the parse tree
	 */
	void enterMacroCondWhile(SasParser.MacroCondWhileContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCondWhile}.
	 * @param ctx the parse tree
	 */
	void exitMacroCondWhile(SasParser.MacroCondWhileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCondWhileToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroCondWhileToken(SasParser.MacroCondWhileTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCondWhileToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroCondWhileToken(SasParser.MacroCondWhileTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroDoBody}.
	 * @param ctx the parse tree
	 */
	void enterMacroDoBody(SasParser.MacroDoBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroDoBody}.
	 * @param ctx the parse tree
	 */
	void exitMacroDoBody(SasParser.MacroDoBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroPutStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroPutStmt(SasParser.MacroPutStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroPutStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroPutStmt(SasParser.MacroPutStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroPutText}.
	 * @param ctx the parse tree
	 */
	void enterMacroPutText(SasParser.MacroPutTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroPutText}.
	 * @param ctx the parse tree
	 */
	void exitMacroPutText(SasParser.MacroPutTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroReturnStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroReturnStmt(SasParser.MacroReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroReturnStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroReturnStmt(SasParser.MacroReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroGotoStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroGotoStmt(SasParser.MacroGotoStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroGotoStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroGotoStmt(SasParser.MacroGotoStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroLabelStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroLabelStmt(SasParser.MacroLabelStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroLabelStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroLabelStmt(SasParser.MacroLabelStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroIncludeStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroIncludeStmt(SasParser.MacroIncludeStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroIncludeStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroIncludeStmt(SasParser.MacroIncludeStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroIncludeTarget}.
	 * @param ctx the parse tree
	 */
	void enterMacroIncludeTarget(SasParser.MacroIncludeTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroIncludeTarget}.
	 * @param ctx the parse tree
	 */
	void exitMacroIncludeTarget(SasParser.MacroIncludeTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroAbortStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroAbortStmt(SasParser.MacroAbortStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroAbortStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroAbortStmt(SasParser.MacroAbortStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCallStmt}.
	 * @param ctx the parse tree
	 */
	void enterMacroCallStmt(SasParser.MacroCallStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCallStmt}.
	 * @param ctx the parse tree
	 */
	void exitMacroCallStmt(SasParser.MacroCallStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCallArgs}.
	 * @param ctx the parse tree
	 */
	void enterMacroCallArgs(SasParser.MacroCallArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCallArgs}.
	 * @param ctx the parse tree
	 */
	void exitMacroCallArgs(SasParser.MacroCallArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCallArg}.
	 * @param ctx the parse tree
	 */
	void enterMacroCallArg(SasParser.MacroCallArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCallArg}.
	 * @param ctx the parse tree
	 */
	void exitMacroCallArg(SasParser.MacroCallArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroCallArgToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroCallArgToken(SasParser.MacroCallArgTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroCallArgToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroCallArgToken(SasParser.MacroCallArgTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#nestedParens}.
	 * @param ctx the parse tree
	 */
	void enterNestedParens(SasParser.NestedParensContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#nestedParens}.
	 * @param ctx the parse tree
	 */
	void exitNestedParens(SasParser.NestedParensContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroExprText}.
	 * @param ctx the parse tree
	 */
	void enterMacroExprText(SasParser.MacroExprTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroExprText}.
	 * @param ctx the parse tree
	 */
	void exitMacroExprText(SasParser.MacroExprTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroExprToken}.
	 * @param ctx the parse tree
	 */
	void enterMacroExprToken(SasParser.MacroExprTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroExprToken}.
	 * @param ctx the parse tree
	 */
	void exitMacroExprToken(SasParser.MacroExprTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroFuncCall}.
	 * @param ctx the parse tree
	 */
	void enterMacroFuncCall(SasParser.MacroFuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroFuncCall}.
	 * @param ctx the parse tree
	 */
	void exitMacroFuncCall(SasParser.MacroFuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroQuoteCall}.
	 * @param ctx the parse tree
	 */
	void enterMacroQuoteCall(SasParser.MacroQuoteCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroQuoteCall}.
	 * @param ctx the parse tree
	 */
	void exitMacroQuoteCall(SasParser.MacroQuoteCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#strContent}.
	 * @param ctx the parse tree
	 */
	void enterStrContent(SasParser.StrContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#strContent}.
	 * @param ctx the parse tree
	 */
	void exitStrContent(SasParser.StrContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#dataStep}.
	 * @param ctx the parse tree
	 */
	void enterDataStep(SasParser.DataStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#dataStep}.
	 * @param ctx the parse tree
	 */
	void exitDataStep(SasParser.DataStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#dataStepHeaderItem}.
	 * @param ctx the parse tree
	 */
	void enterDataStepHeaderItem(SasParser.DataStepHeaderItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#dataStepHeaderItem}.
	 * @param ctx the parse tree
	 */
	void exitDataStepHeaderItem(SasParser.DataStepHeaderItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#datasetName}.
	 * @param ctx the parse tree
	 */
	void enterDatasetName(SasParser.DatasetNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#datasetName}.
	 * @param ctx the parse tree
	 */
	void exitDatasetName(SasParser.DatasetNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroDatasetRef}.
	 * @param ctx the parse tree
	 */
	void enterMacroDatasetRef(SasParser.MacroDatasetRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroDatasetRef}.
	 * @param ctx the parse tree
	 */
	void exitMacroDatasetRef(SasParser.MacroDatasetRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(SasParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(SasParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#datasetOptions}.
	 * @param ctx the parse tree
	 */
	void enterDatasetOptions(SasParser.DatasetOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#datasetOptions}.
	 * @param ctx the parse tree
	 */
	void exitDatasetOptions(SasParser.DatasetOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#dataStepOption}.
	 * @param ctx the parse tree
	 */
	void enterDataStepOption(SasParser.DataStepOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#dataStepOption}.
	 * @param ctx the parse tree
	 */
	void exitDataStepOption(SasParser.DataStepOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#dataStepStatement}.
	 * @param ctx the parse tree
	 */
	void enterDataStepStatement(SasParser.DataStepStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#dataStepStatement}.
	 * @param ctx the parse tree
	 */
	void exitDataStepStatement(SasParser.DataStepStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#stmtLabel}.
	 * @param ctx the parse tree
	 */
	void enterStmtLabel(SasParser.StmtLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#stmtLabel}.
	 * @param ctx the parse tree
	 */
	void exitStmtLabel(SasParser.StmtLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetStatement(SasParser.SetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#setStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetStatement(SasParser.SetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#setOptions}.
	 * @param ctx the parse tree
	 */
	void enterSetOptions(SasParser.SetOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#setOptions}.
	 * @param ctx the parse tree
	 */
	void exitSetOptions(SasParser.SetOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#mergeStatement}.
	 * @param ctx the parse tree
	 */
	void enterMergeStatement(SasParser.MergeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#mergeStatement}.
	 * @param ctx the parse tree
	 */
	void exitMergeStatement(SasParser.MergeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#byStatement}.
	 * @param ctx the parse tree
	 */
	void enterByStatement(SasParser.ByStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#byStatement}.
	 * @param ctx the parse tree
	 */
	void exitByStatement(SasParser.ByStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#byVariable}.
	 * @param ctx the parse tree
	 */
	void enterByVariable(SasParser.ByVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#byVariable}.
	 * @param ctx the parse tree
	 */
	void exitByVariable(SasParser.ByVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#ifThenElseStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfThenElseStmt(SasParser.IfThenElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#ifThenElseStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfThenElseStmt(SasParser.IfThenElseStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#actionStatement}.
	 * @param ctx the parse tree
	 */
	void enterActionStatement(SasParser.ActionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#actionStatement}.
	 * @param ctx the parse tree
	 */
	void exitActionStatement(SasParser.ActionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#doBlock}.
	 * @param ctx the parse tree
	 */
	void enterDoBlock(SasParser.DoBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#doBlock}.
	 * @param ctx the parse tree
	 */
	void exitDoBlock(SasParser.DoBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#doSpec}.
	 * @param ctx the parse tree
	 */
	void enterDoSpec(SasParser.DoSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#doSpec}.
	 * @param ctx the parse tree
	 */
	void exitDoSpec(SasParser.DoSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#selectBlock}.
	 * @param ctx the parse tree
	 */
	void enterSelectBlock(SasParser.SelectBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#selectBlock}.
	 * @param ctx the parse tree
	 */
	void exitSelectBlock(SasParser.SelectBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void enterWhenClause(SasParser.WhenClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void exitWhenClause(SasParser.WhenClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#otherwiseClause}.
	 * @param ctx the parse tree
	 */
	void enterOtherwiseClause(SasParser.OtherwiseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#otherwiseClause}.
	 * @param ctx the parse tree
	 */
	void exitOtherwiseClause(SasParser.OtherwiseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#assignmentOrCallStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOrCallStmt(SasParser.AssignmentOrCallStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#assignmentOrCallStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOrCallStmt(SasParser.AssignmentOrCallStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#lengthStatement}.
	 * @param ctx the parse tree
	 */
	void enterLengthStatement(SasParser.LengthStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#lengthStatement}.
	 * @param ctx the parse tree
	 */
	void exitLengthStatement(SasParser.LengthStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#formatStatement}.
	 * @param ctx the parse tree
	 */
	void enterFormatStatement(SasParser.FormatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#formatStatement}.
	 * @param ctx the parse tree
	 */
	void exitFormatStatement(SasParser.FormatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#informatStatement}.
	 * @param ctx the parse tree
	 */
	void enterInformatStatement(SasParser.InformatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#informatStatement}.
	 * @param ctx the parse tree
	 */
	void exitInformatStatement(SasParser.InformatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#labelStatement}.
	 * @param ctx the parse tree
	 */
	void enterLabelStatement(SasParser.LabelStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#labelStatement}.
	 * @param ctx the parse tree
	 */
	void exitLabelStatement(SasParser.LabelStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#labelItem}.
	 * @param ctx the parse tree
	 */
	void enterLabelItem(SasParser.LabelItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#labelItem}.
	 * @param ctx the parse tree
	 */
	void exitLabelItem(SasParser.LabelItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#attribStatement}.
	 * @param ctx the parse tree
	 */
	void enterAttribStatement(SasParser.AttribStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#attribStatement}.
	 * @param ctx the parse tree
	 */
	void exitAttribStatement(SasParser.AttribStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#attribItem}.
	 * @param ctx the parse tree
	 */
	void enterAttribItem(SasParser.AttribItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#attribItem}.
	 * @param ctx the parse tree
	 */
	void exitAttribItem(SasParser.AttribItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#attribSpec}.
	 * @param ctx the parse tree
	 */
	void enterAttribSpec(SasParser.AttribSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#attribSpec}.
	 * @param ctx the parse tree
	 */
	void exitAttribSpec(SasParser.AttribSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#retainStatement}.
	 * @param ctx the parse tree
	 */
	void enterRetainStatement(SasParser.RetainStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#retainStatement}.
	 * @param ctx the parse tree
	 */
	void exitRetainStatement(SasParser.RetainStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#retainItem}.
	 * @param ctx the parse tree
	 */
	void enterRetainItem(SasParser.RetainItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#retainItem}.
	 * @param ctx the parse tree
	 */
	void exitRetainItem(SasParser.RetainItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#arrayStatement}.
	 * @param ctx the parse tree
	 */
	void enterArrayStatement(SasParser.ArrayStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#arrayStatement}.
	 * @param ctx the parse tree
	 */
	void exitArrayStatement(SasParser.ArrayStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#keepStatement}.
	 * @param ctx the parse tree
	 */
	void enterKeepStatement(SasParser.KeepStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#keepStatement}.
	 * @param ctx the parse tree
	 */
	void exitKeepStatement(SasParser.KeepStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#dropStatement}.
	 * @param ctx the parse tree
	 */
	void enterDropStatement(SasParser.DropStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#dropStatement}.
	 * @param ctx the parse tree
	 */
	void exitDropStatement(SasParser.DropStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#whereStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhereStatement(SasParser.WhereStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#whereStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhereStatement(SasParser.WhereStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#outputStatement}.
	 * @param ctx the parse tree
	 */
	void enterOutputStatement(SasParser.OutputStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#outputStatement}.
	 * @param ctx the parse tree
	 */
	void exitOutputStatement(SasParser.OutputStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(SasParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(SasParser.DeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#putStatement}.
	 * @param ctx the parse tree
	 */
	void enterPutStatement(SasParser.PutStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#putStatement}.
	 * @param ctx the parse tree
	 */
	void exitPutStatement(SasParser.PutStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#putlogStatement}.
	 * @param ctx the parse tree
	 */
	void enterPutlogStatement(SasParser.PutlogStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#putlogStatement}.
	 * @param ctx the parse tree
	 */
	void exitPutlogStatement(SasParser.PutlogStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#inputStatement}.
	 * @param ctx the parse tree
	 */
	void enterInputStatement(SasParser.InputStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#inputStatement}.
	 * @param ctx the parse tree
	 */
	void exitInputStatement(SasParser.InputStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#infileStatement}.
	 * @param ctx the parse tree
	 */
	void enterInfileStatement(SasParser.InfileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#infileStatement}.
	 * @param ctx the parse tree
	 */
	void exitInfileStatement(SasParser.InfileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#infileOptions}.
	 * @param ctx the parse tree
	 */
	void enterInfileOptions(SasParser.InfileOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#infileOptions}.
	 * @param ctx the parse tree
	 */
	void exitInfileOptions(SasParser.InfileOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#fileStatement}.
	 * @param ctx the parse tree
	 */
	void enterFileStatement(SasParser.FileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#fileStatement}.
	 * @param ctx the parse tree
	 */
	void exitFileStatement(SasParser.FileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#fileOptions}.
	 * @param ctx the parse tree
	 */
	void enterFileOptions(SasParser.FileOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#fileOptions}.
	 * @param ctx the parse tree
	 */
	void exitFileOptions(SasParser.FileOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void enterCallStatement(SasParser.CallStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#callStatement}.
	 * @param ctx the parse tree
	 */
	void exitCallStatement(SasParser.CallStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(SasParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(SasParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#abortStatement}.
	 * @param ctx the parse tree
	 */
	void enterAbortStatement(SasParser.AbortStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#abortStatement}.
	 * @param ctx the parse tree
	 */
	void exitAbortStatement(SasParser.AbortStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#stopStatement}.
	 * @param ctx the parse tree
	 */
	void enterStopStatement(SasParser.StopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#stopStatement}.
	 * @param ctx the parse tree
	 */
	void exitStopStatement(SasParser.StopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#errorStatement}.
	 * @param ctx the parse tree
	 */
	void enterErrorStatement(SasParser.ErrorStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#errorStatement}.
	 * @param ctx the parse tree
	 */
	void exitErrorStatement(SasParser.ErrorStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#gotoStatement}.
	 * @param ctx the parse tree
	 */
	void enterGotoStatement(SasParser.GotoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#gotoStatement}.
	 * @param ctx the parse tree
	 */
	void exitGotoStatement(SasParser.GotoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#linkStatement}.
	 * @param ctx the parse tree
	 */
	void enterLinkStatement(SasParser.LinkStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#linkStatement}.
	 * @param ctx the parse tree
	 */
	void exitLinkStatement(SasParser.LinkStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void enterLeaveStatement(SasParser.LeaveStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void exitLeaveStatement(SasParser.LeaveStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(SasParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(SasParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#listStatement}.
	 * @param ctx the parse tree
	 */
	void enterListStatement(SasParser.ListStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#listStatement}.
	 * @param ctx the parse tree
	 */
	void exitListStatement(SasParser.ListStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#cardsStatement}.
	 * @param ctx the parse tree
	 */
	void enterCardsStatement(SasParser.CardsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#cardsStatement}.
	 * @param ctx the parse tree
	 */
	void exitCardsStatement(SasParser.CardsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#cardsData}.
	 * @param ctx the parse tree
	 */
	void enterCardsData(SasParser.CardsDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#cardsData}.
	 * @param ctx the parse tree
	 */
	void exitCardsData(SasParser.CardsDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#procStep}.
	 * @param ctx the parse tree
	 */
	void enterProcStep(SasParser.ProcStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#procStep}.
	 * @param ctx the parse tree
	 */
	void exitProcStep(SasParser.ProcStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#procOptionToken}.
	 * @param ctx the parse tree
	 */
	void enterProcOptionToken(SasParser.ProcOptionTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#procOptionToken}.
	 * @param ctx the parse tree
	 */
	void exitProcOptionToken(SasParser.ProcOptionTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#procBody}.
	 * @param ctx the parse tree
	 */
	void enterProcBody(SasParser.ProcBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#procBody}.
	 * @param ctx the parse tree
	 */
	void exitProcBody(SasParser.ProcBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#procBodyStatement}.
	 * @param ctx the parse tree
	 */
	void enterProcBodyStatement(SasParser.ProcBodyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#procBodyStatement}.
	 * @param ctx the parse tree
	 */
	void exitProcBodyStatement(SasParser.ProcBodyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#globalStatement}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStatement(SasParser.GlobalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#globalStatement}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStatement(SasParser.GlobalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#libnameStatement}.
	 * @param ctx the parse tree
	 */
	void enterLibnameStatement(SasParser.LibnameStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#libnameStatement}.
	 * @param ctx the parse tree
	 */
	void exitLibnameStatement(SasParser.LibnameStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#filenameStatement}.
	 * @param ctx the parse tree
	 */
	void enterFilenameStatement(SasParser.FilenameStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#filenameStatement}.
	 * @param ctx the parse tree
	 */
	void exitFilenameStatement(SasParser.FilenameStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#optionsStatement}.
	 * @param ctx the parse tree
	 */
	void enterOptionsStatement(SasParser.OptionsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#optionsStatement}.
	 * @param ctx the parse tree
	 */
	void exitOptionsStatement(SasParser.OptionsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#optionsItem}.
	 * @param ctx the parse tree
	 */
	void enterOptionsItem(SasParser.OptionsItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#optionsItem}.
	 * @param ctx the parse tree
	 */
	void exitOptionsItem(SasParser.OptionsItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#odsStatement}.
	 * @param ctx the parse tree
	 */
	void enterOdsStatement(SasParser.OdsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#odsStatement}.
	 * @param ctx the parse tree
	 */
	void exitOdsStatement(SasParser.OdsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#titleStatement}.
	 * @param ctx the parse tree
	 */
	void enterTitleStatement(SasParser.TitleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#titleStatement}.
	 * @param ctx the parse tree
	 */
	void exitTitleStatement(SasParser.TitleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#footnoteStatement}.
	 * @param ctx the parse tree
	 */
	void enterFootnoteStatement(SasParser.FootnoteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#footnoteStatement}.
	 * @param ctx the parse tree
	 */
	void exitFootnoteStatement(SasParser.FootnoteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#dmStatement}.
	 * @param ctx the parse tree
	 */
	void enterDmStatement(SasParser.DmStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#dmStatement}.
	 * @param ctx the parse tree
	 */
	void exitDmStatement(SasParser.DmStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#systaskStatement}.
	 * @param ctx the parse tree
	 */
	void enterSystaskStatement(SasParser.SystaskStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#systaskStatement}.
	 * @param ctx the parse tree
	 */
	void exitSystaskStatement(SasParser.SystaskStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#endsasStatement}.
	 * @param ctx the parse tree
	 */
	void enterEndsasStatement(SasParser.EndsasStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#endsasStatement}.
	 * @param ctx the parse tree
	 */
	void exitEndsasStatement(SasParser.EndsasStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#lockStatement}.
	 * @param ctx the parse tree
	 */
	void enterLockStatement(SasParser.LockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#lockStatement}.
	 * @param ctx the parse tree
	 */
	void exitLockStatement(SasParser.LockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#genericGlobalStatement}.
	 * @param ctx the parse tree
	 */
	void enterGenericGlobalStatement(SasParser.GenericGlobalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#genericGlobalStatement}.
	 * @param ctx the parse tree
	 */
	void exitGenericGlobalStatement(SasParser.GenericGlobalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(SasParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(SasParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SasParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SasParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(SasParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(SasParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(SasParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(SasParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(SasParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(SasParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(SasParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(SasParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#comparisonOp}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOp(SasParser.ComparisonOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#comparisonOp}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOp(SasParser.ComparisonOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#addExpression}.
	 * @param ctx the parse tree
	 */
	void enterAddExpression(SasParser.AddExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#addExpression}.
	 * @param ctx the parse tree
	 */
	void exitAddExpression(SasParser.AddExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#mulExpression}.
	 * @param ctx the parse tree
	 */
	void enterMulExpression(SasParser.MulExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#mulExpression}.
	 * @param ctx the parse tree
	 */
	void exitMulExpression(SasParser.MulExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#powerExpression}.
	 * @param ctx the parse tree
	 */
	void enterPowerExpression(SasParser.PowerExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#powerExpression}.
	 * @param ctx the parse tree
	 */
	void exitPowerExpression(SasParser.PowerExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(SasParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(SasParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(SasParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(SasParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#macroConcatName}.
	 * @param ctx the parse tree
	 */
	void enterMacroConcatName(SasParser.MacroConcatNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#macroConcatName}.
	 * @param ctx the parse tree
	 */
	void exitMacroConcatName(SasParser.MacroConcatNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(SasParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(SasParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(SasParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(SasParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(SasParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(SasParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SasParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SasParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SasParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SasParser.IdentifierContext ctx);
}