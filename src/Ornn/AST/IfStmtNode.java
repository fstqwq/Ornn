package Ornn.AST;

import Ornn.util.Position;

public class IfStmtNode extends StmtNode {
    private ExprNode expr;
    private StmtNode thenStmt;
    private StmtNode elseStmt;

    public IfStmtNode(ExprNode expr, StmtNode thenStmt, StmtNode elseStmt, Position position) {
        super(position);
        this.expr = expr;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public StmtNode getThenStmt() {
        return thenStmt;
    }

    public StmtNode getElseStmt() {
        return elseStmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
