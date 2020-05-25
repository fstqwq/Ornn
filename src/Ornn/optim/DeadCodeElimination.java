package Ornn.optim;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.Register;

import java.util.Iterator;
import java.util.Map;

/*
    ~Aggressive~ Naive Dead Code Eliminator

    ~All code are created dead. Then revive those are escaped.~
    Simply fuck those result is not used
 */


public class DeadCodeElimination implements Pass {
    Root root;
    boolean modified;
    public DeadCodeElimination(Root root) {
        this.root = root;
    }

    void kill() {
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, Function> e : root.functions.entrySet()) {
                String s = e.getKey();
                Function function = e.getValue();
                for (BasicBlock block : function.blocks) {
                    for (Iterator<Map.Entry<Register, Phi>> iter = block.phiInst.entrySet().iterator(); iter.hasNext(); ) {
                        Map.Entry<Register, Phi> entry = iter.next();
                        if (entry.getKey().uses.size() == 0) {
                            iter.remove();
                            changed = true;
                        }
                    }
                    for (Inst inst = block.front; inst != null; inst = inst.next) {
                        if (inst instanceof Load
                                || inst instanceof GEP
                                || inst instanceof Binary
                                || inst instanceof Cast
                                || inst instanceof Cmp
                                || inst instanceof Malloc
                                || (inst instanceof Call && root.isBuiltin(((Call) inst).callee.name) && !((Call) inst).callee.hasSideEffect)
                        ) {
                            Register cur = inst.getDest();
                            if (cur == null || cur.uses.size() == 0) {
                                inst.delete();
                                changed = true;
                            }
                        }
                    }
                }
                for (Iterator<BasicBlock> iter = function.blocks.iterator(); iter.hasNext(); ) {
                    BasicBlock basicBlock = iter.next();
                    if (basicBlock.precursors.size() == 0 && !basicBlock.equals(function.entryBlock)) {
                        basicBlock.cleanUpTerminator();
                        iter.remove();
                    } else if (basicBlock.precursors.size() == 1 && basicBlock.successors.size() == 1 && basicBlock.successors.get(0).equals(basicBlock)) { // dead while true
                        iter.remove();
                    }
                }
            }
            modified |= changed;
        } while (changed);
    }

    @Override
    public boolean run() {
        modified = false;
        kill();
        return modified;
    }
}
