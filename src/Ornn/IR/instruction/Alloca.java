package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.Pointer;
import Ornn.IR.util.IRReplicator;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Alloca extends Inst {
    public Register dest;
    public BaseType typePointedTo;
    public Alloca(Register dest, BasicBlock block) {
        super(block);
        assert dest.type instanceof Pointer;
        this.dest = dest;
        this.typePointedTo = ((Pointer) dest.type).typePointedTo;
        dest.def = this;
    }
    @Override
    public String toString() {
        return dest.toString() + " = alloca "
                + typePointedTo.toString()
                + ", align " + typePointedTo.size() / 8;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>();
    }

    @Override
    public Register getDest() {
        return dest;
    }

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        throw new UnreachableError();
    }

    @Override
    public void accept(IRVisitor visitor) {
        throw new UnreachableError();
    }

    @Override
    public void copySelfTo(BasicBlock dest, IRReplicator replicator) {
        throw new UnreachableError(); // should only appear after SSA construction
    }
}
