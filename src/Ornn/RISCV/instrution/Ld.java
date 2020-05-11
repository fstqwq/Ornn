package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.*;
import Ornn.util.UnreachableError;

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
    public String toString() {
        switch (width) {
            case 1:
                return "lb " + rd + ", " + (rs instanceof GReg ? rd : (offset + "(" + rd + ")"));
            case 4:
                return "lw " + rd + ", " + (rs instanceof GReg ? rd : (offset + "(" + rd + ")"));
        }
        throw new UnreachableError();
    }
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{add(rd);}};
    }
    public void replaceUse(Reg old, Reg newReg) {
        if (rs == old) {
            rs = newReg;
        } else {
            assert false;
        }
    }

}
