package Ornn.AST;

import Ornn.AST.util.Position;

import java.util.List;

public class FuncCallExprNode extends ExprNode {
    private ExprNode functionNode;
    private List<ExprNode> parameterList;

    public FuncCallExprNode(ExprNode functionNode, List<ExprNode> parameterList, Position position) {
        super(position);
        this.functionNode = functionNode;
        this.parameterList = parameterList;
    }

    public ExprNode getFunctionNode() {
        return functionNode;
    }

    public List<ExprNode> getParameterList() {
        return parameterList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
