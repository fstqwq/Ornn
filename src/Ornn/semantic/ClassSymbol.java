package Ornn.semantic;

import Ornn.AST.VarDeclNode;
import Ornn.util.CompilationError;
import Ornn.util.Position;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassSymbol extends Symbol implements Scope, Type{
    private Scope enclosingScope;
    private FunctionSymbol constructor;
    private Map<String, VariableSymbol> variableSymbolMap = new LinkedHashMap<>();
    private Map<String, FunctionSymbol> functionSymbolMap = new LinkedHashMap<>();

    public ClassSymbol(String name, Type type, VarDeclNode varDeclNode, Scope enclosingScope) {
        super(name, type, varDeclNode);
        this.enclosingScope = enclosingScope;
        constructor = null;
    }

    @Override
    public boolean isClassSymbol() {
        return true;
    }

    @Override
    public String getScopeName() {
        return super.getSymbolName();
    }

    @Override
    public String getTypeName() {
        return super.getSymbolName();
    }

    @Override
    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public FunctionSymbol getConstructor() {
        return constructor;
    }

    public void setConstructor(FunctionSymbol constructor) {
        this.constructor = constructor;
    }

    @Override
    public void defineVariable(VariableSymbol symbol) {
        if (variableSymbolMap.containsKey(symbol.getSymbolName()) || functionSymbolMap.containsKey(symbol.getSymbolName()))
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefine().getPosition());
        variableSymbolMap.put(symbol.getSymbolName(), symbol);
        symbol.setScope(this);
    }

    @Override
    public void defineFunction(FunctionSymbol symbol) {
        if (variableSymbolMap.containsKey(symbol.getSymbolName()) || functionSymbolMap.containsKey(symbol.getSymbolName()))
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefine().getPosition());
        functionSymbolMap.put(symbol.getSymbolName(), symbol);
        symbol.setScope(this);
    }

    @Override
    public void defineClass(ClassSymbol symbol) {

    }

    @Override
    public Symbol resolveSymbol(String identifier, Position position) {
        Symbol variableSymbol = variableSymbolMap.get(identifier);
        Symbol functionSymbol = functionSymbolMap.get(identifier);
        if (variableSymbol != null) return variableSymbol;
        if (functionSymbol != null) return functionSymbol;
        throw new CompilationError(identifier + " is not a member of " + getSymbolName(), position);
    }

    @Override
    public void compatible(Type type, Position position) {
        if (!type.getTypeName().equals(getTypeName()) &&
                (type.getTypeName().equals("string")
                || !type.getTypeName().equals("null")
                )
        )
            throw new CompilationError("'" + getTypeName().equals("string") + "' is not compatible with '" + type.getTypeName() + "'", position);
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public boolean isClassType() {
        return true;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isNullType() {
        return false;
    }

}
