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
    @Override
    public String toString() {
        return "la " + rd + ", " + src;
    }
    @Override
    public HashSet<Reg> getDefs() {
        return new HashSet<>() {{ add(rd); }};
    }
    @Override
    public void replaceRd(Reg old, Reg newReg) {
        if (rd == old) {
            rd = newReg;
        }
    }
}
