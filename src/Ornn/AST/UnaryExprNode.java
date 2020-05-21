package Ornn.AST;

import Ornn.AST.util.Position;

public class UnaryExprNode extends ExprNode {
    private String op;
    private ExprNode expr;
    public UnaryExprNode(ExprNode expr, String op, Position position) {
        super(position);
        this.expr = expr;
        this.op = op;
    }
    public ExprNode getExpr() {return expr;}

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
