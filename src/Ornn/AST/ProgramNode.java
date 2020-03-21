package Ornn.AST;

import Ornn.util.Position;
import java.util.List;


public class ProgramNode extends ASTNode {

    private List <DeclNode> declNodeList;

    public boolean hasClassDeclNode = false;

    public ProgramNode(List<DeclNode> declNodeList, Position position, boolean hasClassDeclNode) {
        super(position);
        this.declNodeList = declNodeList;
        this.hasClassDeclNode = hasClassDeclNode;
    }
    public List<DeclNode> getDeclNodeList() {
        return this.declNodeList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
