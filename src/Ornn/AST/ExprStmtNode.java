package Ornn.AST;

import Ornn.util.Position;

public class ExprStmtNode extends StmtNode {
    ExprNode expr;
    public ExprStmtNode (ExprNode expr, Position position) {
        super(position);
        this.expr = expr;
    }
    public ExprNode getExpr() {
        return expr;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
