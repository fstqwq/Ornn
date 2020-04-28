package Ornn.IR.type;

import Ornn.IR.operand.ConstInt;

import java.util.ArrayList;

public class ClassType extends BaseType {
    public String name;
    public int size = 0;
    public ArrayList<BaseType> members = new ArrayList<>();
    public ArrayList<ConstInt> offsets = new ArrayList<>();

    public ClassType(String name) {
        this.name = name;
    }

    public void addMember(BaseType member) {
        members.add(member);
        offsets.add(new ConstInt(size, 32));
        size += member.size();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "%struct." + name;
    }

    @Override
    public boolean isSame(BaseType type) {
        return type instanceof ClassType && ((ClassType) type).name.equals(name);
    }
}
