package Ornn.AST;

import Ornn.util.Position;

import java.util.ArrayList;
import java.util.List;

public class BlockStmtNode extends StmtNode {
    private List<StmtNode> stmtList;

    public BlockStmtNode(List<StmtNode> stmtList, Position position) {
        super(position);
        this.stmtList = stmtList;
    }

    public BlockStmtNode(StmtNode stmt) {
        super(stmt.getPosition());
        if (stmt instanceof BlockStmtNode) {
            stmtList = new ArrayList<>(((BlockStmtNode) stmt).getStmtList());
        } else {
            this.stmtList = new ArrayList<>() {{add(stmt);}};
        }
    }

    public List<StmtNode> getStmtList() {
        return stmtList;
    }

    public void setStmtList(List<StmtNode> stmtList) {
        this.stmtList = stmtList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
