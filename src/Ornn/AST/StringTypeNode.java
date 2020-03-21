package Ornn.AST;

import Ornn.util.Position;

public class StringTypeNode extends ASTNode {
    public StringTypeNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}