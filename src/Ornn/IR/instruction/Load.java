package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.*;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Load extends Inst {
    public Register dest;
    public Operand addr;
    public Load(Register dest, Operand addr, BasicBlock block) {
        super(block);
        this.dest = dest;
        this.addr = addr;
        dest.def = this;
        addr.uses.add(this);
    }

    @Override
    public String toString() {
        return dest.toString() + " = load "
        + dest.type.toString() + ", "
        + addr.type.toString() + " " + addr.toString()
        + ", align " + dest.type.size() / 8;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{add(addr); }};
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
        if (addr.equals(old)) {
            addr = newOpr;
            success = true;
        }
        assert success;
    }
    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
