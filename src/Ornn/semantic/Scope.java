package Ornn.semantic;


import Ornn.AST.TypeNode;
import Ornn.util.Position;

public interface Scope {
    Scope getEnclosingScope();

    void defineVariable(VariableSymbol symbol);

    void defineFunction(FunctionSymbol symbol);

    void defineClass(ClassSymbol symbol);

    Symbol resolveSymbol(String identifier, Position position);
}
