package Ornn.AST;

import Ornn.AST.semantic.FunctionSymbol;
import Ornn.AST.util.Position;

public class ReturnNode extends StmtNode {
    private ExprNode expr;
    private FunctionSymbol functionSymbol;
    public ReturnNode(ExprNode expr, FunctionSymbol functionSymbol, Position position) {
        super(position);
        this.expr = expr;
        this.functionSymbol = functionSymbol;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public void setExpr(ExprNode expr) {
        this.expr = expr;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
