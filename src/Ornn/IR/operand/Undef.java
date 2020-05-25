package Ornn.IR.operand;

import Ornn.IR.type.BaseType;

public class Undef extends Operand {
    public Undef(BaseType type) {
        super("undef", type);
    }

    @Override
    public Undef getCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "undef";
    }

    @Override
    public boolean isSameWith(Operand other) {
        return true;
    }
}
