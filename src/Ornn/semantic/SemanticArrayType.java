package Ornn.semantic;

import Ornn.util.CompilationError;
import Ornn.AST.util.Position;


public class SemanticArrayType implements SemanticType {
    private SemanticType baseType;
    private int dimension;
    public SemanticArrayType(SemanticType baseType, int dimension) {
        this.baseType = baseType;
        this.dimension = dimension;
    }
    @Override
    public String getTypeName() {
        return baseType.getTypeName() + "[]".repeat(dimension);
    }

    @Override
    public void compatible(SemanticType type, Position position) {
        if (type instanceof NullType) {

        } else if (type instanceof SemanticArrayType) {
            baseType.compatible(((SemanticArrayType) type).getBaseType(), position);
            if (dimension != ((SemanticArrayType) type).getDimension()) {
                throw new CompilationError("array assigned with different dimensions", position);
            }
        } else {
            throw new CompilationError("operation between array and non-array", position);
        }
    }

    @Override
    public void equable(SemanticType type, Position position) {
        if (type instanceof NullType) {

        } else if (type instanceof SemanticArrayType) {
            throw new CompilationError("strangely, comparision between arrays are not allowed", position);
        } else {
            throw new CompilationError("comparision between array and non-array", position);
        }
    }

    public SemanticType getBaseType() {
        return baseType;
    }

    public int getDimension() {
        return dimension;
    }
}
