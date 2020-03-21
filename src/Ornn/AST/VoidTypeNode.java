package Ornn.AST;

import Ornn.util.Position;

public class VoidTypeNode extends ASTNode {
    public VoidTypeNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}

