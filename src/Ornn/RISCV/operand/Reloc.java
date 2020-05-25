package Ornn.RISCV.operand;

public class Reloc extends Imm {
    public enum RCategory {
        hi, lo,
    }
    public GReg global;
    public RCategory type;
    public Reloc(GReg global, RCategory type) {
        super(0);
        this.global = global;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Reloc && ((Reloc) obj).global.equals(global) && ((Reloc) obj).type.equals(type);
    }

    @Override
    public int hashCode() {
        return global.hashCode() ^ type.hashCode();
    }

    @Override
    public String toString() {
        return "%" + type.toString() + "(" + global + ")";
    }
}
