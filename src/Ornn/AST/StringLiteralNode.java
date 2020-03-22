package Ornn.AST;

import Ornn.util.Position;

public class StringLiteralNode extends ExprNode {
    private String value;
    public StringLiteralNode(String value, Position position) {
        super(position);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
