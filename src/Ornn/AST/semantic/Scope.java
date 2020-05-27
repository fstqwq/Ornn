package Ornn.AST.semantic;


import Ornn.AST.util.Position;

public interface Scope {
    Scope getEnclosingScope();

    void defineVariable(VariableSymbol symbol);

    void defineFunction(FunctionSymbol symbol);

    void defineClass(ClassSymbol symbol);

    Symbol resolveSymbol(String identifier, Position position);
}
