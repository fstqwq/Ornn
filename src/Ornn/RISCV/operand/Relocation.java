package Ornn.RISCV.operand;

public class Relocation extends Imm {
    public enum RCategory {
        hi, lo,
    }
    GReg global;
    RCategory type;
    public Relocation(GReg global, RCategory type) {
        super(0);
        this.global = global;
        this.type = type;
    }
    @Override
    public String toString() {
        return "%" + type.toString() + "(" + global + ")";
    }
}
