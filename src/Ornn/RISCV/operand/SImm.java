package Ornn.RISCV.operand;

import Ornn.util.UnreachableError;

public class SImm extends Imm {
    boolean topDown, applied = false;
    public SImm(int value, boolean topDown) {
        super(value);
        this.topDown = topDown;
    }

    @Override
    public void applyStackOffset(int stackOffset) {
        if (!applied) {
            applied = true;
            value = stackOffset * (topDown ? -1 : 1) + value;
        }
    }
}
