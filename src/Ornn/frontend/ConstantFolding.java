package Ornn.frontend;

import Ornn.AST.*;
import Ornn.semantic.PrimitiveTypeSymbol;
import Ornn.semantic.SemanticArrayType;
import Ornn.semantic.Symbol;
import Ornn.semantic.VariableSymbol;
import Ornn.util.CompilationError;

import java.util.HashMap;
import java.util.HashSet;

import static Ornn.frontend.ToplevelScopeBuilder.*;


/*
    Why we do constant folding in the frontend?
    0. it's coooooooooooooooooooooooooool

    1. It's easy and fast
    2. strings are able to fold quickly at HIR stage
 */

public class ConstantFolding implements ASTVisitor {

    public ConstantFolding() {}
    HashMap<Symbol, Literal> declMap = new HashMap<>();
    HashSet<Symbol> modified = new HashSet<>();

    boolean collectConst;
    @Override
    public void visit(ProgramNode node) {
        collectConst = true;
        for (DeclNode declNode : node.getDeclNodeList()) {
            declNode.accept(this);
        }
        collectConst = false;
        for (DeclNode declNode : node.getDeclNodeList()) {
            declNode.accept(this);
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        node.getBlock().accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        node.getFuncDeclNodes().forEach(x -> x.accept(this));
        node.getVarDeclNodes().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(VarDeclNode node) {
        if (node.getExpr() != null) {
            node.getExpr().accept(this);
            if (node.getExpr().isPureConstant()
            && !(node.getTypeAfterResolve() instanceof SemanticArrayType)
            && (node.getTypeAfterResolve() instanceof PrimitiveTypeSymbol
                    || node.getTypeAfterResolve().getTypeName().equals("string")
            )) {
                declMap.put(node.getVariableSymbol(), node.getExpr().equivalentConstant);
            }
        }
    }

    @Override
    public void visit(BlockStmtNode node) {
        node.getStmtList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        node.getVarDeclList().getDeclList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(IfStmtNode node) {
        node.getExpr().accept(this);
        node.getThenStmt().accept(this);
        if (node.getElseStmt() != null) {
            node.getElseStmt().accept(this);
        }
    }

    @Override
    public void visit(WhileStmtNode node) {
        node.getExpr().accept(this);
        node.getStmt().accept(this);
    }

    @Override
    public void visit(ForStmtNode node) {
        if (node.getInit() != null) node.getInit().accept(this);
        if (node.getCond() != null) node.getCond().accept(this);
        if (node.getStep() != null) node.getStep().accept(this);
        node.getStmt().accept(this);
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getExpr() != null) {
            node.getExpr().accept(this);
        }
    }

    @Override
    public void visit(BreakNode node) {
    }

    @Override
    public void visit(ContinueNode node) {
    }

    @Override
    public void visit(IDExprNode node) {
        if (!collectConst) {
            if (declMap.containsKey(node.getVariableSymbol()) && !modified.contains(node.getVariableSymbol())) {
                node.equivalentConstant = declMap.get(node.getVariableSymbol());
            }
        }
    }

    @Override
    public void visit(ArrayIndexNode node) {
        node.getArray().accept(this);
        node.getIndex().accept(this);
    }

    @Override
    public void visit(BinaryExprNode node) {
        node.getLhs().accept(this);
        node.getRhs().accept(this);
        ExprNode lhs = node.getLhs();
        ExprNode rhs = node.getRhs();
        if (collectConst) {
            if (node.getOp().equals("=")) {
                if (lhs instanceof IDExprNode) {
                    modified.add(((IDExprNode) lhs).getVariableSymbol());
                }
            }
        }
        else if (lhs.isPureConstant() && rhs.isPureConstant()) {
            // even shortcut circuits are acceptable
            try {
                switch (node.getOp()) {
                    case "*":
                        node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() * rhs.equivalentConstant.getInt(), node.getPosition());
                        break;
                    case "/":
                        node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() / rhs.equivalentConstant.getInt(), node.getPosition());
                        break;
                    case "%":
                        node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() % rhs.equivalentConstant.getInt(), node.getPosition());
                        break;
                    case "-":
                        node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() - rhs.equivalentConstant.getInt(), node.getPosition());
                        break;
                    case "<<":
                        node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() << rhs.equivalentConstant.getInt(), node.getPosition());
                        break;
                    case ">>":
                        node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() >> rhs.equivalentConstant.getInt(), node.getPosition());
                        break;
                    case "&":
                        if (lhs.getType().equals(Bool)) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getBool() & rhs.equivalentConstant.getBool(), node.getPosition());
                        } else {
                            node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() & rhs.equivalentConstant.getInt(), node.getPosition());
                        }
                        break;
                    case "^":
                        if (lhs.getType().equals(Bool)) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getBool() ^ rhs.equivalentConstant.getBool(), node.getPosition());
                        } else {
                            node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() ^ rhs.equivalentConstant.getInt(), node.getPosition());
                        }
                        break;
                    case "|":
                        if (lhs.getType().equals(Bool)) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getBool() | rhs.equivalentConstant.getBool(), node.getPosition());
                        } else {
                            node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() | rhs.equivalentConstant.getInt(), node.getPosition());
                        }
                        break;
                    case "&&":
                        node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getBool() & rhs.equivalentConstant.getBool(), node.getPosition());
                        break;
                    case "||":
                        node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getBool() | rhs.equivalentConstant.getBool(), node.getPosition());
                        break;
                    case "+":
                        if (lhs.isInt()) {
                            node.equivalentConstant = new IntLiteralNode(lhs.equivalentConstant.getInt() + rhs.equivalentConstant.getInt(), node.getPosition());
                        } else {
                            node.equivalentConstant = new StringLiteralNode(lhs.equivalentConstant.getStr() + rhs.equivalentConstant.getStr(), node.getPosition());
                        }
                        break;
                    case "<":
                        if (lhs.isInt()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getInt() < rhs.equivalentConstant.getInt(), node.getPosition());
                        } else {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getStr().compareTo(rhs.equivalentConstant.getStr()) < 0, node.getPosition());
                        }
                        break;
                    case ">":
                        if (lhs.isInt()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getInt() > rhs.equivalentConstant.getInt(), node.getPosition());
                        } else {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getStr().compareTo(rhs.equivalentConstant.getStr()) > 0, node.getPosition());
                        }
                        break;
                    case "<=":
                        if (lhs.isInt()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getInt() <= rhs.equivalentConstant.getInt(), node.getPosition());
                        } else {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getStr().compareTo(rhs.equivalentConstant.getStr()) <= 0, node.getPosition());
                        }
                        break;
                    case ">=":
                        if (lhs.isInt()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getInt() >= rhs.equivalentConstant.getInt(), node.getPosition());
                        } else {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getStr().compareTo(rhs.equivalentConstant.getStr()) >= 0, node.getPosition());
                        }
                        break;
                    case "==":
                        if (lhs.isInt()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getInt() == rhs.equivalentConstant.getInt(), node.getPosition());
                        } else if (lhs.isString()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getStr().compareTo(rhs.equivalentConstant.getStr()) == 0, node.getPosition());
                        } else if (lhs.isBool()){
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getBool() == rhs.equivalentConstant.getBool(), node.getPosition());
                        } else {
                            // null == null
                            node.equivalentConstant = new BoolLiteralNode(true, node.getPosition());
                        }
                        break;
                    case "!=":
                        if (lhs.isInt()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getInt() != rhs.equivalentConstant.getInt(), node.getPosition());
                        } else if (lhs.isString()) {
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getStr().compareTo(rhs.equivalentConstant.getStr()) != 0, node.getPosition());
                        } else if (lhs.isBool()){
                            node.equivalentConstant = new BoolLiteralNode(lhs.equivalentConstant.getBool() != rhs.equivalentConstant.getBool(), node.getPosition());
                        } else {
                            // null != null
                            node.equivalentConstant = new BoolLiteralNode(false, node.getPosition());
                        }
                        break;
                    default:
                        throw new CompilationError("unknown error", node.getPosition());
                }
            } catch (Exception err) {
                System.err.println("warning: division by zero " + node.getPosition());
                node.equivalentConstant = new IntLiteralNode(-1, node.getPosition());
            }
        }
    }

    @Override
    public void visit(UnaryExprNode node) {
        node.getExpr().accept(this);
        if (node.getExpr().isPureConstant()) {
            switch (node.getOp()) {
                case "++i":
                case "--i":
                case "i++":
                case "i--":
                    if (node.getExpr() instanceof IDExprNode) {
                        modified.add(((IDExprNode) node.getExpr()).getVariableSymbol());
                    }
                    break;
                case "+":
                    break;
                case "-":
                    node.equivalentConstant = new IntLiteralNode(-node.getExpr().equivalentConstant.getInt(), node.getPosition());
                    break;
                case "~":
                    node.equivalentConstant = new IntLiteralNode(~((int)node.getExpr().equivalentConstant.getInt()), node.getPosition());
                    break;
                case "!":
                    node.equivalentConstant = new BoolLiteralNode(!node.getExpr().equivalentConstant.getBool(), node.getPosition());
                    break;
                default:
                    throw new CompilationError("unknown unary operator", node.getPosition());
            }
        }
    }

    @Override
    public void visit(ClassMemberNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(FuncCallExprNode node) {
        node.getFunctionNode().accept(this);
        node.getParameterList().forEach(x -> x.accept(this));
        if ("toString".equals(node.getFunctionSymbol().getSymbolName())) {
            if (node.getParameterList().get(0).isPureConstant()) {
                node.equivalentConstant = new StringLiteralNode(Long.toString(node.getParameterList().get(0).equivalentConstant.getInt()), node.getPosition());
            }
        }
    }

    @Override
    public void visit(NewExprNode node) {
        node.getExprNodeList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ThisExprNode node) {
    }


    @Override public void visit(IntLiteralNode node) {
        node.equivalentConstant = node;
    }
    @Override public void visit(BoolLiteralNode node) {
        node.equivalentConstant = node;
    }
    @Override public void visit(NullLiteralNode node) {
        node.equivalentConstant = node;
    }
    @Override public void visit(StringLiteralNode node) {
        node.equivalentConstant = node;
    }

    @Override public void visit(ArrayTypeNode node) {}
    @Override public void visit(ClassTypeNode node) {}
    @Override public void visit(BoolTypeNode node) {}
    @Override public void visit(IntTypeNode node) {}
    @Override public void visit(VoidTypeNode node) {}
    @Override public void visit(StringTypeNode node) {}
}
