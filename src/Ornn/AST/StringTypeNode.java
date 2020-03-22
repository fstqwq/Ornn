package Ornn.AST;

import Ornn.util.Position;

public class StringTypeNode extends PrimitiveTypeNode {
    public StringTypeNode(Position position) {
        super("string", position);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
