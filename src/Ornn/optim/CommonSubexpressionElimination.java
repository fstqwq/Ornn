package Ornn.optim;

import Ornn.CompileParameter;
import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.Call;
import Ornn.IR.instruction.Inst;
import Ornn.IR.instruction.Phi;
import Ornn.IR.operand.Register;
import Ornn.IR.util.BlockGraphUpdater;
import Ornn.IR.util.DominatorTreeBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


public class CommonSubexpressionElimination implements Pass {
    // warning : may cause too long live range
    Root root;
    public CommonSubexpressionElimination(Root root) {
        this.root = root;
    }

    boolean modified;
    BasicBlock currentBlock;

    void globalCSE(BasicBlock block, int numInst, ArrayList<Inst> appearedInst) {
        for (Inst inst = block.front; inst != null && numInst++ < CompileParameter.CSEInstLimit; inst = inst.next) {
            if (!inst.hasSideEffect()) {
                for (Inst i : appearedInst) {
                    if (i.isSameWith(inst)) {
                        inst.getDest().replaceAll(i.getDest()); // must have a result
                        inst.delete();
                    }
                }
            } else if (inst instanceof Call) {
                numInst += CompileParameter.crossCallPenalty;  // across call && block, too costly
            }
        }
        if (numInst < CompileParameter.CSEInstLimit) {
            for (BasicBlock successor : block.successors) {
                if (successor.isDomedBy(currentBlock)) {
                    globalCSE(successor, numInst, appearedInst);
                }
            }
        }
    }

    void runForBlock(BasicBlock block) {
        boolean changed;
        do {
            changed = false;
            HashSet <Phi> appearedPhi = new HashSet<>();
            for (Iterator<Map.Entry<Register, Phi>> iter = block.phiInst.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry<Register, Phi> entry = iter.next();
                Phi phi = entry.getValue();
                boolean flag = false;
                for (Phi i : appearedPhi) {
                    if (i.isSameWith(phi)) {
                        phi.dest.replaceAll(i.dest);
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    changed = true;
                    phi.delete();
                    iter.remove();
                } else {
                    appearedPhi.add(phi);
                }
            }
            ArrayList<Inst> appearedInst = new ArrayList<>();
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (!inst.hasSideEffect()) {
                    boolean flag = false;
                    for (Inst i : appearedInst) {
                        if (i.isSameWith(inst)) {
                            flag = true;
                            inst.getDest().replaceAll(i.getDest()); // must have a result
                            inst.delete();
                            break;
                        }
                    }
                    if (!flag) appearedInst.add(inst);
                    else changed = true;
                }
            }
            currentBlock = block;
            block.successors.forEach(successor -> {
                if (successor.isDomedBy(block)) {
                    globalCSE(successor, 0, appearedInst);
                }
            });
            modified |= changed;
        } while (changed);
    }



    void runForFunction(Function function) {
        DominatorTreeBuilder.runForFunction(function);
        function.blocks.forEach(this::runForBlock);
    }

    @Override
    public boolean run() {
        modified = false;
        root.functions.forEach((s, function) -> runForFunction(function));
        return modified;
    }
}
