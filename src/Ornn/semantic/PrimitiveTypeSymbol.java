package Ornn.semantic;

import Ornn.util.CompilationError;
import Ornn.util.Position;

public class PrimitiveTypeSymbol extends Symbol implements Type {
    public PrimitiveTypeSymbol(String name) {super(name, null, null);}

    @Override
    public String getTypeName() {
        return super.getSymbolName();
    }

    @Override
    public void compatible(Type type, Position position) {
        if (type.getTypeName().equals("void") || !getTypeName().equals(type.getTypeName())) {
            throw new CompilationError(getTypeName() + "is not assignable by " + type.getTypeName(), position);
        }
    }

    @Override
    public boolean isPrimitiveType() {
        return true;
    }

    @Override
    public boolean isClassType() {
        return false;
    }

    @Override
    public boolean isNullType() {
        return false;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }
}
