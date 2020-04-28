package Ornn.IR.operand;

import Ornn.IR.type.*;

public class Null extends Operand {
    public Null() {
        super("0", new Pointer(new VoidType()));
    }

    @Override
    public Operand getCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "0";
    }
}
