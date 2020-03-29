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
            throw new CompilationError(type.getTypeName() + " can't be converted to " + getTypeName(), position);
        }
    }

    @Override
    public void equable(Type type, Position position) {
        if (getTypeName().equals("void") || !getTypeName().equals(type.getTypeName())) {
            throw new CompilationError(type.getTypeName() + " can't evaluate with " + getTypeName(), position);
        }
    }
}
