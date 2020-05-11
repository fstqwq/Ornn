package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;


/* TODO : check use def */
public abstract class RVInst {
    public enum SCategory {
        add, sub, slt, sgt, xor, or, sll, srl, sra, mul, div, rem,
    }
    public enum BCategory {
        eq, ne, le, ge, lt, gt,
    }
    public enum SzCategory {
        seqz, snez,
    }
    public RVInst prev;
    public RVInst next;
    public RVBlock block;

    // for debug
    public String comment;

    public RVInst() {}

    public abstract String toString();
    public abstract HashSet<Reg> getUses();
    public abstract void replaceUse(Reg old, Reg newReg);

    public boolean hasNext() {
        return next != null;
    }
    public boolean hasPrev() {
        return prev != null;
    }
    public void insertBefore(RVInst RVInst) {
        if (hasPrev()) {
            prev.next = RVInst;
            RVInst.prev = prev;
        } else {
            block.front = RVInst;
        }
        RVInst.next = this;
        this.prev = RVInst;
    }
    public void insertAfter(RVInst RVInst) {
        if (hasNext()) {
            next.prev = RVInst;
            RVInst.next = next;
        } else {
            block.back = RVInst;
        }
        RVInst.prev = this;
        this.next = RVInst;
    }
    public void replace(RVInst RVInst) {
        if (hasPrev()) {
            prev.next = RVInst;
            RVInst.prev = prev;
        } else {
            block.front = RVInst;
        }
        if (hasNext()) {
            next.prev = RVInst;
            RVInst.next = next;
        } else {
            block.back = RVInst;
        }
    }
    public void delete() {
        if (hasPrev()) {
            prev.next = next;
        } else {
            block.front = next;
        }
        if (hasNext()) {
            next.prev = prev;
        } else {
            block.back = prev;
        }
    }
}
