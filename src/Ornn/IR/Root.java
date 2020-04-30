package Ornn.IR;

import Ornn.IR.operand.ConstStr;
import Ornn.IR.operand.Global;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.ClassType;
import Ornn.IR.type.Pointer;
import Ornn.frontend.ToplevelScopeBuilder;
import Ornn.semantic.*;
import Ornn.util.Position;
import Ornn.util.UnreachableError;

import static Ornn.util.Constant.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Root {
    public HashMap<String, Function> builtinFunctions = new LinkedHashMap<>();
    public HashMap<String, Function> functions = new LinkedHashMap<>();
    public HashMap<String, ConstStr> constStrings = new LinkedHashMap<>();
    public ArrayList<Global> globals = new ArrayList<>();
    public HashMap<String, ClassType> types = new LinkedHashMap<>();
    public void addFunction(String name, Function func) {
        functions.put(name, func);
    }
    public Function getFunction(String name) {
        if (functions.containsKey(name)) {
            return functions.get(name);
        }
        else {
            return builtinFunctions.get(name);
        }
    }
    public void addConstStr(String value) {
        if (constStrings.containsKey(value)) {

        }
        else {
           constStrings.put(value, new ConstStr(".str." + constStrings.size(), value));
        }
    }
    public ConstStr getConstStr(String value) {
        return constStrings.get(value);
    }
    public void addType(String name, ClassType type) {
        types.put(name, type);
    }
    public ClassType getType(String name) {
        return types.get(name);
    }
    public void addGlobal(Global global) {
        globals.add(global);
    }
    public boolean isBuiltin(String name) {
        return builtinFunctions.containsKey(name);
    }

    public BaseType resolveType(SemanticType semanticType, boolean inMemory) {
        if (semanticType instanceof SemanticArrayType) {
            BaseType ret = resolveType(((SemanticArrayType) semanticType).getBaseType(), inMemory);
            for (int i = 0; i < ((SemanticArrayType) semanticType).getDimension(); i++)
                ret = new Pointer(ret);
            return ret;
        } else if (semanticType instanceof PrimitiveTypeSymbol) {
            switch (semanticType.getTypeName()) {
                case "int":
                    return I32;
                case "bool":
                    return inMemory ? I8 : BOOL;
                case "void":
                    return VOID;
                default:
                    assert false;
            }
        } else if (semanticType instanceof ClassSymbol) {
            if (semanticType.getTypeName().equals("string")) {
                return new Pointer(STR);
            } else {
                return new Pointer(getType(semanticType.getTypeName()));
            }
        } else if (semanticType instanceof NullType) {
            return VOID;
        } else if (semanticType == null) {
            return VOID;
        }
        throw new UnreachableError();
    }
    public Root(ToplevelScope toplevelScope) {
        // tricky way to print elegantly in LLVM IR
        FunctionSymbol printFunc = (FunctionSymbol) toplevelScope.resolveSymbol("print", null);
        printFunc.setType(ToplevelScopeBuilder.Int);
        builtinFunctions.put("print", builtinPuts);
        builtinPuts.returnType = I32;
        builtinPuts.params.add(new Register("str", STR));
        printFunc.function = builtinPuts;

        builtinFunctions.put(builtinPrintln.name, builtinPrintln);
        builtinPrintln.returnType = VOID;
        builtinPrintln.params.add(new Register("n", I32));
        ((FunctionSymbol) toplevelScope.resolveSymbol("println", null)).function = builtinPrintln;

        builtinFunctions.put(builtinPrintInt.name, builtinPrintInt);
        builtinPrintInt.returnType = VOID;
        builtinPrintInt.params.add(new Register("n", I32));
        ((FunctionSymbol) toplevelScope.resolveSymbol("printInt", null)).function = builtinPrintInt;

        builtinFunctions.put(builtinPrintlnInt.name, builtinPrintlnInt);
        builtinPrintlnInt.returnType = VOID;
        builtinPrintlnInt.params.add(new Register("n", I32));
        ((FunctionSymbol) toplevelScope.resolveSymbol("printlnInt", null)).function = builtinPrintlnInt;

        builtinFunctions.put(builtinGetString.name, builtinGetString);
        builtinGetString.returnType = STR;
        ((FunctionSymbol) toplevelScope.resolveSymbol("getString", null)).function = builtinGetString;

        builtinFunctions.put(builtinGetInt.name, builtinGetInt);
        builtinGetInt.returnType = I32;
        ((FunctionSymbol) toplevelScope.resolveSymbol("getInt", null)).function = builtinGetInt;

        builtinFunctions.put(builtinToString.name, builtinToString);
        builtinToString.returnType = STR;
        builtinToString.params.add(new Register("i", I32));
        ((FunctionSymbol) toplevelScope.resolveSymbol("toString", null)).function = builtinToString;

        ClassSymbol string = ToplevelScopeBuilder.string;

        builtinFunctions.put(builtinStringLength.name, builtinStringLength);
        builtinStringLength.returnType = I32;
        builtinStringLength.params.add(new Register("s", STR));
        ((FunctionSymbol) string.resolveSymbol("length", null)).function = builtinStringLength;

        builtinFunctions.put(builtinSubstring.name, builtinSubstring);
        builtinSubstring.returnType = STR;
        builtinSubstring.params.add(new Register("s", STR));
        builtinSubstring.params.add(new Register("left", STR));
        builtinSubstring.params.add(new Register("right", STR));
        ((FunctionSymbol) string.resolveSymbol("substring", null)).function = builtinSubstring;

        builtinFunctions.put(builtinParseInt.name, builtinParseInt);
        builtinParseInt.returnType = I32;
        builtinParseInt.params.add(new Register("s", STR));
        ((FunctionSymbol) string.resolveSymbol("parseInt", null)).function = builtinParseInt;

        builtinFunctions.put(builtinStringAdd.name, builtinStringAdd);
        builtinStringAdd.returnType = STR;
        builtinStringAdd.params.add(new Register("lhs", STR));
        builtinStringAdd.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringLT.name, builtinStringLT);
        builtinStringLT.returnType = STR;
        builtinStringLT.params.add(new Register("lhs", STR));
        builtinStringLT.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringGT.name, builtinStringGT);
        builtinStringGT.returnType = STR;
        builtinStringGT.params.add(new Register("lhs", STR));
        builtinStringGT.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringLE.name, builtinStringLE);
        builtinStringLE.returnType = STR;
        builtinStringLE.params.add(new Register("lhs", STR));
        builtinStringLE.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringGE.name, builtinStringGE);
        builtinStringGE.returnType = STR;
        builtinStringGE.params.add(new Register("lhs", STR));
        builtinStringGE.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringEQ.name, builtinStringEQ);
        builtinStringEQ.returnType = STR;
        builtinStringEQ.params.add(new Register("lhs", STR));
        builtinStringEQ.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringNE.name, builtinStringNE);
        builtinStringNE.returnType = STR;
        builtinStringNE.params.add(new Register("lhs", STR));
        builtinStringNE.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinOrd.name, builtinOrd);
        builtinOrd.returnType = I32;
        builtinOrd.params.add(new Register("s", STR));
        builtinOrd.params.add(new Register("i", I32));
        ((FunctionSymbol) string.resolveSymbol("ord", null)).function = builtinOrd;
    }
    /* Replace print with puts, for ir gen convenience */
    public static final Function builtinPuts = new Function("puts", true);
    public static final Function builtinPrintln = new Function("println", true);
    public static final Function builtinPrintInt = new Function("printInt", true);
    public static final Function builtinPrintlnInt = new Function("printlnInt", true);
    public static final Function builtinGetString = new Function("getString", true);
    public static final Function builtinGetInt = new Function("getInt", true);
    public static final Function builtinStringLength = new Function("string.length", false);
    public static final Function builtinSubstring = new Function("string.substring", false);
    public static final Function builtinParseInt = new Function("string.parseInt", false);
    public static final Function builtinStringAdd = new Function("string.add", false);
    public static final Function builtinStringLT = new Function("string.lt", false);
    public static final Function builtinStringGT = new Function("string.gt", false);
    public static final Function builtinStringLE = new Function("string.le", false);
    public static final Function builtinStringGE = new Function("string.ge", false);
    public static final Function builtinStringEQ = new Function("string.eq", false);
    public static final Function builtinStringNE = new Function("string.ne", false);
    public static final Function builtinOrd = new Function("string.ord", false);
    public static final Function builtinToString = new Function("toString", false);
}
