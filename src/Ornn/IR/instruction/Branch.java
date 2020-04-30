package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;

import java.util.HashSet;

public class Branch extends Inst{
    public  BasicBlock thenDest, elseDest;
    public Operand cond;
    public Branch(Operand cond, BasicBlock thenDest, BasicBlock elseDest, BasicBlock block) {
        super(block);
        this.cond = cond;
        this.thenDest = thenDest;
        this.elseDest = elseDest;
    }

    @Override
    public String toString() {
        return "br "
                + cond.type + " " + cond.toString()
                + ", label " + thenDest.toString()
                + ", label " + elseDest.toString();
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>();
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public Register getDest() {
        return null;
    }
}
