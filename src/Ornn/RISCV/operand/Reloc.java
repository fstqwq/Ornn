package Ornn.RISCV.operand;

public class Reloc extends Imm {
    public enum RCategory {
        hi, lo,
    }
    GReg global;
    RCategory type;
    public Reloc(GReg global, RCategory type) {
        super(0);
        this.global = global;
        this.type = type;
    }
    @Override
    public String toString() {
        return "%" + type.toString() + "(" + global + ")";
    }
}
