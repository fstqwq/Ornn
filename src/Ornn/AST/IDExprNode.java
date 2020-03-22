package Ornn.AST;

import Ornn.semantic.Symbol;
import Ornn.util.Position;

public class IDExprNode extends ExprNode {
    private String identifier;
    private Symbol symbol;

    public IDExprNode(String identifier, Position position) {
        super(position);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
