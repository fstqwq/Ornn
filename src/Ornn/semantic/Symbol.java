package Ornn.semantic;

import Ornn.AST.ASTNode;

public class Symbol {
    private String name;
    private Type type;
    private Scope scope;
    private ASTNode defineNode;

    public Symbol(String name, Type type, ASTNode defineNode) {
        this.name = name;
        this.type = type;
        this.defineNode = defineNode;
    }

    public String getSymbolName() {
        return this.name;
    }

    public ASTNode getDefineNode() {
        return defineNode;
    }

    public Type getType() {
        return type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
