package Ornn.AST;

import Ornn.AST.semantic.SemanticType;
import Ornn.AST.semantic.VariableSymbol;
import Ornn.AST.util.Position;

public class VarDeclNode extends DeclNode {
    private TypeNode type;
    private SemanticType typeAfterResolve;
    private ExprNode expr;
    private String identifier;
    private VariableSymbol variableSymbol;
    private boolean isParameterVariable = false;

    public VarDeclNode(TypeNode type, ExprNode expr, String identifier, Position position) {
        super(position);
        this.type = type;
        this.expr = expr;
        this.identifier = identifier;
    }

    public TypeNode getType() {
        return type;
    }

    public void setType(TypeNode type) {
        this.type = type;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public void setExpr(ExprNode expr)  {
        this.expr = expr;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SemanticType getTypeAfterResolve() {
        return typeAfterResolve;
    }

    public void setTypeAfterResolve(SemanticType typeAfterResolve) {
        this.typeAfterResolve = typeAfterResolve;
    }

    public VariableSymbol getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(VariableSymbol variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    public boolean isParameterVariable() {
        return isParameterVariable;
    }

    public void setParameterVariable() {
        isParameterVariable = true;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
