package Ornn.IR.instruction;

import Ornn.IR.*;
import Ornn.IR.operand.Operand;

import java.util.HashSet;

public abstract class Inst {
    public Inst prev;
    public Inst next;
    public BasicBlock basicBlock;

    public Inst(BasicBlock basicBlock) {
        this.basicBlock = basicBlock;
    }

    public abstract String toString();
    public abstract HashSet<Operand> getUses();
    public abstract boolean isTerminal();

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
            basicBlock.back = next;
        }
    }
}
