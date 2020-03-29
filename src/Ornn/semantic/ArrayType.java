package Ornn.semantic;

import Ornn.util.CompilationError;
import Ornn.util.Position;


public class ArrayType implements Type {
    private Type baseType;
    private int dimension;
    public ArrayType(Type baseType, int dimension) {
        this.baseType = baseType;
        this.dimension = dimension;
    }
    @Override
    public String getTypeName() {
        return baseType.getTypeName() + "[]".repeat(dimension);
    }

    @Override
    public void compatible(Type type, Position position) {
        if (type instanceof NullType) {

        } else if (type instanceof ArrayType) {
            baseType.compatible(((ArrayType) type).getBaseType(), position);
            if (dimension != ((ArrayType) type).getDimension()) {
                throw new CompilationError("array assigned with different dimensions", position);
            }
        } else {
            throw new CompilationError("operation between array and non-array", position);
        }
    }

    @Override
    public void equable(Type type, Position position) {
        if (type instanceof NullType) {

        } else if (type instanceof ArrayType) {
            throw new CompilationError("strangely, comparision between arrays are not allowed", position);
        } else {
            throw new CompilationError("comparision between array and non-array", position);
        }
    }

    public Type getBaseType() {
        return baseType;
    }

    public int getDimension() {
        return dimension;
    }
}
