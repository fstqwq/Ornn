package Ornn.AST;

import Ornn.util.Position;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclNode extends ASTNode {
    String Identifier;
    List<VarDeclNode> varDeclNodes;
    List<FuncDeclNode> funcDeclNodes;

    public ClassDeclNode(String Identifier, Position position) {
        super(position);
        this.Identifier = Identifier;
        this.varDeclNodes = new ArrayList<>();
        this.funcDeclNodes = new ArrayList<>();
    }

    public ClassDeclNode(String Identifier, List<VarDeclNode> varDeclNodes, List<FuncDeclNode> funcDeclNodes, Position position) {
        super(position);
        this.Identifier = Identifier;
        this.varDeclNodes = varDeclNodes;
        this.funcDeclNodes = funcDeclNodes;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void addVarDecl(VarDeclNode varDeclNode) {
        varDeclNodes.add(varDeclNode);
    }

    public void addFuncDecl(FuncDeclNode funcDeclNode) {
        funcDeclNodes.add(funcDeclNode);
    }

    public List<FuncDeclNode> getFuncDeclNodes() {
        return funcDeclNodes;
    }

    public List<VarDeclNode> getVarDeclNodes() {
        return varDeclNodes;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
