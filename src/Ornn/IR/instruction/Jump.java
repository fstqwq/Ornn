package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;

import java.util.HashSet;

public class Jump extends Inst{
    public BasicBlock dest;
    public Jump(BasicBlock dest, BasicBlock block) {
        super(block);
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "br label " + dest.toString();
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>();
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
