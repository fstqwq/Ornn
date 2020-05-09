package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Return extends Inst implements Terminator {
    public Operand value;

    public Return(BasicBlock block, Operand value) {
        super(block);
        this.value = value;
        if (value != null) {
            value.uses.add(this);
        }
    }

    @Override
    public String toString() {
        return "ret " + (value == null ? "void" : value.type.toString() + " " + value.toString());
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ if (value != null) add(value); }};
    }

    @Override
    public boolean isTerminal() {
        return true;
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
        assert success;
    }

    @Override
    public void redirect(BasicBlock from, BasicBlock to) {
        throw new UnreachableError();
    }
}
