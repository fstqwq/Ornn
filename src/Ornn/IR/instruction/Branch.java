package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.util.IRReplicator;
import Ornn.util.UnreachableCodeError;

import java.util.HashSet;

public class Branch extends Inst implements Terminator {
    public  BasicBlock thenDest, elseDest;
    public Operand cond;
    public Branch(Operand cond, BasicBlock thenDest, BasicBlock elseDest, BasicBlock block) {
        super(block);
        this.cond = cond;
        this.thenDest = thenDest;
        this.elseDest = elseDest;
        cond.uses.add(this);
    }

    @Override
    public String toString() {
        return "br "
                + cond.type + " " + cond.toString()
                + ", label " + thenDest.toString()
                + ", label " + elseDest.toString();
    }

    @Override
    public void copySelfTo(BasicBlock dest, IRReplicator replicator) {
        dest.pushBack(new Branch(replicator.get(cond), replicator.get(thenDest), replicator.get(elseDest), dest));
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ add(cond); }};
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
        if (cond.equals(old)) cond = newOpr;
        else throw new UnreachableCodeError();
    }

    @Override
    public void redirect(BasicBlock from, BasicBlock to) {
        boolean success = false;
        if (thenDest.equals(from)) {
            thenDest = to;
            success = true;
        }
        if (elseDest.equals(from)) {
            elseDest = to;
            success = true;
        }
        assert success;
    }
    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
