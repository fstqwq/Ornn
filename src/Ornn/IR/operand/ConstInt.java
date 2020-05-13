package Ornn.IR.operand;

import Ornn.IR.type.*;

public class ConstInt extends Operand {
    public int value;
    public ConstInt(int value, int size) {
        super(Integer.toString(value), new IntType(size));
        this.value = value;
    }

    @Override
    public Operand getCopy() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
