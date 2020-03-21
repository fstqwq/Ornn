package Ornn.AST;

import Ornn.semantic.FunctionSymbol;
import Ornn.semantic.Type;
import Ornn.util.Position;

public abstract class ExprNode extends ASTNode {
    private Category category;
    public enum Category {
        LVALUE, RVALUE, CLASS, FUNCTION
    }
    private Type type;
    private FunctionSymbol functionSymbol;

    public ExprNode(Position position) {
        super(position);
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
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

    public boolean isAssignableInteger() {
        return category == Category.LVALUE && type.getTypeName().equals("int");
    }

    public boolean isString() {
        return (category == Category.LVALUE || category == Category.RVALUE) && type.getTypeName().equals("string");
    }

    public boolean isInteger() {
        return (category == Category.LVALUE || category == Category.RVALUE) && type.getTypeName().equals("int");
    }

    public boolean isBoolean() {
        return (category == Category.LVALUE || category == Category.RVALUE) && type.getTypeName().equals("bool");
    }

    public boolean isAssignable() {
        return (category == Category.LVALUE);
    }

    public boolean isCallable() {
        return (category == Category.FUNCTION);
    }

    public boolean isAccessable() {
        return (isValue() && type.isClassType());
    }

    public boolean isNullable() {
        return (category == Category.LVALUE && (type.isClassType() || type.isArrayType())) || type.isNullType();
    }

    public boolean isValue() {
        return category != Category.CLASS && category != Category.FUNCTION;
    }

    public boolean isNull() {
        return type.isNullType();
    }


}
