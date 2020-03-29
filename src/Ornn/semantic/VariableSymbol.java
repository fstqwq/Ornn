package Ornn.semantic;

import Ornn.AST.VarDeclNode;

public class VariableSymbol extends Symbol {
    public VariableSymbol(String name, Type type, VarDeclNode varDeclNode) {
        super(name, type, varDeclNode);
    }
}
