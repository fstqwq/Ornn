package Ornn.IR.instruction;


/*
    Fake LLVM IR instruction to denote the MIR after SSA destruction
 */

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;

import java.util.HashSet;

public class Move extends Inst {
    public Register dest;
    public Operand src;
    public Move(Register dest, Operand src, BasicBlock block) {
        super(block);
        this.dest = dest;
        this.src = src;
        dest.def = this;
    }

    public void completeConstruction() {
        src.uses.add(this);
    }

    @Override
    public String toString() {
        return dest.toString() + " = mov "
                + src.type.toString() + " " + src.toString();
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{add(src); }};
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
        if (src.equals(old)) {
            src = newOpr;
            success = true;
        }
        assert success;
    }

}
