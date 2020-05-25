package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.*;
import Ornn.util.UnreachableCodeError;

import java.util.HashSet;

public class Ld extends RVInst {
    public Reg rs, rd;
    public Imm offset;
    public int width;
    public Ld(Reg rs, Imm offset, Reg rd, int width, RVBlock block) {
        this.block = block;
        this.rs = rs;
        this.offset = offset;
        this.rd = rd;
        this.width = width;
    }
    @Override
    public String toString() {
        switch (width) {
            case 1:
                return "lb " + rd + ", " + (rs instanceof GReg ? rs : (offset + "(" + rs + ")"));
            case 4:
                return "lw " + rd + ", " + (rs instanceof GReg ? rs : (offset + "(" + rs + ")"));
        }
        throw new UnreachableCodeError();
    }
    @Override
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{ if (!(rs instanceof GReg)) add(rs);}};
    }
    @Override
    public void replaceUse(Reg old, Reg newReg) {
        if (rs == old) {
            rs = newReg;
        }
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
        offset.applyStackOffset(stackOffset);
    }
}
