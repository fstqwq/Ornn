package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.*;
import Ornn.IR.util.IRReplicator;
import Ornn.IR.util.Op;

import java.util.HashSet;

public class Binary extends Inst { // return int
    public String op;
    public Operand src1, src2;
    public Register dest;
    public Binary (Operand src1, Operand src2, Register dest, String op, BasicBlock block) {
        super(block);
        this.src1 = src1;
        this.src2 = src2;
        this.dest = dest;

        this.op = op;
        src1.uses.add(this);
        src2.uses.add(this);
        dest.def = this;
    }

    @Override
    public String toString() {
        return dest.toString() + " = "
                + Op.translate(op) + " "
                + src1.type.toString() + " " + src1.toString() + ", "
                + src2.toString();
    }

    @Override
    public void copySelfTo(BasicBlock dest, IRReplicator replicator) {
        dest.pushBack(new Binary(replicator.get(src1), replicator.get(src2), (Register) replicator.get(this.dest), op, dest));
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ add(src1); add(src2);}};
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public Register getDest() {
        return dest;
    }

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        boolean success = false;
        if (src1.equals(old)) {
            src1 = newOpr;
            success = true;
        }
        if (src2.equals(old)) {
            src2 = newOpr;
            success = true;
        }
        assert success;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isSameWith(Inst inst) {
        return inst instanceof Binary && ((Binary) inst).op.equals(op) &&
            (
                    (src1.equals(((Binary) inst).src1) && src2.equals(((Binary) inst).src2))
            ||      (Op.isAbelian(op) && src1.equals(((Binary) inst).src2) && src2.equals(((Binary) inst).src1))
            );
    }

    @Override
    public boolean hasSideEffect() {
        return false;
    }
}
