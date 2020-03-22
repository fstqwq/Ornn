package Ornn.AST;

import Ornn.util.Position;

public class BinaryExprNode extends ExprNode {
    private Op op;
    private ExprNode lhs, rhs;
    public enum Op {
        MUL, DIV, MOD, ADD, SUB, SHL, SHR, LT, LEQ, GT, GEQ, AND, XOR, OR, LAND, LOR, ASG
    }

    public BinaryExprNode(ExprNode lhs, ExprNode rhs, Op op, Position position) {
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

    public Op getOp() {
        return op;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
