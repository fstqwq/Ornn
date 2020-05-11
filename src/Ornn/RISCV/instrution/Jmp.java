package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class Jmp extends RVInst {
    public RVBlock offset;
    public Jmp(RVBlock offset, RVBlock rvBlock) {
        this.offset = offset;
        this.block = rvBlock;
    }
    public String toString() {
        return "j " + offset;
    }
    public HashSet<Reg> getUses() {
        return new HashSet<>();
    }
    public void replaceUse(Reg old, Reg newReg) {
        assert false;
    }
}
