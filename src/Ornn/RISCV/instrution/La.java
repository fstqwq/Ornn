package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.GReg;
import Ornn.RISCV.operand.Reg;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class La extends RVInst {
    public GReg src;
    public Reg rd;
    public La(GReg src, Reg rd, RVBlock block) {
        this.rd = rd;
        this.src = src;
        this.block = block;
    }
    public String toString() {
        return "la " + rd + ", " + src;
    }
    public HashSet<Reg> getUses() {
        return new HashSet<>();
    }
    public void replaceUse(Reg old, Reg newReg) {
        throw new UnreachableError();
    }
}
