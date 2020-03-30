package Ornn.semantic;

import Ornn.util.CompilationError;
import Ornn.util.Position;

public class NullType implements Type {
    @Override
    public String getTypeName() {
        return "null";
    }

    @Override
    public void compatible(Type type, Position position) {
        throw new CompilationError("null can't be operated", position);
    }

    @Override
    public void equable(Type type, Position position) {
        if (type instanceof PrimitiveTypeSymbol) {
            throw new CompilationError(type.getTypeName() + " can't compare to null", position);
        }
    }
}