package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.*;
import Ornn.IR.type.Pointer;

import java.util.HashSet;

public class Store extends Inst {
    public Operand value;
    public Operand addr;
    public Store(Operand addr, Operand value, BasicBlock block) {
        super(block);
        assert addr.type instanceof Pointer;
        this.value = value;
        this.addr = addr;
        if (value instanceof Null) value.type = ((Pointer) addr.type).typePointedTo;
        value.uses.add(this);
        addr.uses.add(this);
    }

    @Override
    public String toString() {
        return "store "
                + value.type.toString() + " " + value.toString() + ", "
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

    @Override
    public Register getDest() {
        return null;
    }

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        boolean success = false;
        if (value.equals(old)) {
            value = newOpr;
            success = true;
        }
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
