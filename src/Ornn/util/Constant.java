package Ornn.util;

import Ornn.IR.operand.ConstBool;
import Ornn.IR.operand.ConstInt;
import Ornn.IR.type.BoolType;
import Ornn.IR.type.IntType;
import Ornn.IR.type.Pointer;
import Ornn.IR.type.VoidType;

public class Constant {
    public static final IntType I8 = new IntType(8);
    public static final IntType I32 = new IntType(32);
    public static final Pointer I32Array = new Pointer(I32);
    public static final Pointer STR = new Pointer(I8);
    public static final BoolType BOOL = new BoolType();
    public static final VoidType VOID = new VoidType();
    public static final ConstBool TRUE = new ConstBool(true);
    public static final ConstBool FALSE = new ConstBool(false);
    public static final ConstInt I8FALSE = new ConstInt(0, 8);
    public static final ConstInt I8TRUE = new ConstInt(1, 8);
    public static final ConstInt I32NEGONE = new ConstInt(-1, 32);
    public static final ConstInt I32ZERO = new ConstInt(0, 32);
    public static final ConstInt I32ONE = new ConstInt(1, 32);

    public static final int PointerSize = 32;
}
