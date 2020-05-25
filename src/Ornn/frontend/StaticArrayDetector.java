package Ornn.frontend;

import Ornn.AST.*;
import Ornn.IR.type.ArrayType;
import Ornn.semantic.*;
import Ornn.util.CompilationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static Ornn.frontend.ToplevelScopeBuilder.Bool;

public class StaticArrayDetector implements ASTVisitor {


    HashSet<Symbol> candidate = new HashSet<>();
    HashMap<Symbol, Integer> access = new HashMap<>();
    boolean collectingConstDecl;

    public StaticArrayDetector() {
    }
    @Override
    public void visit(ProgramNode node) {
        collectingConstDecl = true;
        for (DeclNode declNode : node.getDeclNodeList()) {
            declNode.accept(this);
        }
        for (Symbol symbol : candidate) {
            if (access.get(symbol) == 0) { // every usage is irrelevant to the implementation of array
                System.err.println(symbol.getSymbolName() + " is recognized as static array");
                ((SemanticArrayType )symbol.getType()).isStatic = true;
            }
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        if (node.getIdentifier().equals("__init")) return;
        node.getBlock().accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        node.getFuncDeclNodes().forEach(x -> x.accept(this));
        node.getVarDeclNodes().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(VarDeclNode node) {
        if (node.getExpr() != null) node.getExpr().accept(this);
        if (node.getVariableSymbol().isGlobal() && node.getExpr() != null && node.getTypeAfterResolve() instanceof SemanticArrayType) {
            if (node.getExpr() instanceof NewExprNode) {
                NewExprNode newExprNode = (NewExprNode)node.getExpr();
                boolean ok = newExprNode.getExprNodeList().size() == newExprNode.getDimension(); // fully initialized
                for (ExprNode exprNode : newExprNode.getExprNodeList()) {
                    ok &= exprNode.isPureConstant(); // every dimension is constant
                }
                if (ok) {
                    candidate.add(node.getVariableSymbol());
                    access.put(node.getVariableSymbol(), 0);
                    SemanticArrayType semanticArrayType = (SemanticArrayType) node.getTypeAfterResolve();
                    semanticArrayType.dimensionOffsets = new ArrayList<>();
                    for (ExprNode exprNode : newExprNode.getExprNodeList()) {
                        semanticArrayType.dimensionOffsets.add((int) exprNode.equivalentConstant.getInt());
                    }
                    semanticArrayType.dimensionOffsets.add(semanticArrayType.getBaseType() == Bool ? 1 : 4); // last is one
                    for (int i = semanticArrayType.dimensionOffsets.size() - 2; i >= 0; i--) {
                        semanticArrayType.dimensionOffsets.set(i, semanticArrayType.dimensionOffsets.get(i) * semanticArrayType.dimensionOffsets.get(i + 1));
                    }
                }
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
        if (node.getVariableSymbol() != null && node.getVariableSymbol().getType() instanceof SemanticArrayType) {
            access.put(node.getVariableSymbol(), 1 + access.getOrDefault(node.getVariableSymbol() ,0));
        }
    }

    @Override
    public void visit(BinaryExprNode node) {
        node.getLhs().accept(this);
        node.getRhs().accept(this);
    }

    @Override
    public void visit(UnaryExprNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(ClassMemberNode node) {
        node.getExpr().accept(this);
    }


    @Override
    public void visit(ArrayIndexNode node) {
        node.getArray().accept(this);
        if (node.getArray() instanceof IDExprNode) {
            node.from = (VariableSymbol) ((IDExprNode) node.getArray()).getVariableSymbol();
            node.curDim = 1;
        }
        else if (node.getArray() instanceof ArrayIndexNode){
            node.from = ((ArrayIndexNode) node.getArray()).from;
            node.curDim = ((ArrayIndexNode) node.getArray()).curDim + 1;
        } else {
            node.getIndex().accept(this);
            return;
        }
        if (((SemanticArrayType) node.from.getType()).getDimension() == node.curDim) {
            access.put(node.from, access.getOrDefault(node.from, 0) - 1);
        }
        node.getIndex().accept(this);
    }

    @Override
    public void visit(FuncCallExprNode node) {
        node.getFunctionNode().accept(this);
        node.getParameterList().forEach(x -> x.accept(this));
        FunctionSymbol symbol = node.getFunctionSymbol();
        if (symbol.getSymbolName().equals("array_size")) {
            ExprNode exprNode = ((ClassMemberNode) node.getFunctionNode()).getExpr();
            if (exprNode instanceof IDExprNode) {
                Symbol variableSymbol = ((IDExprNode) exprNode).getVariableSymbol();
                access.put(variableSymbol, access.getOrDefault(variableSymbol, 0) - 1);
            } else {
                Symbol variableSymbol = ((ArrayIndexNode) exprNode).from;
                access.put(variableSymbol, access.getOrDefault(variableSymbol, 0) - 1);
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
    }
    @Override public void visit(BoolLiteralNode node) {
    }
    @Override public void visit(NullLiteralNode node) {
    }
    @Override public void visit(StringLiteralNode node) {
    }

    @Override public void visit(ArrayTypeNode node) {}
    @Override public void visit(ClassTypeNode node) {}
    @Override public void visit(BoolTypeNode node) {}
    @Override public void visit(IntTypeNode node) {}
    @Override public void visit(VoidTypeNode node) {}
    @Override public void visit(StringTypeNode node) {}
}
