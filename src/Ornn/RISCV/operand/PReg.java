package Ornn.RISCV.operand;

public class PReg extends Reg {
    public String name;
    public PReg(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
