package Ornn.semantic;

import Ornn.AST.VarDeclNode;
import Ornn.util.CompilationError;
import Ornn.util.Position;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends Symbol implements Scope {
    private Scope enclosingScope;
    private Map<String, VariableSymbol>  arguments = new LinkedHashMap<>();

    public FunctionSymbol(String name, Type type, VarDeclNode varDeclNode, Scope enclosingScope) {
        super(name, type, varDeclNode);
        this.enclosingScope = enclosingScope;
    }

    @Override
    public String getScopeName() {
        return super.getSymbolName();
    }

    @Override
    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    @Override
    public void defineFunction(FunctionSymbol symbol) {

    }

    @Override
    public void defineVariable(VariableSymbol symbol) {
        if (arguments.containsKey(symbol.getSymbolName())) {
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefine().getPosition());
        } else {
            arguments.put(symbol.getSymbolName(), symbol);
        }
        symbol.setScope(this);
    }

    @Override
    public void defineClass(ClassSymbol symbol) {

    }

    @Override
    public Symbol resolveSymbol(String identifier, Position position) {
        Symbol symbol = arguments.get(identifier);
        if (symbol != null) return symbol;
        else return enclosingScope.resolveSymbol(identifier, position);
    }

    @Override
    public boolean isFunctionSymbol() {
        return true;
    }
}
