package Ornn.AST;

import Ornn.AST.util.Position;

public class IfStmtNode extends StmtNode {
    private ExprNode expr;
    private BlockStmtNode thenStmt;
    private BlockStmtNode elseStmt;

    public IfStmtNode(ExprNode expr, BlockStmtNode thenStmt, BlockStmtNode elseStmt, Position position) {
        super(position);
        this.expr = expr;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public BlockStmtNode getThenStmt() {
        return thenStmt;
    }

    public BlockStmtNode getElseStmt() {
        return elseStmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
