package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Imm;
import Ornn.RISCV.operand.Reg;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class Lui extends RVInst {
    public Imm value;
    public Reg rd;
    public Lui(Imm value, Reg rd, RVBlock block) {
        this.value = value;
        this.rd = rd;
        this.block = block;
    }
    public String toString() {
        return "lui " + rd + ", " + value;
    }
    public HashSet<Reg> getUses() {
        return new HashSet<>();
    }
    public void replaceUse(Reg old, Reg newReg) {
        throw new UnreachableError();
    }
}
