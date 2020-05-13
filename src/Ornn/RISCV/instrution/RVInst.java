package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;


/* TODO : check use def */
public abstract class RVInst {
    public enum SCategory {
        add, sub, slt, xor, or, and, sll, srl, sra, mul, div, rem,
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
    public HashSet<Reg> getUses() {
        return new HashSet<>();
    }
    public void replaceUse(Reg old, Reg newReg) {
        assert false;
    }

    public void replaceRd(Reg old, Reg newReg) {
        assert false;
    }
    public HashSet<Reg> getDefs() {
        return new HashSet<>();
    }
    public void applyStackOffset(int stackOffset) {}
    public boolean hasNext() {
        return next != null;
    }
    public boolean hasPrev() {
        return prev != null;
    }
    public void insertBefore(RVInst inst) {
        if (hasPrev()) {
            prev.next = inst;
        } else {
            block.front = inst;
        }
        inst.prev = prev;
        inst.next = this;
        this.prev = inst;
    }
    public void insertAfter(RVInst inst) {
        if (hasNext()) {
            next.prev = inst;
        } else {
            block.back = inst;
        }
        inst.next = next;
        inst.prev = this;
        this.next = inst;
    }
    public void replace(RVInst inst) {
        if (hasPrev()) {
            prev.next = inst;
            inst.prev = prev;
        } else {
            block.front = inst;
        }
        if (hasNext()) {
            next.prev = inst;
            inst.next = next;
        } else {
            block.back = inst;
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
