package Ornn.optim;

import Ornn.AST.util.Position;
import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.ConstBool;
import Ornn.IR.operand.Register;
import Ornn.util.CompilationError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CFGSimplification implements Pass {
    Root root;
    boolean modified;
    public CFGSimplification(Root root) {
        this.root = root;
    }

    void runForFunction(Function function) {
        boolean changed;
        do {
            changed = false;
            for (BasicBlock block : function.blocks) {
                if (block.successors.size() == 1 && block.successors.get(0).precursors.size() == 1 && !block.successors.get(0).equals(block)) {
                    changed = true;
                    BasicBlock next = block.successors.get(0);
                    next.phiInst.forEach((register, phi) -> {
                        register.replaceAll(phi.values.get(0));
                    });
                    next.phiInst.clear();
                    block.cleanUpTerminator();
                    block.merge(next);
                    function.blocks.remove(next);
                    if (next.equals(function.exitBlock)) {
                        function.exitBlock = block;
                    }
                    break;
                } else if (block.back instanceof Branch) {
                    Branch branch = (Branch) block.back;
                    if (branch.thenDest.equals(branch.elseDest)){
                        changed = true;
                        branch.cond.uses.remove(branch);
                        block.removeTerminator();
                        block.pushBack(new Jump(branch.thenDest, block));
                    } else if (branch.cond instanceof ConstBool) {
                        changed = true;
                        BasicBlock to = ((ConstBool) branch.cond).value ? branch.thenDest : branch.elseDest;
                        BasicBlock notTo = ((ConstBool) branch.cond).value ? branch.elseDest : branch.thenDest;
                        notTo.phiRemove(block);
                        block.removeTerminator();
                        block.pushBack(new Jump(to, block));
                        if (to.equals(function.exitBlock)) {
                            function.exitBlock = block;
                        }
                        if (notTo.equals(function.exitBlock) && function.exitBlock.precursors.isEmpty()) {
                            throw new CompilationError("unreachable return instruction in function " + function.name, Position.nowhere);
                        }
                    }
                }
            }
        } while (changed);
    }

    @Override
    public boolean run() {
        modified = false;
        root.functions.forEach(((s, function) -> runForFunction(function)));
        return modified;
    }
}
