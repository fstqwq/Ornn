package Ornn.frontend;

import Ornn.AST.*;
import Ornn.parser.MxstarBaseVisitor;
import Ornn.parser.MxstarParser;
import Ornn.util.CompilationError;
import Ornn.util.Position;
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
                if (decl instanceof VarDeclListNode) declNodeList.addAll(((VarDeclListNode) decl).getDeclList());
                else declNodeList.add((DeclNode) decl);
            }
        }
        return new ProgramNode(declNodeList, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitProgramSection(MxstarParser.ProgramSectionContext ctx) {
        if (null != ctx.functionDeclaration()) return visit(ctx.functionDeclaration());
        if (null != ctx.variableDeclaration()) return visit(ctx.variableDeclaration());
        if (null != ctx.classDeclaration()) return visit(ctx.classDeclaration());
        throw new CompilationError("Internal Error", new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitFunctionDeclaration(MxstarParser.FunctionDeclarationContext ctx) {
        TypeNode type = (TypeNode)visit(ctx.returnType());
        String identifier = ctx.Identifier().getText();
        List<VarDeclNode> varDeclNodes = ctx.parameterDeclarationList() == null
                ? new ArrayList<>()
                : ((VarDeclListNode) visit(ctx.parameterDeclarationList())).getDeclList();
        BlockStmtNode blockStmt = (BlockStmtNode) visit(ctx.block());
        return new FuncDeclNode(type, identifier, varDeclNodes, blockStmt, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitConstructiveFunctionDeclaration(MxstarParser.ConstructiveFunctionDeclarationContext ctx) {
        String identifier = ctx.Identifier().getText();
        BlockStmtNode blockStmt = (BlockStmtNode) visit(ctx.block());
        return new FuncDeclNode(null, identifier, null, blockStmt, new Position(ctx.getStart()));
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
        return new VarDeclNode((TypeNode) visit(ctx.type()),
                                null,
                                ctx.Identifier().getText(),
                                new Position(ctx.getStart()));
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
            classDeclNode.addVarDecl((VarDeclNode) visit(decl));
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
            stmtNodes.add((StmtNode) visit(stmt));
        }
        return new BlockStmtNode(stmtNodes, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitBlockStmt(MxstarParser.BlockStmtContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public ASTNode visitVDecStmt(MxstarParser.VDecStmtContext ctx) {
        return new VarDeclStmtNode((VarDeclListNode) visit(ctx.variableDeclaration()), new Position(ctx.getStart()));
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
        return null;
    }

    @Override
    public ASTNode visitReturnStmt(MxstarParser.ReturnStmtContext ctx) {
        return new ReturnNode((ExprNode) visit(ctx.expression()), null, new Position(ctx.getStart()));
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
        return new IfStmtNode((ExprNode) visit(ctx.expression()), thenStmt, elseStmt, new Position(ctx.getStart()));
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
        throw new CompilationError("Invalid new creator", new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitBinaryExpr(MxstarParser.BinaryExprContext ctx) {
        BinaryExprNode.Op op;
        switch (ctx.op.getText()) {
            case "*":
                op = BinaryExprNode.Op.MUL;
                break;
            case "/":
                op = BinaryExprNode.Op.DIV;
                break;
            case "%":
                op = BinaryExprNode.Op.MOD;
                break;
            case "+":
                op = BinaryExprNode.Op.ADD;
                break;
            case "-":
                op = BinaryExprNode.Op.SUB;
                break;
            case "<<":
                op = BinaryExprNode.Op.SHL;
                break;
            case ">>":
                op = BinaryExprNode.Op.SHR;
                break;
            case "<":
                op = BinaryExprNode.Op.LT;
                break;
            case ">":
                op = BinaryExprNode.Op.GT;
                break;
            case "<=":
                op = BinaryExprNode.Op.LEQ;
                break;
            case ">=":
                op = BinaryExprNode.Op.GEQ;
                break;
            case "==":
                op = BinaryExprNode.Op.EQ;
                break;
            case "!=":
                op = BinaryExprNode.Op.NEQ;
                break;
            case "&":
                op = BinaryExprNode.Op.AND;
                break;
            case "^":
                op = BinaryExprNode.Op.XOR;
                break;
            case "|":
                op = BinaryExprNode.Op.OR;
                break;
            case "&&":
                op = BinaryExprNode.Op.LAND;
                break;
            case "||":
                op = BinaryExprNode.Op.LOR;
                break;
            case "=":
                op = BinaryExprNode.Op.ASG;
                break;
            default:
                throw new CompilationError("Unknown binary operator", new Position(ctx.getStart()));
        }
        return new BinaryExprNode((ExprNode) visit(ctx.src1), (ExprNode) visit(ctx.src2), op, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitUnaryExpr(MxstarParser.UnaryExprContext ctx) {
        UnaryExprNode.Op op;
        switch (ctx.op.getText()) {
            case "++":
                op = UnaryExprNode.Op.PRE_INC;
                break;
            case "--":
                op = UnaryExprNode.Op.PRE_DEC;
                break;
            case "+":
                op = UnaryExprNode.Op.POS;
                break;
            case "-":
                op = UnaryExprNode.Op.NEG;
                break;
            case "!":
                op = UnaryExprNode.Op.LNOT;
                break;
            case "~":
                op = UnaryExprNode.Op.NOT;
                break;
            default:
                throw new CompilationError("Unknown unary operator", new Position(ctx.getStart()));
        }
        return new UnaryExprNode((ExprNode) visit(ctx.expression()), op, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitSufExpr(MxstarParser.SufExprContext ctx) {
        UnaryExprNode.Op op;
        switch (ctx.op.getText()) {
            case "++":
                op = UnaryExprNode.Op.SUF_INC;
                break;
            case "--":
                op = UnaryExprNode.Op.SUF_DEC;
                break;
            default:
                throw new CompilationError("Unknown unary operator", new Position(ctx.getStart()));
        }
        return new UnaryExprNode((ExprNode) visit(ctx.expression()), op, new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitNewExpr(MxstarParser.NewExprContext ctx) {
        return visit(ctx.creator());
    }

    @Override
    public ASTNode visitIntLiteral(MxstarParser.IntLiteralContext ctx) {
        return new IntLiteralNode(Integer.parseInt(ctx.IntConstant().getText()), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitStrLiteral(MxstarParser.StrLiteralContext ctx) {
        return new StringLiteralNode(ctx.StringConstant().getText(), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitBoolLiteral(MxstarParser.BoolLiteralContext ctx) {
        return new BoolLiteralNode(Boolean.parseBoolean(ctx.BoolConstant().getText()), new Position(ctx.getStart()));
    }

    @Override
    public ASTNode visitNullLiteral(MxstarParser.NullLiteralContext ctx) {
        return new NullLiteralNode(new Position(ctx.getStart()));
    }

}
