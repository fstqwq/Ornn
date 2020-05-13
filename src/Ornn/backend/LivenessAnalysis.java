package Ornn.backend;

import Ornn.RISCV.RVBlock;
import Ornn.RISCV.RVFunction;
import Ornn.RISCV.instrution.RVInst;
import Ornn.RISCV.operand.Reg;

import java.util.HashMap;
import java.util.HashSet;

public class LivenessAnalysis {

    static HashMap<RVBlock, HashSet<Reg>> blockUses;
    static HashMap<RVBlock, HashSet<Reg>> blockDefs;
    static HashSet<RVBlock> visited;

    static void runForBlock(RVBlock block) {
        HashSet<Reg> uses = new HashSet<>();
        HashSet<Reg> defs = new HashSet<>();
        for (RVInst inst = block.front; inst != null; inst = inst.next) {
            HashSet<Reg> instUses = inst.getUses();
            instUses.removeAll(defs);
            uses.addAll(instUses);
            defs.addAll(inst.getDefs());
        }
        blockUses.put(block, uses);
        blockDefs.put(block, defs);
        block.liveIn = new HashSet<>();
        block.liveOut = new HashSet<>();
    }


    static void runBackward(RVBlock block) {
        if (visited.contains(block)) return;
        visited.add(block);
        HashSet<Reg> liveOut = new HashSet<>();
        for (RVBlock successor : block.successors) {
            liveOut.addAll(successor.liveIn);
        }
        HashSet<Reg> liveIn = new HashSet<>(liveOut);
        liveIn.removeAll(blockDefs.get(block));
        liveIn.addAll(blockUses.get(block));
        block.liveOut.addAll(liveOut);
        liveIn.removeAll(block.liveIn);
        if (!liveIn.isEmpty()) {
            block.liveIn.addAll(liveIn);
            visited.removeAll(block.precursors);
        }
        for (RVBlock precursor : block.precursors) {
            runBackward(precursor);
        }
    }

    public static void runForFunction(RVFunction function) {
        blockUses = new HashMap<>();
        blockDefs = new HashMap<>();
        visited = new HashSet<>();
        function.blocks.forEach(LivenessAnalysis::runForBlock);
        runBackward(function.exitBlock);
    }
}
