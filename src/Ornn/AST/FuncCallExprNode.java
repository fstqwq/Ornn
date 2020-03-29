package Ornn.AST;

import Ornn.util.Position;

import java.util.ArrayList;
import java.util.List;

public class FuncCallExprNode extends ExprNode {
    private ExprNode function;
    private List<ExprNode> parameterList;

    public FuncCallExprNode(ExprNode function, List<ExprNode> parameterList, Position position) {
        super(position);
        this.function = function;
        this.parameterList = parameterList;
    }

    public ExprNode getFunction() {
        return function;
    }

    public List<ExprNode> getParameterList() {
        return parameterList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
