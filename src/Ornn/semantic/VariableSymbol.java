package Ornn.semantic;

import Ornn.AST.VarDeclNode;
import Ornn.IR.operand.ConstInt;
import Ornn.IR.operand.Operand;

public class VariableSymbol extends Symbol {

    public Operand operand;
    public ConstInt index;

    public VariableSymbol(String name, SemanticType type, VarDeclNode varDeclNode) {
        super(name, type, varDeclNode);
    }
    public boolean isMember() {
        return getScope() instanceof ClassSymbol;
    }
    public boolean isGlobal() {
        return getScope() instanceof ToplevelScope;
    }
}
