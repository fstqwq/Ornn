package Ornn.util;

import java.util.HashMap;

public class Op2Inst {

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
}
