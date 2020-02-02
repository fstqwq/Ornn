// Generated from C:/code/Ornn/src/Ornn/Parser\Mxstar.g4 by ANTLR 4.8
package Ornn.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxstarParser}.
 */
public interface MxstarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#programSection}.
	 * @param ctx the parse tree
	 */
	void enterProgramSection(MxstarParser.ProgramSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#programSection}.
	 * @param ctx the parse tree
	 */
	void exitProgramSection(MxstarParser.ProgramSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(MxstarParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(MxstarParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(MxstarParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(MxstarParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(MxstarParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(MxstarParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclarationList(MxstarParser.ParameterDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclarationList(MxstarParser.ParameterDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclaration(MxstarParser.ParameterDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclaration(MxstarParser.ParameterDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarationList(MxstarParser.VariableDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarationList(MxstarParser.VariableDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#singleVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSingleVariableDeclaration(MxstarParser.SingleVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#singleVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSingleVariableDeclaration(MxstarParser.SingleVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#constructiveFunctionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructiveFunctionDeclaration(MxstarParser.ConstructiveFunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#constructiveFunctionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructiveFunctionDeclaration(MxstarParser.ConstructiveFunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(MxstarParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(MxstarParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MxstarParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MxstarParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#nonarrayType}.
	 * @param ctx the parse tree
	 */
	void enterNonarrayType(MxstarParser.NonarrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#nonarrayType}.
	 * @param ctx the parse tree
	 */
	void exitNonarrayType(MxstarParser.NonarrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(MxstarParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(MxstarParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#newType}.
	 * @param ctx the parse tree
	 */
	void enterNewType(MxstarParser.NewTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#newType}.
	 * @param ctx the parse tree
	 */
	void exitNewType(MxstarParser.NewTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MxstarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MxstarParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(MxstarParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(MxstarParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MxstarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MxstarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxstarParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxstarParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#conditionStatement}.
	 * @param ctx the parse tree
	 */
	void enterConditionStatement(MxstarParser.ConditionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#conditionStatement}.
	 * @param ctx the parse tree
	 */
	void exitConditionStatement(MxstarParser.ConditionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(MxstarParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(MxstarParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 */
	void enterControlStatement(MxstarParser.ControlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 */
	void exitControlStatement(MxstarParser.ControlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(MxstarParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(MxstarParser.KeywordContext ctx);
}