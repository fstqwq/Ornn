package Ornn.IR.operand;

import Ornn.IR.instruction.Inst;
import Ornn.IR.type.BaseType;

import java.util.HashSet;
import java.util.LinkedHashSet;

abstract public class Operand {
    public String name;
    public BaseType type;
    public HashSet<Inst> uses = new LinkedHashSet<>();

    public Operand(String name, BaseType type) {
        this.name = name;
        this.type = type;
    }
    public abstract Operand getCopy();
    public abstract String toString();
    public boolean isSameWith(Operand other) {
        return false;
    }
}
