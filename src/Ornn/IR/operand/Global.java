package Ornn.IR.operand;

import Ornn.IR.type.BaseType;

public class Global extends Operand {
    public Global(BaseType type, String name) {
        super(name, type);
    }

    @Override
    public Operand getCopy() {
        throw new RuntimeException("unreachable");
    }

    @Override
    public String toString() {
        return "%" + name;
    }
}
