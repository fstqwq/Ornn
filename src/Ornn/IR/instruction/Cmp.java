package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Null;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.util.Op2Inst;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Cmp extends Inst { // return boolean
    public String op;
    public Operand src1, src2;
    Register dest;
    public Cmp(Operand src1, Operand src2, Register dest, String op, BasicBlock block) {
        super(block);
        this.src1 = src1;
        this.src2 = src2;
        if (src1 instanceof Null) src1.type = src2.type;
        if (src2 instanceof Null) src2.type = src1.type;
        this.dest = dest;
        this.op = op;
        src1.uses.add(this);
        src2.uses.add(this);
        dest.def = this;
    }

    @Override
    public String toString() {
        return dest.toString() + " = "
                + "icmp " + Op2Inst.translate(op) + " "
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
