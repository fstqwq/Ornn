package Ornn.AST;

import Ornn.util.Position;

public class ClassTypeNode extends ASTNode {
    public ClassTypeNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
