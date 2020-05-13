package Ornn.RISCV.operand;


public class VReg extends Reg {
    public int name, size;
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
