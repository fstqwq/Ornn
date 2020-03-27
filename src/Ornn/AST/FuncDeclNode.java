package Ornn.AST;

import Ornn.semantic.FunctionSymbol;
import Ornn.semantic.Type;
import Ornn.util.Position;

import java.util.ArrayList;
import java.util.List;

public class FuncDeclNode extends DeclNode {
    private TypeNode type;
    private String identifier;
    private List<VarDeclNode> parameterList;
    private BlockStmtNode blockStmt;
    private FunctionSymbol functionSymbol;

    public FuncDeclNode(TypeNode type, String identifier, List<VarDeclNode> parameterList, BlockStmtNode blockStmt, Position position) {
        super(position);
        this.type = type;
        this.identifier = identifier;
        if (parameterList == null) {
            this.parameterList = new ArrayList<>();
        }
        else {
            this.parameterList = parameterList;
        }
        this.blockStmt = blockStmt;
        this.parameterList.forEach(VarDeclNode::setParameterVariable);
    }

    public String getIdentifier() {
        return identifier;
    }

    public TypeNode getType() {
        return type;
    }

    public List<VarDeclNode> getParameterList() {
        return parameterList;
    }

    public BlockStmtNode getBlock() {
        return blockStmt;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
