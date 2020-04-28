package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.*;

import java.util.HashSet;

public class Store extends Inst {
    public Operand value;
    public Operand addr;
    public Store(Operand addr, Operand value, BasicBlock block) {
        super(block);
        this.value = value;
        this.addr = addr;
        value.uses.add(this);
        addr.uses.add(this);
    }

    @Override
    public String toString() {
        return "store "
                + value.type.toString() + ", "
                + value.toString() + ", "
                + addr.type.toString() + " " + addr.toString()
                + ", align " + value.type.size() / 8;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ add(addr); add(value); }};
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
}
