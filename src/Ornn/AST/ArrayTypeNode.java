package Ornn.AST;

import Ornn.util.Position;

public class ArrayTypeNode extends ASTNode {
    public ArrayTypeNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
