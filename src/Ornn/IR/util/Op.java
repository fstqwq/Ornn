package Ornn.IR.util;

import java.util.HashMap;

public class Op { // for llvm IR

    public static final HashMap<String, String> opTable = new HashMap<>() {
        {
            put("+", "add");
            put("-", "sub");
            put("*", "mul");
            put("/", "sdiv");
            put("%", "srem");
            put("^", "xor");
            put("&", "and");
            put("|", "or");
            put("<<", "shl");
            put(">>", "ashr");
            // icmp
            put(">", "sgt");
            put("<", "slt");
            put(">=", "sge");
            put("<=", "sle");
            put("!=", "ne");
            put("==", "eq");
        }
    };
    public static String translate(String op) {
        return opTable.get(op);
    }
    public static boolean isAbelian(String op) {
        switch (op) {
            case "+": case "*": case "^": case "&": case "|": return true;
            default: return false;
        }
    }
}
