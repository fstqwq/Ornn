package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.ConstInt;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;
import Ornn.IR.util.IRReplicator;
import Ornn.util.UnreachableCodeError;

import java.util.HashSet;

public class GEP extends Inst {
    public Register dest;
    public BaseType type;
    public Operand ptr, arrayOffset;
    public ConstInt elementOffset;
    public GEP(BaseType type, Operand ptr, Operand arrayOffset, ConstInt elementOffset, Register dest, BasicBlock block) {
        super(block);
        this.type = type;
        this.ptr = ptr;
        this.arrayOffset = arrayOffset;
        this.elementOffset = elementOffset;
        this.dest = dest;
        ptr.uses.add(this);
        if (arrayOffset != null) arrayOffset.uses.add(this);
        if (elementOffset != null) elementOffset.uses.add(this);
        dest.def = this;
    }

    @Override
    public void copySelfTo(BasicBlock dest, IRReplicator replicator) {
        dest.pushBack(new GEP(type, replicator.get(ptr), replicator.get(arrayOffset), elementOffset, replicator.get(this.dest), dest));
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>(){{ add(ptr);
            if (arrayOffset != null) add(arrayOffset);
            if (elementOffset != null) add(elementOffset);}};
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return dest.toString() + " = getelementptr inbounds "
                + type.toString() + ", "
                + ptr.type.toString() + " " + ptr.toString() + ", "
                + arrayOffset.type.toString() + " " + arrayOffset.toString() +
                (elementOffset == null ? "" : ", " + elementOffset.type.toString() + " " + elementOffset.toString());
    }
    @Override
    public Register getDest() {
        return dest;
    }

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        boolean success = false;
        if (ptr.equals(old)) {
            ptr = newOpr;
            success = true;
        }
        if (old.equals(arrayOffset)) {
            arrayOffset = newOpr;
            success = true;
        }
        if (!success) {
            throw new UnreachableCodeError();
        }
    }
    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isSameWith(Inst inst) {
        return inst instanceof GEP
                && ptr.isSameWith(((GEP) inst).ptr)
                && arrayOffset.isSameWith(((GEP) inst).arrayOffset)
                && (
                        (elementOffset == null && ((GEP) inst).elementOffset == null)
                ||      (elementOffset != null && elementOffset.isSameWith(((GEP) inst).elementOffset))
                );
    }
    @Override
    public boolean hasSideEffect() {
        return false;
    }
}
