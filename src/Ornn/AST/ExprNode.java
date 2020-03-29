package Ornn.AST;

import Ornn.semantic.*;
import Ornn.util.Position;

import static Ornn.semantic.TypeCategory.*;


public abstract class ExprNode extends ASTNode {
    private TypeCategory typeCategory;
    private Type type;
    private FunctionSymbol functionSymbol;

    public ExprNode(Position position) {
        super(position);
    }

    public TypeCategory getTypeCategory() {
        return typeCategory;
    }
    public void setTypeCategory(TypeCategory typeCategory) {
        this.typeCategory = typeCategory;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public Type getType() {
        return type;
    }
    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }
    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    public boolean isString() {
        return (typeCategory == LVALUE || typeCategory == RVALUE) && type.getTypeName().equals("string");
    }

    public boolean isInt() {
        return (typeCategory == LVALUE || typeCategory == RVALUE) && type.getTypeName().equals("int");
    }

    public boolean isBool() {
        return (typeCategory == LVALUE || typeCategory == RVALUE) && type.getTypeName().equals("bool");
    }

    public boolean isValue() {
        return typeCategory != CLASS && typeCategory != FUNCTION;
    }

    public boolean isLvalue() {
        return (typeCategory == LVALUE);
    }

    public boolean isFunction() {
        return (typeCategory == FUNCTION);
    }

    public boolean isClassValue() {
        return (isValue() && type instanceof ClassSymbol);
    }

    public boolean isArray() {
        return type instanceof ArrayType;
    }
}
