package Ornn.semantic;

import Ornn.AST.TypeNode;
import Ornn.util.CompilationError;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class NameScope implements Scope {
    protected Map<String, Symbol> symbolMap = new LinkedHashMap<>();
    private Scope enclosingScope;

    NameScope(Scope enclosingScope) {
        this.enclosingScope = enclosingScope;
        symbolMap = new LinkedHashMap<>();
    }

    @Override
    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    @Override
    public void defineVariable(VariableSymbol symbol) {
        if (symbolMap.containsKey(symbol.getSymbolName()))
            throw new CompilationError("duplicate identifier " + symbol.getSymbolName(), symbol.getDefineNode().getPosition());
        symbolMap.put(symbol.getSymbolName(), symbol);
        symbol.setScope(this);
    }

    @Override
    public void defineFunction(FunctionSymbol symbol) {
        if (symbolMap.containsKey(symbol.getSymbolName()))
            throw new CompilationError("duplicate identifier " + symbol.getSymbolName(), symbol.getDefineNode().getPosition());
        symbolMap.put(symbol.getSymbolName(), symbol);
        symbol.setScope(this);
    }
}
