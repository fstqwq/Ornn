package Ornn.IR.type;

public class IntType extends BaseType {
    private int size;
    public IntType(int size) {
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "i" + size;
    }

    @Override
    public boolean isSameWith(BaseType type) {
        return (type instanceof IntType && size == type.size()) || (size == 8 && type instanceof BoolType);
    }
}
