package Ornn.frontend;

import Ornn.AST.*;
import Ornn.AST.semantic.*;
import Ornn.util.CompilationError;

import static java.lang.Integer.max;


/*
Resolve:
1. scope for variable and function
2. loop for break/continue
3. function for return
4. class for this
 */

public class ScopeResolver implements ASTVisitor {
    private ToplevelScope toplevelScope;
    private Scope currentScope;
    private ClassSymbol currentClassSymbol;
    private FunctionSymbol currentFunctionSymbol;
    private Loop currentLoop;

    public ScopeResolver(ToplevelScope toplevelScope) {
        this.toplevelScope = toplevelScope;
        currentScope = toplevelScope;
        currentClassSymbol = null;
        currentFunctionSymbol = null;
        currentLoop = null;
    }

    @Override
    public void visit(ProgramNode node) {
        for (DeclNode declNode : node.getDeclNodeList()) {
            declNode.accept(this);
            currentScope = toplevelScope;
            currentClassSymbol = null;
            currentFunctionSymbol = null;
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        FunctionSymbol functionSymbol = (FunctionSymbol) currentScope.resolveSymbol(node.getIdentifier(), node.getPosition());
        currentFunctionSymbol = functionSymbol;
        currentScope = functionSymbol;
        node.getParameterList().forEach(x -> x.accept(this));
        node.getBlock().accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        ClassSymbol classSymbol = node.getClassSymbol();
        currentScope = classSymbol;
        currentClassSymbol = classSymbol;
        for (DeclNode x : node.getVarDeclNodes()) {
            x.accept(this);
            currentScope = classSymbol;
        }
        for (DeclNode x : node.getFuncDeclNodes()) {
            x.accept(this);
            currentScope = classSymbol;
        }
    }

    @Override
    public void visit(VarDeclNode node) {
        if (node.getExpr() != null) {
            node.getExpr().accept(this);
        }
        SemanticType type = toplevelScope.resolveType(node.getType());
        VariableSymbol variableSymbol = new VariableSymbol(node.getIdentifier(), type, node);
        currentScope.defineVariable(variableSymbol);
        variableSymbol.setScope(currentScope);
        node.setVariableSymbol(variableSymbol);
        node.setTypeAfterResolve(type);
    }

    @Override
    public void visit(BlockStmtNode node) {
        LocalScope localScope = new LocalScope(currentScope);
        currentScope = localScope;
        node.getStmtList().forEach(x ->{{
            x.accept(this);
            currentScope = localScope;
        }});
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
        Loop enclosingLoop = currentLoop;
        currentLoop = node;
        currentLoop.setLoopDepth(0);
        node.getExpr().accept(this);
        node.getStmt().accept(this);
        if (enclosingLoop != null) enclosingLoop.setLoopDepth(max(enclosingLoop.getLoopDepth(), currentLoop.getLoopDepth() + 1));
        currentLoop = enclosingLoop;
    }

    @Override
    public void visit(ForStmtNode node) {
        Loop enclosingLoop = currentLoop;
        currentLoop = node;
        currentLoop.setLoopDepth(0);
        if (node.getInit() != null) node.getInit().accept(this);
        if (node.getCond() != null) node.getCond().accept(this);
        if (node.getStep() != null) node.getStep().accept(this);
        node.getStmt().accept(this);
        if (enclosingLoop != null) enclosingLoop.setLoopDepth(max(enclosingLoop.getLoopDepth(), currentLoop.getLoopDepth() + 1));
        currentLoop = enclosingLoop;
    }

    @Override
    public void visit(ReturnNode node) {
        if (currentFunctionSymbol == null) {
            throw new CompilationError("nothing to return", node.getPosition());
        }
        node.setFunctionSymbol(currentFunctionSymbol);
        if (node.getExpr() != null) {
            node.getExpr().accept(this);
        }
    }

    @Override
    public void visit(BreakNode node) {
        if (currentLoop == null) {
            throw new CompilationError("nothing to break", node.getPosition());
        }
        node.setLoop(currentLoop);
    }

    @Override
    public void visit(ContinueNode node) {
        if (currentLoop == null) {
            throw new CompilationError("nothing to continue", node.getPosition());
        }
        node.setLoop(currentLoop);
    }

    @Override
    public void visit(IDExprNode node) {
        Symbol symbol = currentScope.resolveSymbol(node.getIdentifier(), node.getPosition());
        node.setVariableSymbol(symbol);
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
    }

    @Override
    public void visit(ClassMemberNode node) {
        node.getExpr().accept(this);
        /* Leave the ID alone. It should be checked in semantic stage. */
    }

    @Override
    public void visit(FuncCallExprNode node) {
        node.getFunctionNode().accept(this);
        node.getParameterList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(NewExprNode node) {
        SemanticType type = toplevelScope.resolveType(node.getBaseType());
        node.setBaseTypeAfterResolve(type);
        node.getExprNodeList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ThisExprNode node) {
        if (currentClassSymbol == null) {
            throw new CompilationError("nothing to refer to this", node.getPosition());
        }
        node.setScope(currentClassSymbol);
    }

    @Override
    public void visit(UnaryExprNode node) {
        node.getExpr().accept(this);
    }

    /* These cuties are lawful good */
    @Override public void visit(IntLiteralNode node) {}
    @Override public void visit(BoolLiteralNode node) {}
    @Override public void visit(NullLiteralNode node) {}
    @Override public void visit(StringLiteralNode node) {}
    @Override public void visit(ArrayTypeNode node) {}
    @Override public void visit(ClassTypeNode node) {}
    @Override public void visit(BoolTypeNode node) {}
    @Override public void visit(IntTypeNode node) {}
    @Override public void visit(VoidTypeNode node) {}
    @Override public void visit(StringTypeNode node) {}
}
