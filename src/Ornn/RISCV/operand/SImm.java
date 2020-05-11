package Ornn.RISCV.operand;

import Ornn.util.UnreachableError;

public class SImm extends Imm {
    boolean topDown;
    public SImm(int value, boolean topDown) {
        super(value);
        this.topDown = topDown;
    }
}
