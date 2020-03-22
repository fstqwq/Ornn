package Ornn.AST;

import Ornn.util.Position;

public class WhileStmtNode extends StmtNode implements Loop {
    private ExprNode expr;
    private StmtNode stmt;

    public WhileStmtNode(ExprNode expr, StmtNode stmt, Position position) {
        super(position);
        this.expr = expr;
        this.stmt = stmt;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public StmtNode getStmt() {
        return stmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
