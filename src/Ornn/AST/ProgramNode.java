package Ornn.AST;

import Ornn.AST.util.Position;
import java.util.List;


public class ProgramNode extends ASTNode {

    private List <DeclNode> declNodeList;

    public ProgramNode(List<DeclNode> declNodeList, Position position) {
        super(position);
        this.declNodeList = declNodeList;
    }
    public List<DeclNode> getDeclNodeList() {
        return this.declNodeList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
