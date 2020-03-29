package Ornn.AST;

import Ornn.util.Position;

public class BinaryExprNode extends ExprNode {
    private String op;
    private ExprNode lhs, rhs;


    public BinaryExprNode(ExprNode lhs, ExprNode rhs, String op, Position position) {
        super(position);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }
    public ExprNode getLhs() {
        return lhs;
    }

    public ExprNode getRhs() {
        return rhs;
    }

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
