package Ornn.AST;

import Ornn.util.Position;

public class ClassDeclNode extends ASTNode {
    public ClassDeclNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
