package Ornn.AST;

import Ornn.util.Position;

public class IntLiteralNode extends ExprNode {
    private int value;
    public IntLiteralNode(int value, Position position) {
        super(position);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
