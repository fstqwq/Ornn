package Ornn.AST;

import Ornn.util.Position;

public abstract class ASTNode {
    private Position position;
    public ASTNode(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return position;
    }
    abstract public void accept(ASTVisitor visitor);
}
