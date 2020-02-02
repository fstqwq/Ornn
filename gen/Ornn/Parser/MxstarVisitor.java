// Generated from C:/code/Ornn/src/Ornn/Parser\Mxstar.g4 by ANTLR 4.8
package Ornn.Parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxstarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxstarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#programSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramSection(MxstarParser.ProgramSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(MxstarParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(MxstarParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(MxstarParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclarationList(MxstarParser.ParameterDeclarationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclaration(MxstarParser.ParameterDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationList(MxstarParser.VariableDeclarationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#singleVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleVariableDeclaration(MxstarParser.SingleVariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#constructiveFunctionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructiveFunctionDeclaration(MxstarParser.ConstructiveFunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MxstarParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(MxstarParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#nonarrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonarrayType(MxstarParser.NonarrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#returnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnType(MxstarParser.ReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#newType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewType(MxstarParser.NewTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(MxstarParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(MxstarParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MxstarParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(MxstarParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#conditionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionStatement(MxstarParser.ConditionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#loopStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopStatement(MxstarParser.LoopStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlStatement(MxstarParser.ControlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(MxstarParser.KeywordContext ctx);
}