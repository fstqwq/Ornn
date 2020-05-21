package Ornn.AST;

import Ornn.AST.util.Position;

public abstract class TypeNode extends ASTNode {
    private String typeIdentifier;

    public TypeNode(String typeIdentifier, Position position) {
        super(position);
        this.typeIdentifier = typeIdentifier;
    }
    public String getTypeIdentifier() {return typeIdentifier;}
}
