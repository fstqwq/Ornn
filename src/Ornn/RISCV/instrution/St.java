package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.GReg;
import Ornn.RISCV.operand.Imm;
import Ornn.RISCV.operand.Reg;
import Ornn.util.UnreachableError;

import java.util.HashSet;

public class St extends RVInst {
    public Reg rs1, rs2;
    public Imm offset;
    public int width;
    public St(Reg rs1, Imm offset, Reg rs2, int width, RVBlock block) {
        this.block = block;
        this.rs1 = rs1;
        this.offset = offset;
        this.rs2 = rs2;
        this.width = width;
    }
    @Override
    public String toString() {
        switch (width) {
            case 1:
                return "sb " + rs2 + ", " + (offset + "(" + rs1 + ")");
            case 4:
                return "sw " + rs2 + ", " + (offset + "(" + rs1 + ")");
        }
        throw new UnreachableError();
    }
    @Override
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{add(rs1); add(rs2);}};
    }
    @Override
    public void replaceUse(Reg old, Reg newReg) {
        if (rs1 == old) {
            rs1 = newReg;
        }
        if (rs2 == old) {
            rs2 = newReg;
        }
    }
    @Override
    public void applyStackOffset(int stackOffset) {
        offset.applyStackOffset(stackOffset);
    }
}
