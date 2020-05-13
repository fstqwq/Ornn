package Ornn.RISCV;

import Ornn.RISCV.instrution.RVInst;
import Ornn.RISCV.operand.Reg;

import java.util.ArrayList;
import java.util.HashSet;

public class RVBlock {
    public RVInst front;
    public RVInst back;
    public String name;
    public ArrayList<RVBlock> precursors = new ArrayList<>();
    public ArrayList<RVBlock> successors = new ArrayList<>();
    public HashSet<Reg> liveIn;
    public HashSet<Reg> liveOut;

    public String comment;

    public RVBlock(String name) {
        this.name = name;
    }
    public void add(RVInst inst) {
        if (back == null) {
            front = back = inst;
        }
        else {
            back.insertAfter(inst);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
