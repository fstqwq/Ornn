package Ornn.IR.type;

public class VoidType extends BaseType {
    @Override
    public int size() {
        throw new RuntimeException("unreachable");
    }

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public boolean isSameWith(BaseType type) {
        return type instanceof VoidType;
    }
}
