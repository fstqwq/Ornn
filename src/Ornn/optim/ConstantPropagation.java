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
