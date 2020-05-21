package Ornn.AST;

import Ornn.AST.util.Position;

public class BreakNode extends StmtNode {
    private Loop loop;
    public BreakNode(Position position) {
        super(position);
    }

    public Loop getLoop() {
        return loop;
    }

    public void setLoop(Loop loop) {
        this.loop = loop;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
