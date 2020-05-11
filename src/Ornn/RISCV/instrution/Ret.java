package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class Ret extends RVInst {
    public Ret(RVBlock rvBlock) {
        this.block = rvBlock;
    }
    public String toString() {
        return "ret";
    }
    public HashSet<Reg> getUses() {
        return new HashSet<>();
    }
    public void replaceUse(Reg old, Reg newReg) {
        assert false;
    }
}
