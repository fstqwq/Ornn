package Ornn.optim;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.*;
import Ornn.IR.type.BoolType;
import Ornn.IR.type.IntType;
import Ornn.util.UnreachableCodeError;

import java.util.Iterator;
import java.util.Map;

import static Ornn.IR.util.Constant.*;

public class ConstantPropagation implements Pass {
    Root root;
    public ConstantPropagation(Root root) {
        this.root = root;
    }

    boolean modified;

    boolean isConstant(Operand operand) {
        return operand instanceof ConstInt
            || operand instanceof ConstBool
            || operand instanceof Null
            || operand instanceof Undef;
    }
    int getInt(Operand operand) {
        if (operand instanceof ConstInt) {
            return ((ConstInt) operand).value;
        } else if (operand instanceof Undef) {
            return 0;
        }
        throw new UnreachableCodeError();
    }

    boolean getBool(Operand operand) {
        if (operand instanceof ConstBool) {
            return ((ConstBool) operand).value;
        } else if (operand instanceof Undef) {
            return false;
        }
        throw new UnreachableCodeError();
    }
    void runForBlock(BasicBlock block) {
        boolean changed;
        changed = false;
/*      // Let CFG Simplification to do it
        for (Iterator<Map.Entry<Register, Phi>> iter = block.phiInst.entrySet().iterator(); iter.hasNext(); ) {
            Phi phi = iter.next().getValue();
            assert phi.blocks.size() > 0; // or fuck DCE
            if (phi.blocks.size() == 1) {
                phi.dest.replaceAll(phi.values.get(0));
                phi.delete();
                iter.remove();
                changed = true;
            }
        }*/
        for (Inst inst = block.front; inst != null; inst = inst.next) {
            if (inst instanceof Binary) {
                Operand src1 = ((Binary) inst).src1, src2 = ((Binary) inst).src2;
                Register dest = ((Binary) inst).dest;
                String op = ((Binary) inst).op;
                if (isConstant(src1) && isConstant(src2)) {
                    Operand equiv;
                    switch (op) {
                        case "*":
                            equiv = new ConstInt(getInt(src1) * getInt(src2), 32);
                            break;
                        case "/":
                            if (getInt(src2) == 0) equiv = I32ZERO;
                            else equiv = new ConstInt(getInt(src1) / getInt(src2), 32);
                            break;
                        case "%":
                            if (getInt(src2) == 0) equiv = I32ZERO;
                            else equiv = new ConstInt(getInt(src1) % getInt(src2), 32);
                            break;
                        case "-":
                            equiv = new ConstInt(getInt(src1) - getInt(src2), 32);
                            break;
                        case "<<":
                            equiv = new ConstInt(getInt(src1) << getInt(src2), 32);
                            break;
                        case ">>":
                            equiv = new ConstInt(getInt(src1) >> getInt(src2), 32);
                            break;
                        case "&":
                            if (src1.type instanceof BoolType) {
                                equiv = new ConstBool(getBool(src1) & getBool(src2));
                            } else{
                                equiv = new ConstInt(getInt(src1) & getInt(src2), 32);
                            }
                            break;
                        case "^":
                            if (src1.type instanceof BoolType) {
                                equiv = new ConstBool(getBool(src1) ^ getBool(src2));
                            } else{
                                equiv = new ConstInt(getInt(src1) ^ getInt(src2), 32);
                            }
                            break;
                        case "|":
                            if (src1.type instanceof BoolType) {
                                equiv = new ConstBool(getBool(src1) | getBool(src2));
                            } else{
                                equiv = new ConstInt(getInt(src1) | getInt(src2), 32);
                            }
                            break;
                        case "+":
                            if (src1.type instanceof IntType) {
                                equiv = new ConstInt(getInt(src1) + getInt(src2), 32);
                            } else {
                                throw new UnreachableCodeError();
                            }
                            break;
                        default:
                            throw new UnreachableCodeError();
                    }
                    dest.replaceAll(equiv);
                    changed = true;
                } else if (isConstant(src1)) {
                    switch (op) {
                        case "*":
                            if (((ConstInt) src1).value == 1) {
                                dest.replaceAll(src2);
                                changed = true;
                            }
                            break;
                        case "<<":
                        case ">>":
                        case "&":
                            if ((src1 instanceof ConstInt && ((ConstInt) src1).value == 0) || (src1 instanceof ConstBool && !((ConstBool) src1).value) ) {
                                dest.replaceAll(src1);
                                changed = true;
                            }
                            break;
                        case "^":
                        case "|":
                        case "+":
                            if ((src1 instanceof ConstInt && ((ConstInt) src1).value == 0) || (src1 instanceof ConstBool && !((ConstBool) src1).value) ) {
                                dest.replaceAll(src2);
                                changed = true;
                            }
                            break;
                        default:
                            break;
                    }
                } else if (isConstant(src2)) {
                    switch (op) {
                        case "*": case "/":
                            if (((ConstInt) src2).value == 1) {
                                dest.replaceAll(src1);
                                changed = true;
                            }
                            break;
                        case "&":
                            if ((src2 instanceof ConstInt && ((ConstInt) src2).value == 0) || (src2 instanceof ConstBool && !((ConstBool) src2).value) ) {
                                dest.replaceAll(src2);
                                changed = true;
                            }
                            break;
                        case "^":
                        case "|":
                        case "+":
                        case "-":
                        case "<<":
                        case ">>":
                            if ((src2 instanceof ConstInt && ((ConstInt) src2).value == 0) || (src2 instanceof ConstBool && !((ConstBool) src2).value) ) {
                                dest.replaceAll(src1);
                                changed = true;
                            }
                            break;
                        default:
                            break;
                    }
                    // associativity
                    if (!changed
                            && src1 instanceof Register
                            && src1.uses.size() == 1 // only a temp
                            && ((Register) src1).def instanceof Binary
                            && ((Binary) ((Register) src1).def).op.equals(op)
                            && ((Binary) ((Register) src1).def).src2 instanceof ConstInt) {
                            if (op.equals("+") || op.equals("-") || op.equals("^") || op.equals("&") || op.equals("|")) { // *, / are dangerous
                                src1.uses.remove(inst);
                                ((Binary) ((Register) src1).def).src1.uses.add(inst);
                                ((Binary) inst).src1 = ((Binary) ((Register) src1).def).src1;
                                assert src2 instanceof ConstInt;
                                switch (op) {
                                    case "+": case "-":
                                        ((Binary) inst).src2 = new ConstInt(((ConstInt) src2).value + ((ConstInt) ((Binary) ((Register) src1).def).src2).value, 32);
                                        break;
                                    case "^":
                                        ((Binary) inst).src2 = new ConstInt(((ConstInt) src2).value ^ ((ConstInt) ((Binary) ((Register) src1).def).src2).value, 32);
                                        break;
                                    case "&":
                                        ((Binary) inst).src2 = new ConstInt(((ConstInt) src2).value & ((ConstInt) ((Binary) ((Register) src1).def).src2).value, 32);
                                        break;
                                    case "|":
                                        ((Binary) inst).src2 = new ConstInt(((ConstInt) src2).value | ((ConstInt) ((Binary) ((Register) src1).def).src2).value, 32);
                                        break;
                                }
                                changed = true;
                            }
                    }
                }
            } else if (inst instanceof Cmp) {
                Operand src1 = ((Cmp) inst).src1, src2 = ((Cmp) inst).src2;
                Register dest = ((Cmp) inst).dest;
                String op = ((Cmp) inst).op;
                if (isConstant(src1) && isConstant(src2)) {
                    Operand equiv;
                    switch (op) {
                        case "<":
                            if (src1.type instanceof IntType) {
                                equiv = new ConstBool(getInt(src1) < getInt(src2));
                            } else {
                                throw new UnreachableCodeError();
                            }
                            break;
                        case ">":
                            if (src1.type instanceof IntType) {
                                equiv = new ConstBool(getInt(src1) > getInt(src2));
                            } else {
                                throw new UnreachableCodeError();
                            }
                            break;
                        case "<=":
                            if (src1.type instanceof IntType) {
                                equiv = new ConstBool(getInt(src1) <= getInt(src2));
                            } else {
                                throw new UnreachableCodeError();
                            }
                            break;
                        case ">=":
                            if (src1.type instanceof IntType) {
                                equiv = new ConstBool(getInt(src1) >= getInt(src2));
                            } else {
                                throw new UnreachableCodeError();
                            }
                            break;
                        case "==":
                            if (src1.type instanceof IntType) {
                                equiv = new ConstBool(getInt(src1) == getInt(src2));
                            } else if (src1.type instanceof BoolType){
                                equiv = new ConstBool(getBool(src1) == getBool(src2));
                            } else {
                                assert src1 instanceof Null && src2 instanceof Null;
                                equiv = TRUE;
                            }
                            break;
                        case "!=":
                            if (src1.type instanceof IntType) {
                                equiv = new ConstBool(getInt(src1) != getInt(src2));
                            } else if (src1.type instanceof BoolType){
                                equiv = new ConstBool(getBool(src1) != getBool(src2));
                            } else {
                                assert src1 instanceof Null && src2 instanceof Null;
                                equiv = FALSE;
                            }
                            break;
                        default:
                            throw new UnreachableCodeError();
                    }
                    dest.replaceAll(equiv);
                    changed = true;
                }
            } else if (inst instanceof Cast) {
                Operand src = ((Cast) inst).src;
                if (isConstant(src)) {
                    if (((Cast) inst).dest.type.equals(I8)) {
                        // i1 to i8
                        ((Cast) inst).dest.replaceAll(new ConstInt(src instanceof ConstBool ? (getBool(src) ? 1 : 0) : getInt(src), 8));
                        changed = true;
                    } else if (((Cast) inst).dest.type.equals(BOOL)) {
                        // i8 to i1
                        ((Cast) inst).dest.replaceAll(getInt(src) == 1 ? TRUE : FALSE);
                        changed = true;
                    } else {
                        // do nothing, since idk
                    }
                }
            }
        }
        modified |= changed;
    }


    void runForFunction(Function function) {
        function.blocks.forEach(this::runForBlock);
    }

    @Override
    public boolean run() {
        modified = false;
        root.functions.forEach((s, function) -> runForFunction(function));
        return modified;
    }
}
