package Ornn.IR.type;

public class BoolType extends BaseType {
    @Override
    public int size() {
        return 1; // at least a byte in memory, should convert when load / store
    }

    @Override
    public String toString() {
        return "i1";
    }

    @Override
    public boolean isSame(BaseType type) {
        return type instanceof BoolType;
    }
}
