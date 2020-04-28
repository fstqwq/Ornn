package Ornn.IR;

import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;

import java.util.ArrayList;
import java.util.HashSet;

public class Function {
    public String name;
    public BaseType returnType;
    public Register classPtr = null;
    public ArrayList<Register> params = new ArrayList<>();
    public BasicBlock entryBlock;
    public BasicBlock exitBlock;

    public HashSet<Function> callee = new HashSet<>();
    public HashSet<Register> allocVar = new HashSet<>();
    public HashSet<BasicBlock> blocks = new HashSet<>();
    public boolean hasSideEffect;

    public Function(String name, boolean hasSideEffect) {
        this.name = name;
        this.hasSideEffect = hasSideEffect;
        blocks.add(entryBlock = new BasicBlock(this, "entry_" + name));
    }

    public boolean isMember() {
        return classPtr != null;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
