package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.util.IRReplicator;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Jump extends Inst implements Terminator {
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
    public void copySelfTo(BasicBlock dest, IRReplicator replicator) {
        dest.pushBack(new Jump(replicator.get(this.dest), dest));
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

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        throw new UnreachableError();
    }
    @Override
    public void redirect(BasicBlock from, BasicBlock to) {
        if (dest.equals(from)) dest = to;
        else throw new UnreachableError();
    }
    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
