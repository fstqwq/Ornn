package Ornn.IR.operand;

import Ornn.IR.instruction.Inst;
import Ornn.IR.type.BaseType;


public class Register extends Operand {
    public Inst def;

    public Register(String name, BaseType type) {
        super(name, type);
    }

    @Override
    public Inst defInst() {
        return def;
    }

    @Override
    public Operand getCopy() {
        return new Register(name, type);
    }

    @Override
    public String toString() {
        return "%" + name;
    }

    public void replaceAll(Operand to) {
        for (Inst inst : uses) {
            inst.replaceUse(this, to);
            to.uses.add(inst);
        }
    }
}
