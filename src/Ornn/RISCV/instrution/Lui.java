package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Imm;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class Lui extends RVInst {
    public Imm value;
    public Reg rd;
    public Lui(Imm value, Reg rd, RVBlock block) {
        this.value = value;
        this.rd = rd;
        this.block = block;
    }
    @Override
    public String toString() {
        return "lui " + rd + ", " + value;
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
    @Override
    public void applyStackOffset(int stackOffset) {
        value.applyStackOffset(stackOffset);
    }

    @Override
    public RVInst getCopy() {
        return new Lui(value, rd, block);
    }
}
