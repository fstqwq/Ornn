package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.operand.*;

import java.util.HashSet;

public class IType extends RVInst {
    public Reg rs, rd;
    public Imm imm;
    public SCategory op;
    public IType(Reg rs, Imm imm, SCategory op, Reg rd, RVBlock rvBlock) {
        this.rs = rs;
        this.imm = imm;
        this.op = op;
        this.rd = rd;
        this.block = rvBlock;
    }
    @Override
    public String toString() {
        return op + "i " + rd + ", " + rs + ", " + imm.value;
    }
    @Override
    public HashSet<Reg> getUses() {
        return new HashSet<>(){{ add(rs); }};
    }
    @Override
    public HashSet<Reg> getDefs() {
        return new HashSet<>() {{ add(rd); }};
    }
    @Override
    public void replaceUse(Reg old, Reg newReg) {
        if (rs == old) {
            rs = newReg;
        } else {
            assert false;
        }
    }

    @Override
    public void replaceRd(Reg old, Reg newReg) {
        if (rd == old) {
            rd = newReg;
        }
    }
    @Override
    public void applyStackOffset(int stackOffset) {
        imm.applyStackOffset(stackOffset);
    }
}
