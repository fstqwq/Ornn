package Ornn.RISCV.instrution;


import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

/*
    Why we need this but not use sltiu / sltu directly?

    Cause Mx* only cares about signed integers,
    it's costly if we implement unsigned operation for two operation.

 */


public class Sz extends RVInst {
    public Reg rs, rd;
    public SzCategory op;
    public Sz(Reg rs, SzCategory op, Reg rd, RVBlock rvBlock) {
        this.rs = rs;
        this.op = op;
        this.rd = rd;
        this.block = rvBlock;
    }
    @Override
    public String toString() {
        return op + " " + rd + ", " + rs;
    }
    @Override
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{add(rs);}};
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
}
