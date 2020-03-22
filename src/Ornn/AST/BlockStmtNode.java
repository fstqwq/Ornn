package Ornn.AST;

import Ornn.util.Position;

import java.util.ArrayList;
import java.util.List;

public class BlockStmtNode extends StmtNode {
    private List<StmtNode> stmtList;

    public BlockStmtNode(List<StmtNode> stmtNode, Position position) {
        super(position);
        this.stmtList = stmtList;
    }

    public List<StmtNode> getStmtList() {
        return stmtList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
