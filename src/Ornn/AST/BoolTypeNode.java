package Ornn.AST;

import Ornn.util.Position;

public class BoolTypeNode extends ASTNode {
    public BoolTypeNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
