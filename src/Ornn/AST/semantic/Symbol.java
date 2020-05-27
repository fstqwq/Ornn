package Ornn.AST.semantic;

import Ornn.AST.ASTNode;

public class Symbol {
    private String name;
    private SemanticType type;
    private Scope scope;
    private ASTNode defineNode;

    public Symbol(String name, SemanticType type, ASTNode defineNode) {
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

    public SemanticType getType() {
        return type;
    }

    public void setType(SemanticType type) {
        this.type = type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
