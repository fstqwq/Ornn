package Ornn.IR.operand;

import Ornn.IR.type.BoolType;

public class ConstBool extends Operand {
    public boolean value;
    public ConstBool(boolean value) {
        super(value ? "1" : "0", new BoolType());
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Operand getCopy() {
        return this;
    }
}
