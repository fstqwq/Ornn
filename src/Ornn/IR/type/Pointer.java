package Ornn.IR.type;

import static Ornn.util.Constant.*;

public class Pointer extends BaseType {
    public BaseType typePointedTo;
    public int dim;

    public Pointer(BaseType typePointedTo) {
        this.typePointedTo = typePointedTo;
        this.dim = typePointedTo.getDim() + 1;
    }

    @Override
    public int size() {
        return PointerSize;
    }

    @Override
    public String toString() {
        return typePointedTo.toString() + "*";
    }

    @Override
    public boolean isSame(BaseType type) {
        return  (
                    type instanceof Pointer &&
                    (((Pointer) type).typePointedTo instanceof VoidType || ((Pointer)type).typePointedTo.isSame(this.typePointedTo))
                ) || (type instanceof ArrayType && ((ArrayType)type).baseType.isSame(typePointedTo));
    }
}
