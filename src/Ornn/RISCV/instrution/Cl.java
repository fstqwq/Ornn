package Ornn.RISCV.instrution;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.RVFunction;
import Ornn.RISCV.RVRoot;
import Ornn.RISCV.operand.Reg;

import java.util.HashSet;

import static java.lang.Integer.min;

public class Cl extends RVInst {
    public RVFunction callee;
    public RVRoot rootInfo;

    public Cl(RVRoot rootInfo, RVFunction callee, RVBlock block) {
        this.callee = callee;
        this.rootInfo = rootInfo;
        this.block = block;
    }

    @Override
    public String toString() {
        return "ret " + callee;
    }

    @Override
    public HashSet<Reg> getUses() {
        return new HashSet<>() {{
            for (int i = 0; i < min(callee.params.size(), 8); i++) {
                add(rootInfo.pRegs.get(10 + i));
            }
        }};
    }

    @Override
    public void replaceUse(Reg old, Reg newReg) {
        assert false;
    }
}
