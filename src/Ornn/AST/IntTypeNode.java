package Ornn.AST;

import Ornn.util.Position;

public class IntTypeNode extends ASTNode {
    public IntTypeNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
