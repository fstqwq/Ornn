package Ornn.AST;

import Ornn.util.Position;

public class ArrayTypeNode extends TypeNode {
    private TypeNode baseType;
    private int dimension;
    public ArrayTypeNode(TypeNode baseType, Position position) {
        super(baseType.getTypeIdentifier(), position);
        if (baseType instanceof ArrayTypeNode) {
            this.baseType = ((ArrayTypeNode) baseType).baseType;
            dimension = ((ArrayTypeNode) baseType).dimension + 1;
        } else {
            this.baseType = baseType;
            dimension = 1;
        }
    }

    public int getDimension() {
        return dimension;
    }

    public TypeNode getBaseType(){
        return baseType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
