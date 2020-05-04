package Ornn.frontend;

import Ornn.AST.*;
import Ornn.parser.MxstarBaseVisitor;
import Ornn.parser.MxstarParser;
import Ornn.util.CompilationError;
import Ornn.util.Position;
import Ornn.util.StringParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends MxstarBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(MxstarParser.ProgramContext ctx) {
        List<DeclNode> declNodeList = new ArrayList<>();
        if (ctx.programSection() != null) {
            for (ParserRuleContext programSection : ctx.programSection()) {
                ASTNode decl = visit(programSection);
                if (decl instanceof VarDeclListNode) {
                    declNodeList.addAll(((VarDeclListNode) decl).getDeclList());
                }
                else {
                    declNodeList.add((DeclNode) decl);
                }
            }
        }
        return new ProgramNode(declNodeList, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitProgramSection(MxstarParser.ProgramSectionContext ctx) {
        if (null != ctx.functionDeclaration()) return visit(ctx.functionDeclaration());
        if (null != ctx.variableDeclaration()) return visit(ctx.variableDeclaration());
        if (null != ctx.classDeclaration()) return visit(ctx.classDeclaration());
        throw new RuntimeException("unreachable");
    }

    @Override
    public ASTNode visitFunctionDeclaration(MxstarParser.FunctionDeclarationContext ctx) {
        TypeNode type = (TypeNode)visit(ctx.returnType());
        String identifier = ctx.Identifier().getText();
        List<VarDeclNode> varDeclNodes =
                ctx.parameterDeclarationList() == null
                ? new ArrayList<>()
                : ((VarDeclListNode) visit(ctx.parameterDeclarationList())).getDeclList();
        BlockStmtNode blockStmt = (BlockStmtNode) visit(ctx.block());
        return new FuncDeclNode(type, identifier, varDeclNodes, blockStmt, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitConstructiveFunctionDeclaration(MxstarParser.ConstructiveFunctionDeclarationContext ctx) {
        String identifier = ctx.Identifier().getText();
        BlockStmtNode blockStmt = (BlockStmtNode) visit(ctx.block());
        return new FuncDeclNode(
                null,
                identifier,
                null,
                blockStmt,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitParameterDeclarationList(MxstarParser.ParameterDeclarationListContext ctx) {
        VarDeclListNode parameterDeclarationList = new VarDeclListNode(new Position(ctx.getStart()));
        for (ParserRuleContext parameterDecl : ctx.parameterDeclaration()) {
            parameterDeclarationList.addDecl((VarDeclNode) visit(parameterDecl));
        }
        return parameterDeclarationList;
    }

    @Override
    public ASTNode visitParameterDeclaration(MxstarParser.ParameterDeclarationContext ctx) {
        return new VarDeclNode(
                (TypeNode) visit(ctx.type()),
                null,
                ctx.Identifier().getText(),
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitVariableDeclaration(MxstarParser.VariableDeclarationContext ctx) {
        VarDeclListNode varDeclList = (VarDeclListNode) visit(ctx.variableDeclarationList());
        varDeclList.setType((TypeNode) visit(ctx.type()));
        return varDeclList;
    }

    @Override
    public ASTNode visitVariableDeclarationList(MxstarParser.VariableDeclarationListContext ctx) {
        VarDeclListNode varDeclList = new VarDeclListNode(new Position(ctx.getStart()));
        for (ParserRuleContext singleVarDecl : ctx.singleVariableDeclaration()) {
            varDeclList.addDecl((VarDeclNode) visit(singleVarDecl));
        }
        return varDeclList;
    }

    @Override
    public ASTNode visitClassDeclaration(MxstarParser.ClassDeclarationContext ctx) {
        ClassDeclNode classDeclNode = new ClassDeclNode(ctx.Identifier().getText(), new Position(ctx.getStart()));
        for (ParserRuleContext decl : ctx.variableDeclaration()) {
            classDeclNode.addVarDecl(((VarDeclListNode) visit(decl)).getDeclList());
        }
        for (ParserRuleContext decl : ctx.functionDeclaration()) {
            classDeclNode.addFuncDecl((FuncDeclNode) visit(decl));
        }
        for (ParserRuleContext decl : ctx.constructiveFunctionDeclaration()) {
            classDeclNode.addFuncDecl((FuncDeclNode) visit(decl));
        }
        return classDeclNode;
    }

    @Override
    public ASTNode visitSingleVariableDeclaration(MxstarParser.SingleVariableDeclarationContext ctx) {
        return new VarDeclNode(null,
                ctx.expression() != null ? (ExprNode) visit(ctx.expression()) : null,
                ctx.Identifier().getText(),
                new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitBlock(MxstarParser.BlockContext ctx) {
        List<StmtNode> stmtNodes = new ArrayList<>();
        for (ParserRuleContext stmt : ctx.statement()) {
            StmtNode stmtNode = (StmtNode) visit(stmt);
            if (stmtNode == null || (stmtNode instanceof BlockStmtNode && ((BlockStmtNode) stmtNode).getStmtList().size() == 0)) {

            }
            else {
                stmtNodes.add(stmtNode);
            }
        }
        return new BlockStmtNode(stmtNodes, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitBlockStmt(MxstarParser.BlockStmtContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public ASTNode visitVDecStmt(MxstarParser.VDecStmtContext ctx) {
        return new VarDeclStmtNode(
                (VarDeclListNode) visit(ctx.variableDeclaration()),
                new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitExprStmt(MxstarParser.ExprStmtContext ctx) {
        return new ExprStmtNode((ExprNode) visit(ctx.expression()), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitCondStmt(MxstarParser.CondStmtContext ctx) {
        return visit(ctx.conditionStatement());
    }

    @Override
    public ASTNode visitLoopStmt(MxstarParser.LoopStmtContext ctx) {
        return visit(ctx.loopStatement());
    }

    @Override
    public ASTNode visitCtrlStmt(MxstarParser.CtrlStmtContext ctx) {
        return visit(ctx.controlStatement());
    }

    @Override
    public ASTNode visitEmptyStmt(MxstarParser.EmptyStmtContext ctx) {
        return new BlockStmtNode(new ArrayList<>(), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitReturnStmt(MxstarParser.ReturnStmtContext ctx) {
        return new ReturnNode(
                ctx.expression() == null ? null : (ExprNode) visit(ctx.expression()),
                null,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitBreakStmt(MxstarParser.BreakStmtContext ctx) {
        return new BreakNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitContinueStmt(MxstarParser.ContinueStmtContext ctx) {
        return new ContinueNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitConditionStatement(MxstarParser.ConditionStatementContext ctx) {
        StmtNode thenStmt = new BlockStmtNode((StmtNode) visit(ctx.thenStmt));
        StmtNode elseStmt = ctx.elseStmt != null ? new BlockStmtNode((StmtNode) visit(ctx.elseStmt)) : null;
        return new IfStmtNode(
                (ExprNode) visit(ctx.expression()),
                thenStmt,
                elseStmt,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitWhileStmt(MxstarParser.WhileStmtContext ctx) {
        return new WhileStmtNode(
                (ExprNode) visit(ctx.expression()),
                new BlockStmtNode((StmtNode)visit(ctx.statement())),
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitForStmt(MxstarParser.ForStmtContext ctx) {
        return new ForStmtNode(
                ctx.init == null ? null : (ExprNode) visit(ctx.init),
                ctx.cond == null ? null : (ExprNode) visit(ctx.cond),
                ctx.step == null ? null : (ExprNode) visit(ctx.step),
                new BlockStmtNode((StmtNode)visit(ctx.statement())),
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitRejectCreator(MxstarParser.RejectCreatorContext ctx) {
        throw new CompilationError("invalid new creator", new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitSimpleCreator(MxstarParser.SimpleCreatorContext ctx) {
        return new NewExprNode(
                (TypeNode) visit(ctx.nonarrayType()),
                0,
                null,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitClassCreator(MxstarParser.ClassCreatorContext ctx) {
        return new NewExprNode(
                (TypeNode) visit(ctx.nonarrayType()),
                0,
                null,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitArrayCreator(MxstarParser.ArrayCreatorContext ctx) {
        List<ExprNode> exprNodes = new ArrayList<>();
        for (ParserRuleContext expr : ctx.expression()) {
            exprNodes.add((ExprNode) visit(expr));
        }
        return new NewExprNode(
                (TypeNode) visit(ctx.nonarrayType()),
                (ctx.getChildCount() - ctx.expression().size()) / 2,
                exprNodes,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitBinaryExpr(MxstarParser.BinaryExprContext ctx) {
        switch (ctx.op.getText()) {
            case "*": case "/":  case "%":
            case "+": case "-":
            case "<<": case ">>":
            case "<": case ">": case "<=": case ">=":
            case "==": case "!=":
            case "&":
            case "^":
            case "|":
            case "&&":
            case "||":
            case "=":
                break;
            default:
                throw new RuntimeException("unreachable");
        }
        return new BinaryExprNode(
                (ExprNode) visit(ctx.src1),
                (ExprNode) visit(ctx.src2),
                ctx.op.getText(),
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitUnaryExpr(MxstarParser.UnaryExprContext ctx) {
        String op;
        switch (ctx.op.getText()) {
            case "++":
            case "--":
                op = ctx.op.getText() + "i";
                break;
            case "+":
            case "-":
            case "!":
            case "~":
                op = ctx.op.getText();
                break;
            default:
                throw new RuntimeException("unreachable");
        }
        return new UnaryExprNode(
                (ExprNode) visit(ctx.expression()),
                op,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitSufExpr(MxstarParser.SufExprContext ctx) {
        String op;
        switch (ctx.op.getText()) {
            case "++":
            case "--":
                op = "i" + ctx.op.getText();
                break;
            default:
                throw new RuntimeException("unreachable");
        }
        return new UnaryExprNode(
                (ExprNode) visit(ctx.expression()),
                op,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitNewExpr(MxstarParser.NewExprContext ctx) {
        return visit(ctx.creator());
    }

    @Override
    public ASTNode visitBracketExpr(MxstarParser.BracketExprContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public ASTNode visitFuncallExpr(MxstarParser.FuncallExprContext ctx) {
        List <ExprNode> parameters = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (ParserRuleContext parameter : ctx.parameterList().expression()) {
                parameters.add((ExprNode) visit(parameter));
            }
        }
        return new FuncCallExprNode(
                (ExprNode) visit(ctx.expression()),
                parameters,
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitParameterList(MxstarParser.ParameterListContext ctx) {
        throw new RuntimeException("unreachable");
    }

    @Override
    public ASTNode visitSubscriptExpr(MxstarParser.SubscriptExprContext ctx) {
        return new ArrayIndexNode(
                (ExprNode) visit(ctx.name),
                (ExprNode) visit(ctx.index),
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitThisExpr(MxstarParser.ThisExprContext ctx) {
        return new ThisExprNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitMemaccessExpr(MxstarParser.MemaccessExprContext ctx) {
        return new ClassMemberNode(
                (ExprNode) visit(ctx.expression()),
                ctx.Identifier().getText(),
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitSimpleType(MxstarParser.SimpleTypeContext ctx) {
        return visit(ctx.nonarrayType());
    }

    @Override
    public ASTNode visitArrayType(MxstarParser.ArrayTypeContext ctx) {
        return new ArrayTypeNode((TypeNode) visit(ctx.type()), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitLiteral(MxstarParser.LiteralContext ctx) {
        return visit(ctx.constant());
    }

    @Override
    public ASTNode visitIntLiteral(MxstarParser.IntLiteralContext ctx) {
        return new IntLiteralNode(Long.parseLong(ctx.IntConstant().getText()), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitStrLiteral(MxstarParser.StrLiteralContext ctx) {
        return new StringLiteralNode(StringParser.parse(ctx.StringConstant().getText().substring(1, ctx.StringConstant().getText().length() - 1), new Position(ctx.getStart())), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitBoolLiteral(MxstarParser.BoolLiteralContext ctx) {
        return new BoolLiteralNode(
                Boolean.parseBoolean(ctx.BoolConstant().getText()),
                new Position(ctx.getStart())
        );
    }

    @Override
    public ASTNode visitNullLiteral(MxstarParser.NullLiteralContext ctx) {
        return new NullLiteralNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitReturnType(MxstarParser.ReturnTypeContext ctx) {
        return ctx.Void() == null
                ? visit(ctx.type())
                : new VoidTypeNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitTypeBool(MxstarParser.TypeBoolContext ctx) {
        return new BoolTypeNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitTypeInt(MxstarParser.TypeIntContext ctx) {
        return new IntTypeNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitTypeString(MxstarParser.TypeStringContext ctx) {
        return new StringTypeNode(new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitTypeIdentifier(MxstarParser.TypeIdentifierContext ctx) {
        return new ClassTypeNode(ctx.getText(), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitIdentifier(MxstarParser.IdentifierContext ctx) {
        return new IDExprNode(ctx.Identifier().getText(), new Position(ctx.getStart()));
    }


}
