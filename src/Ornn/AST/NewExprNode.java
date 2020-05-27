package Ornn.AST;

import Ornn.AST.semantic.SemanticType;
import Ornn.AST.util.Position;

import java.util.ArrayList;
import java.util.List;

public class NewExprNode extends ExprNode {
    private TypeNode baseType;
    private SemanticType typeAfterResolve;
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

    public SemanticType getBaseTypeAfterResolve() {
        return typeAfterResolve;
    }

    public void setBaseTypeAfterResolve(SemanticType baseTypeAfterResolve) {
        this.typeAfterResolve = baseTypeAfterResolve;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
