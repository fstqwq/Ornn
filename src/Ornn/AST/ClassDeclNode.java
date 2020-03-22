package Ornn.AST;

import Ornn.util.Position;

public class ClassDeclNode extends ASTNode {
    public ClassDeclNode(Position positon) {
        super(positon);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
