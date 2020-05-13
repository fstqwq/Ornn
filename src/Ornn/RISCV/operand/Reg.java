package Ornn.RISCV.operand;

import Ornn.RISCV.instrution.Mv;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class Reg implements RVOperand {
    public int degree = 0;
    public float weight = 0;
    public PReg color = null;
    public Reg alias = null;
    public Imm stackOffset = null;
    public HashSet<Reg> adjList = new LinkedHashSet<>();
    public HashSet<Mv> moveList = new LinkedHashSet<>();
    public Reg() {
        if (this instanceof PReg) color = (PReg) this;
    }
    static int INF = 0x3f3f3f3f;
    public void reset() {
        weight = 0;
        degree = 0;
        color = null;
        alias = null;
        adjList.clear();
        moveList.clear();
    }
    public void setPreColored() {
        weight = 0;
        degree = INF;
        color = (PReg) this;
        alias = null;
        adjList.clear();
        moveList.clear();
    }
    public void addAdj(Reg reg) {
        adjList.add(reg);
        degree++;
    }
}
