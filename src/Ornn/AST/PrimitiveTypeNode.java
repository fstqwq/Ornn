package Ornn.AST;

import Ornn.util.Position;

public abstract class PrimitiveTypeNode extends TypeNode {
    public PrimitiveTypeNode(String typeIdentifier, Position position) {
        super(typeIdentifier, position);
    }
}
