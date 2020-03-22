package Ornn.AST;

import Ornn.semantic.Symbol;
import Ornn.util.Position;

public class ClassMemberNode extends ExprNode {
    private ExprNode expr;
    private String identifier;
    private Symbol symbol;
    public ClassMemberNode(ExprNode expr, String identifier, Position position) {
        super(position);
        this.expr = expr;
        this.identifier = identifier;
    }
    public ExprNode getExpr() {
        return expr;
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
