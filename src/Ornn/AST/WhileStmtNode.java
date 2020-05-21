package Ornn.AST;

import Ornn.IR.BasicBlock;
import Ornn.AST.util.Position;

public class WhileStmtNode extends StmtNode implements Loop {
    private ExprNode expr;
    private BlockStmtNode stmt;

    public BasicBlock condBlock;
    public BasicBlock destBlock;

    public WhileStmtNode(ExprNode expr, BlockStmtNode stmt, Position position) {
        super(position);
        this.expr = expr;
        this.stmt = stmt;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public BlockStmtNode getStmt() {
        return stmt;
    }

    @Override
    public BasicBlock getDestBlock() {
        return destBlock;
    }

    @Override
    public BasicBlock getContinueBlock() {
        return condBlock;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
