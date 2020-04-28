package Ornn.IR.operand;

import Ornn.IR.instruction.Inst;
import Ornn.IR.type.BaseType;

import java.util.HashSet;

abstract public class Operand {
    public String name;
    public BaseType type;
    public HashSet<Inst> uses = new HashSet<>();

    public Operand(String name, BaseType type) {
        this.name = name;
        this.type = type;
    }
    public Inst defInst() {
        return null;
    }
    public abstract Operand getCopy();
    public abstract String toString();
}
