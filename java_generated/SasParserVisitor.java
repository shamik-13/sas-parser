// Generated from SasParser.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SasParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SasParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SasParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(SasParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#programStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramStatement(SasParser.ProgramStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(SasParser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#lineComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineComment(SasParser.LineCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDefinition(SasParser.MacroDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroDefOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDefOption(SasParser.MacroDefOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroDefOptionToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDefOptionToken(SasParser.MacroDefOptionTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroParamList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroParamList(SasParser.MacroParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroParam(SasParser.MacroParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroParamDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroParamDefault(SasParser.MacroParamDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroParamDefaultToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroParamDefaultToken(SasParser.MacroParamDefaultTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroBody(SasParser.MacroBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroGenericText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroGenericText(SasParser.MacroGenericTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroGenericToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroGenericToken(SasParser.MacroGenericTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroStatement(SasParser.MacroStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroLetStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroLetStmt(SasParser.MacroLetStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroVarRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroVarRef(SasParser.MacroVarRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroGlobalStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroGlobalStmt(SasParser.MacroGlobalStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroLocalStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroLocalStmt(SasParser.MacroLocalStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroVarNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroVarNameList(SasParser.MacroVarNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroIfStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroIfStmt(SasParser.MacroIfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCondition(SasParser.MacroConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCondToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCondToken(SasParser.MacroCondTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroIfAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroIfAction(SasParser.MacroIfActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroActionText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroActionText(SasParser.MacroActionTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroActionToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroActionToken(SasParser.MacroActionTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroDoBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDoBlock(SasParser.MacroDoBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroDoSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDoSpec(SasParser.MacroDoSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCondWhile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCondWhile(SasParser.MacroCondWhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCondWhileToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCondWhileToken(SasParser.MacroCondWhileTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroDoBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDoBody(SasParser.MacroDoBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroPutStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroPutStmt(SasParser.MacroPutStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroPutText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroPutText(SasParser.MacroPutTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroReturnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroReturnStmt(SasParser.MacroReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroGotoStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroGotoStmt(SasParser.MacroGotoStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroLabelStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroLabelStmt(SasParser.MacroLabelStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroIncludeStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroIncludeStmt(SasParser.MacroIncludeStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroIncludeTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroIncludeTarget(SasParser.MacroIncludeTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroAbortStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroAbortStmt(SasParser.MacroAbortStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCallStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCallStmt(SasParser.MacroCallStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCallArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCallArgs(SasParser.MacroCallArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCallArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCallArg(SasParser.MacroCallArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroCallArgToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroCallArgToken(SasParser.MacroCallArgTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#nestedParens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedParens(SasParser.NestedParensContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroExprText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroExprText(SasParser.MacroExprTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroExprToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroExprToken(SasParser.MacroExprTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroFuncCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroFuncCall(SasParser.MacroFuncCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroQuoteCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroQuoteCall(SasParser.MacroQuoteCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#strContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrContent(SasParser.StrContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#dataStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStep(SasParser.DataStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#dataStepHeaderItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStepHeaderItem(SasParser.DataStepHeaderItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#datasetName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatasetName(SasParser.DatasetNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroDatasetRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroDatasetRef(SasParser.MacroDatasetRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(SasParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#datasetOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatasetOptions(SasParser.DatasetOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#dataStepOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStepOption(SasParser.DataStepOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#dataStepStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStepStatement(SasParser.DataStepStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#stmtLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtLabel(SasParser.StmtLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#setStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetStatement(SasParser.SetStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#setOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetOptions(SasParser.SetOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#mergeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeStatement(SasParser.MergeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#byStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByStatement(SasParser.ByStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#byVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByVariable(SasParser.ByVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#ifThenElseStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThenElseStmt(SasParser.IfThenElseStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#actionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionStatement(SasParser.ActionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#doBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoBlock(SasParser.DoBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#doSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoSpec(SasParser.DoSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#selectBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectBlock(SasParser.SelectBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#whenClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenClause(SasParser.WhenClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#otherwiseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOtherwiseClause(SasParser.OtherwiseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#assignmentOrCallStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOrCallStmt(SasParser.AssignmentOrCallStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#lengthStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLengthStatement(SasParser.LengthStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#formatStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormatStatement(SasParser.FormatStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#informatStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInformatStatement(SasParser.InformatStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#labelStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelStatement(SasParser.LabelStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#labelItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelItem(SasParser.LabelItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#attribStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribStatement(SasParser.AttribStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#attribItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribItem(SasParser.AttribItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#attribSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribSpec(SasParser.AttribSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#retainStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetainStatement(SasParser.RetainStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#retainItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetainItem(SasParser.RetainItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#arrayStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayStatement(SasParser.ArrayStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#keepStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeepStatement(SasParser.KeepStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#dropStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropStatement(SasParser.DropStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#whereStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereStatement(SasParser.WhereStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#outputStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutputStatement(SasParser.OutputStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#deleteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStatement(SasParser.DeleteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#putStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPutStatement(SasParser.PutStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#putlogStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPutlogStatement(SasParser.PutlogStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#inputStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputStatement(SasParser.InputStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#infileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfileStatement(SasParser.InfileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#infileOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfileOptions(SasParser.InfileOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#fileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileStatement(SasParser.FileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#fileOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileOptions(SasParser.FileOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#callStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallStatement(SasParser.CallStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(SasParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#abortStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbortStatement(SasParser.AbortStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#stopStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStopStatement(SasParser.StopStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#errorStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorStatement(SasParser.ErrorStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#gotoStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGotoStatement(SasParser.GotoStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#linkStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkStatement(SasParser.LinkStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#leaveStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeaveStatement(SasParser.LeaveStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(SasParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#listStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListStatement(SasParser.ListStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#cardsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCardsStatement(SasParser.CardsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#cardsData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCardsData(SasParser.CardsDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#procStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcStep(SasParser.ProcStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#procOptionToken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcOptionToken(SasParser.ProcOptionTokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#procBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcBody(SasParser.ProcBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#procBodyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcBodyStatement(SasParser.ProcBodyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#globalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStatement(SasParser.GlobalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#libnameStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLibnameStatement(SasParser.LibnameStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#filenameStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilenameStatement(SasParser.FilenameStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#optionsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionsStatement(SasParser.OptionsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#optionsItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionsItem(SasParser.OptionsItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#odsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOdsStatement(SasParser.OdsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#titleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTitleStatement(SasParser.TitleStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#footnoteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFootnoteStatement(SasParser.FootnoteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#dmStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDmStatement(SasParser.DmStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#systaskStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystaskStatement(SasParser.SystaskStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#endsasStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndsasStatement(SasParser.EndsasStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#lockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockStatement(SasParser.LockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#genericGlobalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericGlobalStatement(SasParser.GenericGlobalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(SasParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SasParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#orExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(SasParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(SasParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#notExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(SasParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(SasParser.ComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#comparisonOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOp(SasParser.ComparisonOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#addExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpression(SasParser.AddExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#mulExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExpression(SasParser.MulExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#powerExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowerExpression(SasParser.PowerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(SasParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(SasParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#macroConcatName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroConcatName(SasParser.MacroConcatNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(SasParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(SasParser.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(SasParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SasParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(SasParser.IdentifierContext ctx);
}