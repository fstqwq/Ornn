package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Li extends RVInst {
    public int value;
    public Reg rd;
    public Li(int value, Reg rd, RVBlock block) {
        this.value = value;
        this.rd = rd;
        this.block = block;
    }
    @Override
    public String toString() {
        return "li " + rd + ", " + value;
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
