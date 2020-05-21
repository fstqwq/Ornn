package Ornn.semantic;

import Ornn.AST.ClassDeclNode;
import Ornn.util.CompilationError;
import Ornn.AST.util.Position;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassSymbol extends Symbol implements Scope, SemanticType {
    private Scope enclosingScope;
    private FunctionSymbol constructor;
    private Map<String, VariableSymbol> variableSymbolMap = new LinkedHashMap<>();
    private Map<String, FunctionSymbol> functionSymbolMap = new LinkedHashMap<>();

    public ClassSymbol(String name, ClassDeclNode classDeclNode, Scope enclosingScope) {
        super(name, null, classDeclNode);
        this.enclosingScope = enclosingScope;
        constructor = null;
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
        if (variableSymbolMap.containsKey(symbol.getSymbolName()) || functionSymbolMap.containsKey(symbol.getSymbolName())) {
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefineNode().getPosition());
        }
        variableSymbolMap.put(symbol.getSymbolName(), symbol);
        symbol.setScope(this);
    }

    @Override
    public void defineFunction(FunctionSymbol symbol) {
        if (variableSymbolMap.containsKey(symbol.getSymbolName()) || functionSymbolMap.containsKey(symbol.getSymbolName())) {
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefineNode().getPosition());
        }
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
        return enclosingScope.resolveSymbol(identifier, position);
    }

    public Symbol accessMember(String identifier, Position position) {
        Symbol variableSymbol = variableSymbolMap.get(identifier);
        Symbol functionSymbol = functionSymbolMap.get(identifier);
        if (variableSymbol != null) return variableSymbol;
        if (functionSymbol != null) return functionSymbol;
        throw new CompilationError(identifier + " is not member of " + getSymbolName(), position);
    }

    @Override
    public void compatible(SemanticType type, Position position) {
        if (!type.getTypeName().equals(getTypeName()))
            if (type.getTypeName().equals("string") || !(type instanceof NullType)) {
                throw new CompilationError(getTypeName() + " is not compatible with " + type.getTypeName(), position);
            }
    }

    @Override
    public void equable(SemanticType type, Position position) {
        if (!type.getTypeName().equals(getTypeName()))
            if (!(type instanceof NullType)) {
                throw new CompilationError(getTypeName() + " can't compare equality with " + type.getTypeName(), position);
            }
    }
}
