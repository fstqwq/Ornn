package Ornn.RISCV.instrution;


import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class Sz extends RVInst {
    public Reg rs, rd;
    public SzCategory op;
    public Sz(Reg rs, SzCategory op, Reg rd, RVBlock rvBlock) {
        this.rs = rs;
        this.op = op;
        this.rd = rd;
        this.block = rvBlock;
    }
    public String toString() {
        return op + " " + rd + ", " + rs;
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
