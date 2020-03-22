package Ornn.AST;

import Ornn.util.Position;

public class ClassTypeNode extends TypeNode {

    public ClassTypeNode(String identifier, Position position) {
        super(identifier, position);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
