package Ornn.RISCV.operand;

public class SImm extends Imm {
    boolean topDown, applied = false;
    public SImm(int value, boolean topDown) {
        super(value);
        this.topDown = topDown;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof SImm && ((SImm) obj).value == value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public void applyStackOffset(int stackOffset) {
        if (!applied) {
            applied = true;
            value = stackOffset * (topDown ? -1 : 1) + value;
        }
    }
}
