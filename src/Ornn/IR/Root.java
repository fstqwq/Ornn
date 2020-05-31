package Ornn.IR;

import Ornn.IR.operand.ConstStr;
import Ornn.IR.operand.Global;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.ClassType;
import Ornn.IR.type.Pointer;
import Ornn.frontend.ToplevelScopeBuilder;
import Ornn.AST.semantic.*;
import Ornn.util.UnreachableCodeError;

import static Ornn.IR.util.Constant.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Root {
    public HashMap<String, Function> builtinFunctions = new LinkedHashMap<>();
    public HashMap<String, Function> functions = new LinkedHashMap<>();
    public HashMap<String, ConstStr> constStrings = new LinkedHashMap<>();
    public ArrayList<Global> globals = new ArrayList<>();
    public HashMap<String, ClassType> types = new LinkedHashMap<>();
    public ArrayList<Global> proxyStatics = new ArrayList<>();
    public void addFunction(Function func) {
        if (builtinFunctions.containsKey(func.name) || functions.containsKey(func.name)) {
            int i = 1; // rename for collision with libc functions
            while (builtinFunctions.containsKey(func.name + i) || functions.containsKey(func.name + i)) {
                i++;
            }
            func.name = func.name + i;
        }
        functions.put(func.name, func);
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
                return STR;
            } else {
                return new Pointer(getType(semanticType.getTypeName()));
            }
        } else if (semanticType instanceof NullType) {
            return VOID;
        } else if (semanticType == null) {
//            System.err.println("warning: resolving null");
            return VOID;
        }
        throw new UnreachableCodeError();
    }
    public Root(ToplevelScope toplevelScope) {

        // avoid duplicate libc name
        builtinFunctions.put("sscanf", null);
        builtinFunctions.put("putchar", null);
        builtinFunctions.put("free", null);
        builtinFunctions.put("memcpy", null);
        builtinFunctions.put("strlen", null);
        builtinFunctions.put("strcpy", null);
        builtinFunctions.put("strcat", null);
        builtinFunctions.put("strcmp", null);
        builtinFunctions.put("memset", null);

        builtinMalloc.returnType = STR;
        builtinMalloc.params.add(new Register("size", I32));
        builtinFunctions.put("malloc", builtinMalloc);

        // alias to print elegantly in LLVM IR
        FunctionSymbol printFunc = (FunctionSymbol) toplevelScope.resolveSymbol("println", null);
        printFunc.setType(ToplevelScopeBuilder.Int);
        printFunc.function = builtinPrintln;
        builtinFunctions.put("puts", builtinPrintln);
        builtinPrintln.returnType = I32;
        builtinPrintln.params.add(new Register("str", STR));

        builtinFunctions.put(builtinPrint.name, builtinPrint);
        builtinPrint.returnType = VOID;
        builtinPrint.params.add(new Register("str", STR));
        ((FunctionSymbol) toplevelScope.resolveSymbol("print", null)).function = builtinPrint;

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
        builtinStringLength.classPtr = new Register("s", STR);
        builtinStringLength.params.add(builtinStringLength.classPtr);
        ((FunctionSymbol) string.resolveSymbol("length", null)).function = builtinStringLength;

        builtinFunctions.put(builtinSubstring.name, builtinSubstring);
        builtinSubstring.returnType = STR;
        builtinSubstring.classPtr = new Register("s", STR);
        builtinSubstring.params.add(builtinSubstring.classPtr);
        builtinSubstring.params.add(new Register("left", I32));
        builtinSubstring.params.add(new Register("right", I32));
        ((FunctionSymbol) string.resolveSymbol("substring", null)).function = builtinSubstring;

        builtinFunctions.put(builtinParseInt.name, builtinParseInt);
        builtinParseInt.returnType = I32;
        builtinParseInt.classPtr = new Register("s", STR);
        builtinParseInt.params.add(builtinParseInt.classPtr);
        ((FunctionSymbol) string.resolveSymbol("parseInt", null)).function = builtinParseInt;

        builtinFunctions.put(builtinStringAdd.name, builtinStringAdd);
        builtinStringAdd.returnType = STR;
        builtinStringAdd.params.add(new Register("lhs", STR));
        builtinStringAdd.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringLT.name, builtinStringLT);
        builtinStringLT.returnType = BOOL;
        builtinStringLT.params.add(new Register("lhs", STR));
        builtinStringLT.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringGT.name, builtinStringGT);
        builtinStringGT.returnType = BOOL;
        builtinStringGT.params.add(new Register("lhs", STR));
        builtinStringGT.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringLE.name, builtinStringLE);
        builtinStringLE.returnType = BOOL;
        builtinStringLE.params.add(new Register("lhs", STR));
        builtinStringLE.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringGE.name, builtinStringGE);
        builtinStringGE.returnType = BOOL;
        builtinStringGE.params.add(new Register("lhs", STR));
        builtinStringGE.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringEQ.name, builtinStringEQ);
        builtinStringEQ.returnType = BOOL;
        builtinStringEQ.params.add(new Register("lhs", STR));
        builtinStringEQ.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinStringNE.name, builtinStringNE);
        builtinStringNE.returnType = BOOL;
        builtinStringNE.params.add(new Register("lhs", STR));
        builtinStringNE.params.add(new Register("rhs", STR));

        builtinFunctions.put(builtinOrd.name, builtinOrd);
        builtinOrd.returnType = I32;
        builtinOrd.classPtr = new Register("s", STR);
        builtinOrd.params.add(builtinOrd.classPtr);
        builtinOrd.params.add(new Register("i", I32));
        ((FunctionSymbol) string.resolveSymbol("ord", null)).function = builtinOrd;

        addConstStr("%d");
        addConstStr("%s");
    }
    /* Replace println with puts */
    public static final Function builtinMalloc = new Function("malloc", true);
    public static final Function builtinPrint = new Function("print", true);
    public static final Function builtinPrintln = new Function("puts", true);
    public static final Function builtinPrintInt = new Function("printInt", true);
    public static final Function builtinPrintlnInt = new Function("printlnInt", true);
    public static final Function builtinGetString = new Function("getString", true);
    public static final Function builtinGetInt = new Function("getInt", true);
    public static final Function builtinStringLength = new Function("string_length", false);
    public static final Function builtinSubstring = new Function("string_substring", false);
    public static final Function builtinParseInt = new Function("string_parseInt", false);
    public static final Function builtinStringAdd = new Function("string_add", false);
    public static final Function builtinStringLT = new Function("string_lt", false);
    public static final Function builtinStringGT = new Function("string_gt", false);
    public static final Function builtinStringLE = new Function("string_le", false);
    public static final Function builtinStringGE = new Function("string_ge", false);
    public static final Function builtinStringEQ = new Function("string_eq", false);
    public static final Function builtinStringNE = new Function("string_ne", false);
    public static final Function builtinOrd = new Function("string_ord", false);
    public static final Function builtinToString = new Function("toString", false);
}
