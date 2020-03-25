package Ornn.AST;

import Ornn.util.Position;

public class UnaryExprNode extends ExprNode {
    private Op op;
    public enum Op {
        PRE_INC, PRE_DEC, SUF_INC, SUF_DEC, POS, NEG, LNOT, NOT
    }
    private ExprNode expr;
    public UnaryExprNode(ExprNode expr, Op op, Position position) {
        super(position);
        this.expr = expr;
        this.op = op;
    }
    public ExprNode getExpr() {return expr;}

    public void setOp(Op op) {
        this.op = op;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
