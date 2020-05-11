package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class Mv extends RVInst {
    public Reg rs, rd;
    public Mv(Reg rs, Reg rd, RVBlock rvBlock) {
        this.rs = rs;
        this.rd = rd;
        this.block = rvBlock;
    }
    public String toString() {
        return "mv " + rd + ", " + rs + ", ";
    }
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{add(rs);}};
    }
    public void replaceUse(Reg old, Reg newReg) {
        if (rs == old) {
            rs = newReg;
        } else {
            assert false;
        }
    }
}
