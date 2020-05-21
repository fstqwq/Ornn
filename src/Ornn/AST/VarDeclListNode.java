package Ornn.AST;

import Ornn.AST.util.Position;

import java.util.ArrayList;
import java.util.List;

public class VarDeclListNode extends DeclNode {
    private List<VarDeclNode> declList;
    public VarDeclListNode(Position position) {
        super(position);
        this.declList = new ArrayList<>();
    }
    public VarDeclListNode(List<VarDeclNode> declNodeList, Position position) {
        super(position);
        this.declList = declNodeList;
    }
    public List<VarDeclNode> getDeclList() {
        return declList;
    }
    public void setType(TypeNode type) {
        for (VarDeclNode decl : declList) {
            decl.setType(type);
        }
    }
    public void addDecl(VarDeclNode varDeclNode) {
        declList.add(varDeclNode);
    }
    @Override
    public void accept(ASTVisitor visitor) {

    }
}
