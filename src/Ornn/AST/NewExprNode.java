package Ornn.AST;

import Ornn.semantic.Type;
import Ornn.util.Position;

import java.util.ArrayList;
import java.util.List;

public class NewExprNode extends ExprNode {
    private TypeNode baseType;
    private Type typeAfterResolve;
    private int dimension;
    private List<ExprNode> exprNodeList;

    public NewExprNode(TypeNode baseType, int dimension, List<ExprNode> exprNodeList, Position position) {
        super(position);
        this.baseType = baseType;
        this.dimension = dimension;
        this.exprNodeList = exprNodeList == null ? new ArrayList<>() : exprNodeList;
    }

    public TypeNode getBaseType() {
        return baseType;
    }

    public int getDimension() {
        return dimension;
    }

    public List<ExprNode> getExprNodeList() {
        return exprNodeList;
    }

    public Type getBaseTypeAfterResolve() {
        return typeAfterResolve;
    }

    public void setBaseTypeAfterResolve(Type baseTypeAfterResolve) {
        this.typeAfterResolve = baseTypeAfterResolve;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
