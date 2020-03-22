package Ornn.AST;

import Ornn.util.Position;

import java.util.List;

public class VarDeclListNode extends DeclNode {
    private List<VarDeclNode> declList;
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
