package Ornn.RISCV.operand;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class Reg implements RVOperand {
    public int deg = 0;
    public float w = 0;
    public PReg color = null;
    public Imm spOffset = null;
    public HashSet<Reg> E = new LinkedHashSet<>();
    public HashSet<Reg> M = new LinkedHashSet<>();
    public Reg() {
        if (this instanceof PReg) color = (PReg) this;
    }
    public void reset() {
        w = 0;
        M.clear();
    }
}
