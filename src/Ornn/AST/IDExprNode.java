package Ornn.AST;

import Ornn.semantic.Symbol;
import Ornn.util.Position;

public class IDExprNode extends ExprNode {
    private String identifier;
    private Symbol variableSymbol;

    public IDExprNode(String identifier, Position position) {
        super(position);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Symbol getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(Symbol variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
