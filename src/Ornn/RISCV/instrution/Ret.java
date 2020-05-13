package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.RVRoot;
import Ornn.RISCV.operand.PReg;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

public class Ret extends RVInst {
    static PReg ra;
    public Ret(RVRoot root, RVBlock rvBlock) {
        ra = root.regMap.get("ra");
        this.block = rvBlock;
    }
    @Override
    public String toString() {
        return "ret";
    }
    @Override
    public HashSet<Reg> getUses() {
        return new HashSet<>() {{ add(ra); }};
    }
}
