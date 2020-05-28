package Ornn.AST;

import Ornn.IR.BasicBlock;
import Ornn.AST.util.Position;

public class ForStmtNode extends StmtNode implements Loop {
    private ExprNode init;
    private ExprNode cond;
    private ExprNode step;
    private BlockStmtNode stmt;

    public BasicBlock stepBlock;
    public BasicBlock destBlock;

    public ForStmtNode(ExprNode init, ExprNode cond, ExprNode step, BlockStmtNode stmt, Position position) {
        super(position);
        this.init = init;
        this.cond = cond;
        this.step = step;
        this.stmt = stmt;
    }

    public ExprNode getInit() {
        return init;
    }

    public void setCond(ExprNode cond) {
        this.cond = cond;
    }

    public ExprNode getCond() {
        return cond;
    }

    public ExprNode getStep() {
        return step;
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
        return stepBlock;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public int loopDepth;

    @Override
    public void setLoopDepth(int depth) {
        loopDepth = depth;
    }

    @Override
    public int getLoopDepth() {
        return loopDepth;
    }
}
