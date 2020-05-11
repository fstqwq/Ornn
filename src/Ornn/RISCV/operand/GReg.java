package Ornn.RISCV.operand;

public class GReg extends Reg {
    public int size;
    public String name;
    public GReg(String name, int size) {
        this.size = size;
        this.name = name;
    }

}
