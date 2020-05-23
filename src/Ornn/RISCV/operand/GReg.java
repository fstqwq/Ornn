package Ornn.RISCV.operand;

import java.util.ArrayList;

public class GReg extends Reg {
    public int size;
    public String name;
    public boolean isArray;
    public ArrayList<String> initialization;

    public GReg(String name, int size) {
        this.size = size;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
