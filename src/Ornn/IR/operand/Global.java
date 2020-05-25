package Ornn.IR.operand;

import Ornn.IR.type.BaseType;

import java.util.ArrayList;

public class Global extends Operand {
    public boolean isArray;
    public int arraySize, arrayLength;
    public ArrayList<String> initialization;
    public Global(BaseType type, String name) {
        super(name, type);
    }

    @Override
    public Operand getCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "@" + name;
    }

    @Override
    public boolean isSameWith(Operand other) {
        return equals(other);
    }
}
