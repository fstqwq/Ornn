package Ornn.IR.instruction;

import Ornn.IR.*;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.util.IRReplicator;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Inst {
    public Inst prev;
    public Inst next;
    public BasicBlock basicBlock;

    // for debug
    public String comment;

    public Inst(BasicBlock basicBlock) {
        this.basicBlock = basicBlock;
    }

    public abstract String toString();
    public abstract HashSet<Operand> getUses();
    public abstract boolean isTerminal();
    public abstract Register getDest();
    public abstract void copySelfTo(BasicBlock dest, IRReplicator replicator);
    public abstract void replaceUse(Register old, Operand newOpr);
    public boolean isSameWith(Inst inst) {
        return false;
    }
    public boolean hasSideEffect() {
        return true;
    }
    public abstract void accept(IRVisitor visitor);

    public boolean hasNext() {
        return next != null;
    }
    public boolean hasPrev() {
        return prev != null;
    }
    public void insertBefore(Inst inst) {
        if (hasPrev()) {
            prev.next = inst;
            inst.prev = prev;
        } else {
            basicBlock.front = inst;
        }
        inst.next = this;
        this.prev = inst;
    }
    public void insertAfter(Inst inst) {
        if (hasNext()) {
            next.prev = inst;
            inst.next = next;
        } else {
            basicBlock.back = inst;
        }
        inst.prev = this;
        this.next = inst;
    }
    public void replace(Inst inst) {
        if (hasPrev()) {
            prev.next = inst;
            inst.prev = prev;
        } else {
            basicBlock.front = inst;
        }
        if (hasNext()) {
            next.prev = inst;
            inst.next = next;
        } else {
            basicBlock.back = inst;
        }
        getUses().forEach(x -> x.uses.remove(this));
        basicBlock.processRemoveInst(this);
    }
    public void delete() {
        if (hasPrev()) {
            prev.next = next;
        } else {
            basicBlock.front = next;
        }
        if (hasNext()) {
            next.prev = prev;
        } else {
            basicBlock.back = prev;
        }
        getUses().forEach(x -> x.uses.remove(this));
        basicBlock.processRemoveInst(this);
    }
}
