package Ornn.IR.operand;

import Ornn.IR.type.*;

import static Ornn.IR.util.Constant.VOID;


public class Null extends Operand {
    public Null() {
        super("null", new Pointer(VOID));
    }

    @Override
    public Operand getCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "null";
    }
}
