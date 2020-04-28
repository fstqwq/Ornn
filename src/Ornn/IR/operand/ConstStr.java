package Ornn.IR.operand;

import Ornn.IR.type.*;

public class ConstStr extends Operand {
    public String value;

    public ConstStr(String name, String value) {
        super(name, new Pointer(new ArrayType(value.length() + 1, new IntType(8))));
        this.value = value;
    }

    @Override
    public Operand getCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
