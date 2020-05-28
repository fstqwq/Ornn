package Ornn.RISCV.operand;


public class VReg extends Reg {
    public int name, size;
    public boolean isImm;
    public int imm;
    public VReg(int name, int size) {
        this.name = name;
        this.size = size;
    }
    @Override
    public String toString() {
        if (color == null) {
            return "%" + name;
        } else {
            return color.toString();
        }
    }
}
