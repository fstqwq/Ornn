package Ornn.AST;

import Ornn.semantic.FunctionSymbol;
import Ornn.util.Position;

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

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
