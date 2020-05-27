package Ornn.AST.semantic;

import Ornn.util.CompilationError;
import Ornn.AST.util.Position;

public class PrimitiveTypeSymbol extends Symbol implements SemanticType {
    public PrimitiveTypeSymbol(String name) {super(name, null, null);}

    @Override
    public String getTypeName() {
        return super.getSymbolName();
    }

    @Override
    public void compatible(SemanticType type, Position position) {
        if (type.getTypeName().equals("void") || !getTypeName().equals(type.getTypeName())) {
            throw new CompilationError(type.getTypeName() + " can't be converted to " + getTypeName(), position);
        }
    }

    @Override
    public void equable(SemanticType type, Position position) {
        if (getTypeName().equals("void") || !getTypeName().equals(type.getTypeName())) {
            throw new CompilationError(type.getTypeName() + " can't evaluate with " + getTypeName(), position);
        }
    }
}
