package Ornn.AST;

import Ornn.util.Position;

public class BoolLiteralNode extends ExprNode {
    boolean value;
    public BoolLiteralNode (boolean value, Position position) {
        super(position);
        this.value = value;
    }
    boolean getValue() {
        return value;
    }
    void setValue(boolean value) {
        this.value = value;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
