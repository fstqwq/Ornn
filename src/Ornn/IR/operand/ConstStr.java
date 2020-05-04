package Ornn.IR.operand;

import Ornn.IR.type.*;

import static Ornn.util.Constant.I8;

public class ConstStr extends Operand {
    public String value;

    public ConstStr(String name, String value) {
        super(name, new Pointer(new ArrayType(value.length() + 1, I8)));
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
