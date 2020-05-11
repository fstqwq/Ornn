package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class Br extends RVInst {
    public Reg rs1, rs2;
    public BCategory op;
    public RVBlock offset;
    public Br(Reg rs1, Reg rs2, BCategory op, RVBlock offset, RVBlock rvBlock) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.op = op;
        this.offset = offset;
        this.block = rvBlock;
    }
    public String toString() {
        return "b" + op + " " + rs1 + ", " + rs2 + ", " + offset;
    }
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{add(rs1); add(rs2);}};
    }
    public void replaceUse(Reg old, Reg newReg) {
        boolean success = false;
        if (rs1 == old) {
            rs1 = newReg;
            success = true;
        }
        if (rs2 == old) {
            rs2 = newReg;
            success = true;
        }
        assert success;
    }
}
