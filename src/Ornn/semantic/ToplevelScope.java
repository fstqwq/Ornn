package Ornn.semantic;

import Ornn.AST.ArrayTypeNode;
import Ornn.AST.TypeNode;
import Ornn.util.CompilationError;
import Ornn.util.Position;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToplevelScope extends NameScope {
    private Map<String, Type> typeMap;

    public ToplevelScope () {
        super(null);
        typeMap = new LinkedHashMap<>();
    }

    public void defineNull(NullType nullType) {
        typeMap.put("null", nullType);
    }

    public void defineType(PrimitiveTypeSymbol symbol) {
        typeMap.put(symbol.getSymbolName(), symbol);
        symbol.setScope(this);
    }

    @Override
    public void defineClass(ClassSymbol symbol) {
        if (typeMap.containsKey(symbol.getSymbolName()))
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefineNode().getPosition());
        if (symbolMap.containsKey(symbol.getSymbolName()))
            throw new CompilationError("duplicate identifiers : " + symbol.getSymbolName(), symbol.getDefineNode().getPosition());
        typeMap.put(symbol.getSymbolName(), symbol);
        symbolMap.put(symbol.getSymbolName(), symbol);
        symbol.setScope(this);
    }

    @Override
    public Symbol resolveSymbol(String identifier, Position position) {
        Symbol symbol = symbolMap.get(identifier);
        if (symbol == null) {
            throw new CompilationError("undefined identifier " + identifier, position);
        }
        return symbol;
    }

    public Type resolveType(String identifier, Position position) {
        Type type = typeMap.get(identifier);
        if (type == null) {
            throw new CompilationError("undefined identifier " + identifier, position);
        }
        return type;
    }

    public Type resolveType(TypeNode typeNode) {
        Type type = resolveType(typeNode.getTypeIdentifier(), typeNode.getPosition());
        if (typeNode instanceof ArrayTypeNode) {
            return new ArrayType(type, ((ArrayTypeNode) typeNode).getDimension());
        } else {
            return type;
        }
    }
}
