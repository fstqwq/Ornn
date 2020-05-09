package Ornn.optim;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.Branch;
import Ornn.IR.instruction.Jump;
import Ornn.IR.instruction.Move;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/*
    SSA Book ** ** is good
 */

public class SSADestruction implements Pass {
    Root root;

    public SSADestruction(Root root) {
        this.root = root;
    }

    static class Edge {
        public BasicBlock u, v;
        public Edge (BasicBlock u, BasicBlock v) {
            this.u = u;
            this.v = v;
        }
    }
    static class ParallelCopy {
        public ArrayList<Move> copies = new ArrayList<>();
        public HashMap<Operand, Integer> useCnt = new HashMap<>();
        public void add(Move move) {
            copies.add(move);
            if (move.src instanceof Register) {
                useCnt.put(move.src, useCnt.getOrDefault(move.src, 0) + 1);
            }
        }
    }

    HashSet <Edge> criticalEdges;
    HashMap <BasicBlock, ParallelCopy> pCopies;

    boolean blockCondition(ParallelCopy pCopy) {
        for (Move copy : pCopy.copies) {
            if (!copy.src.equals(copy.dest)) {
                return true;
            }
        }
        return false;
    }

    void runForBlock(BasicBlock block) {
        ParallelCopy pCopy = pCopies.get(block);

        for ( ; blockCondition(pCopy); ) {
            boolean breakLoop = true;
            for (Iterator<Move> iter = pCopy.copies.iterator(); iter.hasNext(); ) {
                Move inst = iter.next();
                if (pCopy.useCnt.getOrDefault(inst.dest, 0) == 0) {
                    iter.remove();
                    if (inst.src instanceof Register) {
                        pCopy.useCnt.put(inst.src, pCopy.useCnt.get(inst.src) - 1);
                    }
                    inst.completeConstruction();
                    block.back.insertBefore(inst);
                    breakLoop = false;
                }
            }
            if (breakLoop) {
                for (Move copy : pCopy.copies) {
                    if (copy.src != copy.dest) {
                        Register tmp = new Register(copy.src.name + "_copy", copy.src.type);
                        Move cp = new Move(copy.dest, copy.src, block);
                        cp.completeConstruction();;
                        block.back.insertBefore(cp);
                        pCopy.useCnt.remove(copy.src);
                        for (Move move : pCopy.copies) {
                            move.replaceUse((Register) copy.src, tmp);
                        }
                        break;
                    }
                }
            }
        }
    }

    BasicBlock jumpRedirect(BasicBlock block) {
        if (block.front != block.back || !(block.back instanceof Jump)) return block;
        Jump jump = (Jump) block.back;
        BasicBlock dest = jumpRedirect(jump.dest);
        if (!jump.dest.equals(dest)) {
            jump.redirect(jump.dest, dest);
        }
        return dest;
    }

    void runForFunction(Function function) {
        criticalEdges = new HashSet<>();
        for (BasicBlock block : function.blocks) {
            if (block.successors.size() > 1) {
                for (BasicBlock successor : block.successors) {
                    if (successor.precursors.size() > 1) {
                        criticalEdges.add(new Edge(block, successor));
                    }
                }
            }
        }
        for (Edge e : criticalEdges) {
            BasicBlock mid = new BasicBlock(function, "mid");
            function.blocks.add(mid);
            mid.pushBack(new Jump(e.v, mid));
            e.v.phiInst.forEach( (register, phi) -> {
                for (int i = 0; i < phi.blocks.size(); i++) {
                    if (phi.blocks.get(i) == e.u) {
                        phi.blocks.set(i, mid);
                    }
                }
            });
            e.u.redirect(e.v, mid);
        }
        pCopies = new HashMap<>();
        for (BasicBlock block : function.blocks) {
            pCopies.put(block, new ParallelCopy());
        }
        for (BasicBlock block : function.blocks) {
            block.phiInst.forEach((register, phi) -> {
                for (int i = 0; i < phi.blocks.size(); i++) {
                    BasicBlock from = phi.blocks.get(i);
                    Operand operand = phi.values.get(i);
                    pCopies.get(from).add(new Move(register, operand, from));
                }
            });
        }
        for (BasicBlock block : function.blocks) {
            runForBlock(block);
        }

        for (BasicBlock block : function.blocks) {
            if (block.back instanceof Jump) {
                BasicBlock dest = jumpRedirect(((Jump) block.back).dest);
                if (!((Jump) block.back).dest.equals(dest)) {
                    ((Jump) block.back).dest.redirect(((Jump) block.back).dest, dest);
                }
            } else if (block.back instanceof Branch) {
                BasicBlock thenDest = jumpRedirect(((Branch) block.back).thenDest);
                if (!((Branch) block.back).thenDest.equals(thenDest)) {
                    ((Branch) block.back).thenDest.redirect(((Branch) block.back).thenDest, thenDest);
                }
                BasicBlock elseDest = jumpRedirect(((Branch) block.back).elseDest);
                if (!((Branch) block.back).elseDest.equals(elseDest)) {
                    ((Branch) block.back).elseDest.redirect(((Branch) block.back).elseDest, elseDest);
                }
            }
        }
        function.blocks.removeIf(cur -> !cur.equals(function.entryBlock) && cur.precursors.size() == 0);
        function.blocks.forEach(cur -> cur.phiInst.clear());
    }


    @Override
    public boolean run() {
        root.functions.forEach(((s, function) -> runForFunction(function)));
        return true;
    }
}
