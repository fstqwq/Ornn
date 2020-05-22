package Ornn.RISCV;

import Ornn.RISCV.operand.Reg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class RVFunction {
    public String name;
    public HashSet<RVBlock> blocks = new LinkedHashSet<>();
    public ArrayList<Reg> params = new ArrayList<>();
    public RVBlock entryBlock;
    public RVBlock tailCallEntryBlock;
    public RVBlock exitBlock;
    public RVFunction(String name) {
        this.name = name;
    }
    public int vRegCount, paramInStackOffset;
    @Override
    public String toString() {
        return this.name;
    }
}
