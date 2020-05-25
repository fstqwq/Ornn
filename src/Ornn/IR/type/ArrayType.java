package Ornn.IR.type;

public class ArrayType extends BaseType {
    public int num;
    public BaseType baseType;

    public ArrayType(int num, BaseType baseType) {
        this.num = num;
        this.baseType = baseType;
    }

    @Override
    public int size() {
        return baseType.size();
    }

    @Override
    public String toString() {
        return "[" + num + " x " + baseType.toString() + "]";
    }

    @Override
    public boolean isSameWith(BaseType type) {
        return  (
                    type instanceof Pointer &&
                    (((Pointer) type).typePointedTo instanceof VoidType || ((Pointer)type).typePointedTo.isSameWith(baseType))
                ) || (type instanceof ArrayType && type.isSameWith(this.baseType));
    }
}
