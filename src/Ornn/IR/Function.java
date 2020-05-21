package Ornn.IR;

import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Function {
    public String name;
    public BaseType returnType;
    public Register classPtr = null;
    public ArrayList<Register> params = new ArrayList<>();
    public ArrayList<BaseType> paramTypes = new ArrayList<>();
    public BasicBlock entryBlock;
    public BasicBlock exitBlock;

    public HashSet<Function> callee = new LinkedHashSet<>(), caller = new LinkedHashSet<>();
    public HashSet<Register> allocVar = new LinkedHashSet<>();
    public HashSet<BasicBlock> blocks = new LinkedHashSet<>();
    public boolean hasSideEffect;

    public Function(String name, boolean hasSideEffect) {
        this.name = name;
        this.hasSideEffect = hasSideEffect;
        blocks.add(entryBlock = new BasicBlock(this, "entry_" + name));
    }

    public BaseType getParamType(int index) {
        if (!paramTypes.isEmpty()) {
            return paramTypes.get(index);
        } else {
            assert params.size() > index;
            return params.get(index).type;
        }
    }

    public boolean isMember() {
        return classPtr != null;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
