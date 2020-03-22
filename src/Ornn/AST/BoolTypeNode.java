package Ornn.AST;

import Ornn.util.Position;

public class BoolTypeNode extends PrimitiveTypeNode {
    public BoolTypeNode(Position position) {
        super("bool", position);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
