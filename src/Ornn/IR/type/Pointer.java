package Ornn.IR.type;

import static Ornn.IR.util.Constant.*;

public class Pointer extends BaseType {
    public BaseType typePointedTo;
    public int dim;

    public Pointer(BaseType typePointedTo) {
        this.typePointedTo = typePointedTo;
        this.dim = typePointedTo.getDim() + 1;
    }

    @Override
    public int getDim() {
        return dim;
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
    public boolean isSameWith(BaseType type) {
        return  (
                    type instanceof Pointer &&
                    (((Pointer) type).typePointedTo instanceof VoidType || ((Pointer)type).typePointedTo.isSameWith(this.typePointedTo))
                ) || (type instanceof ArrayType && ((ArrayType)type).baseType.isSameWith(typePointedTo));
    }
}
