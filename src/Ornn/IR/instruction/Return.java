package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;

import java.util.HashSet;

public class Return extends Inst {
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
        return "ret " + (value == null ? "void" : value.toString());
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ add(value); }};
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
