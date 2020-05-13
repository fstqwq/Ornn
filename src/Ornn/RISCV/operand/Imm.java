package Ornn.RISCV.operand;

import Ornn.util.UnreachableError;

public class Imm {
    public int value;
    public Imm(int value) {
        if (value < -(1 << 11) || value >= (1 << 11)) {
            System.err.println(value);
            throw new UnreachableError();
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    public void applyStackOffset(int stackOffset) {

    }
}
