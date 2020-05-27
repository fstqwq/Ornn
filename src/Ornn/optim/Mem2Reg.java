package Ornn.optim;

import Ornn.IR.BasicBlock;
import Ornn.IR.util.DominatorTreeBuilder;
import Ornn.IR.*;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.*;

import java.util.*;

/*
    Algorithm refer to SSA book's basic algorithm.
    Implementation refer to YongHao Zhuang's code and conversations, improved with path compression.
    Again, it's double plus ungood that Java has no reload of [].
 */


public class Mem2Reg implements Pass {
    Root root;
    HashSet<Register> allocVar;
    HashMap<Operand, Operand> fa;
    HashSet<BasicBlock> definitionBlocks;
    HashMap<BasicBlock, HashSet<Load>> allocLoad;
    HashMap<BasicBlock, HashMap<Register, Phi>> allocPhi;
    HashMap<BasicBlock, HashMap<Register, Operand>> allocStore;

    public Mem2Reg(Root root) {
        this.root = root;
    }

    Operand getFa(Operand from) {
        if (fa.containsKey(from)) {
            Operand to = fa.get(from);
            Operand toto = getFa(to);
            if (!to.equals(toto)) {
                fa.put(from, toto);
            }
            return toto;
        }
        return from;
    }
    void runForFunction(Function function) {
        DominatorTreeBuilder.runForFunction(function);

        allocVar = function.allocVar;
        allocLoad = new LinkedHashMap<>();
        allocPhi = new LinkedHashMap<>();
        allocStore = new LinkedHashMap<>();
        definitionBlocks = new LinkedHashSet<>();
        fa = new HashMap<>();

        for (BasicBlock block : function.blocks) {
            allocLoad.put(block, new LinkedHashSet<>());
            allocStore.put(block, new LinkedHashMap<>());
            allocPhi.put(block, new LinkedHashMap<>());
        }
        for (BasicBlock block : function.blocks) {
            for (Inst inst = block.front; inst != null ; inst = inst.next) {
                if (inst instanceof Load) {
                    Operand addr = ((Load) inst).addr;
                    if (addr instanceof Register && allocVar.contains(addr)) {
                        HashMap<Register, Operand> blockStore = allocStore.get(block);
                        if (blockStore.containsKey(addr)) {
                            fa.put(((Load) inst).dest, blockStore.get(addr));
                            inst.delete();
                        } else {
                            allocLoad.get(block).add((Load) inst);
                        }
                    }
                } else if (inst instanceof Store) {
                    Operand addr = ((Store) inst).addr;
                    if (addr instanceof Register && allocVar.contains(addr)) {
                        definitionBlocks.add(block);
                        allocStore.get(block).put((Register) addr, ((Store) inst).value);
                        inst.delete();
                    }
                }
            }
        }

        while (definitionBlocks.size() > 0) {
            HashSet<BasicBlock> candidates = definitionBlocks;
            definitionBlocks = new HashSet<>();
            for (BasicBlock candidate : candidates) {
                HashMap<Register, Operand> candidateStores = allocStore.get(candidate);
                for (BasicBlock domFrontier : candidate.domFrontier) {
                    for (Map.Entry<Register, Operand> e : candidateStores.entrySet()) {
                        Register key = e.getKey();
                        Operand val = e.getValue();
                        if (!allocPhi.get(domFrontier).containsKey(key)) {
                            Register dest = new Register("phi_" + key.name, val.type);
                            Phi phi = new Phi(dest, new ArrayList<>(), new ArrayList<>(), domFrontier);
                            domFrontier.pushFront(phi);
                            if (!allocStore.get(domFrontier).containsKey(key)) {
                                allocStore.get(domFrontier).put(key, dest);
                                definitionBlocks.add(domFrontier);
                            }
                            allocPhi.get(domFrontier).put(key, phi);
                        }
                    }
                }
            }
        }
        for (BasicBlock block : function.blocks) {
            if (!allocPhi.get(block).isEmpty()) {
                allocPhi.get(block).forEach((addr, phi) -> block.precursors.forEach(pre -> {
                    BasicBlock runner = pre;
                    while (!allocStore.get(runner).containsKey(addr)) {
                        runner = runner.iDom;
                    }
                    phi.add(allocStore.get(runner).get(addr), pre);
                }));
            }
            if (!allocLoad.get(block).isEmpty()) {
                for (Load load : allocLoad.get(block)) {
                    Register reg = load.dest;
                    Register from = (Register) load.addr;
                    Operand to;
                    if (allocPhi.get(block).containsKey(from)) {
                        to = allocPhi.get(block).get(from).dest;
                    } else {
                        BasicBlock cur = block.iDom;
                        while (!allocStore.get(cur).containsKey(from)) {
                            cur = cur.iDom;
                        }
                        to = allocStore.get(cur).get(from);
                    }
                    fa.put(reg, getFa(to));
                    load.delete();
                }
            }
        }
        fa.forEach((from, to) -> ((Register) from).replaceAll(getFa(to)));
        while (function.entryBlock.front instanceof Alloca) {
            function.entryBlock.front.delete();
        }
    }

    @Override
    public boolean run() {
        root.functions.forEach((s, function) -> runForFunction(function));
        return true;
    }
}
