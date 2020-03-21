package Ornn.semantic;


import Ornn.util.Position;

public interface Scope {
    String getScopeName();

    Scope getEnclosingScope();

    void defineVariable(VariableSymbol symbol);

    void defineFunction(FunctionSymbol symbol);

    void defineClass(ClassSymbol symbol);

    Symbol resolveSymbol(String identifier, Position position);
}
