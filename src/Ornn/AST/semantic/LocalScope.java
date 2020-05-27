package Ornn.AST.semantic;

import Ornn.util.CompilationError;
import Ornn.AST.util.Position;

public class LocalScope extends NameScope {
    public LocalScope(Scope enclosingScope) {
        super(enclosingScope);
    }

    @Override
    public void defineClass(ClassSymbol symbol) {
        throw new CompilationError("not possible to reach here", symbol.getDefineNode().getPosition());
    }

    @Override
    public Symbol resolveSymbol(String identifier, Position position) {
        Symbol symbol = symbolMap.get(identifier);
        if (symbol == null) {
            return getEnclosingScope().resolveSymbol(identifier, position);
        } else {
            return symbol;
        }
    }

}
