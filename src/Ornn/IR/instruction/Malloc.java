package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.Pointer;
import Ornn.IR.util.IRReplicator;

import java.util.HashSet;

public class Malloc extends Inst {
    public Register dest;
    public Operand size;
    public Malloc(Register dest, Operand size, BasicBlock block) {
        super(block);
        this.dest = dest;
        this.size = size;
        dest.def = this;
        size.uses.add(this);
    }
    @Override
    public String toString() {
        return dest.toString() + " = call i8* @malloc(" +
                size.type.toString() + " " + size.toString() + ")";
    }

    @Override
    public void copySelfTo(BasicBlock dest, IRReplicator replicator) {
        dest.pushBack(new Malloc(replicator.get(this.dest), replicator.get(size), dest));
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ add(size); }};
    }
    @Override
    public Register getDest() {
        return dest;
    }

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        boolean success = false;
        if (size.equals(old)) {
            size = newOpr;
            success = true;
        }
        assert success;
    }
    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
