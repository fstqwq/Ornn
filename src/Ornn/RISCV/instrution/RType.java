package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class RType extends RVInst {
    public Reg rs1, rs2, rd;
    public SCategory op;
    public RType(Reg rs1, Reg rs2, SCategory op, Reg rd, RVBlock rvBlock) {
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.op = op;
        this.rd = rd;
        this.block = rvBlock;
    }
    @Override
    public String toString() {
        return op + " " + rd + ", " + rs1 + ", " + rs2;
    }
    @Override
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{add(rs1); add(rs2);}};
    }
    @Override
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
    public RVInst getCopy() {
        return new RType(rs1, rs2, op, rd, block);
    }
}
