package Ornn.semantic;

import Ornn.AST.ASTNode;
import Ornn.AST.FuncDeclNode;
import Ornn.AST.TypeNode;
import Ornn.AST.VarDeclNode;
import Ornn.util.CompilationError;
import Ornn.util.Position;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends Symbol implements Scope {
    private Scope enclosingScope;
    private Map<String, VariableSymbol> arguments;

    public FunctionSymbol(String name, Type returnType, FuncDeclNode funcDeclNode, Scope enclosingScope) {
        super(name, returnType, funcDeclNode);
        this.enclosingScope = enclosingScope;
        arguments = new LinkedHashMap<>();
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
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefineNode().getPosition());
        } else {
            arguments.put(symbol.getSymbolName(), symbol);
        }
        symbol.setScope(this);
    }

    @Override
    public void defineClass(ClassSymbol symbol) {

    }

    public Map<String, VariableSymbol> getArguments() {
        return arguments;
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
