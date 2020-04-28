package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.*;

import java.util.HashSet;

public class Load extends Inst {
    public Register dest;
    public Operand addr;
    public Load(Register dest, Operand addr, BasicBlock block) {
        super(block);
        this.dest = dest;
        this.addr = addr;
        dest.uses.add(this);
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
        return new HashSet<>() {{ add(addr); }};
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
}
