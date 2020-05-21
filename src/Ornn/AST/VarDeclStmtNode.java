package Ornn.AST;

import Ornn.AST.util.Position;

public class VarDeclStmtNode extends StmtNode {
    private VarDeclListNode varDeclList;
    public VarDeclStmtNode(VarDeclListNode varDeclList, Position position) {
        super(position);
        this.varDeclList = varDeclList;
    }

    public VarDeclListNode getVarDeclList() {
        return varDeclList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
