package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.*;
import Ornn.util.Op2Inst;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Binary extends Inst { // return int
    public String op;
    public Operand src1, src2;
    Register dest;
    public Binary (Operand src1, Operand src2, Register dest, String op, BasicBlock block) {
        super(block);
        this.src1 = src1;
        this.src2 = src2;
        this.dest = dest;
        this.op = op;
        src1.uses.add(this);
        src2.uses.add(this);
        dest.def = this;
    }

    @Override
    public String toString() {
        return dest.toString() + " = "
                + Op2Inst.translate(op) + " "
                + src1.type.toString() + " " + src1.toString() + ", "
                + src2.toString();
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ add(src1); add(src2);}};
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public Register getDest() {
        return dest;
    }

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        boolean success = false;
        if (src1.equals(old)) {
            src1 = newOpr;
            success = true;
        }
        if (src2.equals(old)) {
            src2 = newOpr;
            success = true;
        }
        assert success;
    }
}
