package Ornn.backend;

import Ornn.RISCV.*;
import Ornn.RISCV.instrution.*;
import Ornn.RISCV.operand.*;

import java.util.*;

public class Peephole {
    static RVRoot root;
    static void removeIdMove(RVFunction function) {
        for (RVBlock block : function.blocks) {
            for (RVInst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Mv && ((Mv) inst).rs.color.equals(((Mv) inst).rd.color)) {
                    inst.delete();
                } else if (inst instanceof IType
                        && (((IType) inst).op == RVInst.SCategory.add
                            || ((IType) inst).op == RVInst.SCategory.or
                            || ((IType) inst).op == RVInst.SCategory.xor
                            || ((IType) inst).op == RVInst.SCategory.sll
                            || ((IType) inst).op == RVInst.SCategory.sra
                        )
                        && ((IType) inst).rd.color.equals(((IType) inst).rs.color) && ((IType) inst).imm.value == 0) {
                    inst.delete();
                } else if (inst instanceof RType
                        && (((RType) inst).op == RVInst.SCategory.add
                            || ((RType) inst).op == RVInst.SCategory.or
                            || ((RType) inst).op == RVInst.SCategory.xor
                            || ((RType) inst).op == RVInst.SCategory.sll
                            || ((RType) inst).op == RVInst.SCategory.sra
                        )
                        && (
                                ((RType) inst).rd.color.equals(((RType) inst).rs1.color) && ((RType) inst).rs2.color == root.pRegs.get(0)
                        )) {
                    inst.delete();
                }
            }
        }
    }
    static void redundantEliminator(RVFunction function) {
        // hard to implement, give up
        // MIR peephole did (most of) it, so it's now not required that much
    }

    static void combineBlocks(RVFunction function) {
        // for redundant eliminator, but now useless (we have reschedule!)
        boolean updated;
        do {
            updated = false;
            for (RVBlock block : function.blocks) {
                if (block.back instanceof Jmp
                && ((Jmp) block.back).offset.precursors.size() == 1
                && !((Jmp) block.back).offset.equals(block)) {
                    RVBlock next = ((Jmp) block.back).offset;
                    block.successors.clear();
                    next.precursors.clear();
                    block.back.delete();
                    ArrayList <RVInst> tmp = new ArrayList<>();
                    for (RVInst inst = next.front; inst != null; inst = inst.next) {
                        tmp.add(inst);
                    }
                    for (RVInst inst : tmp) {
                        inst.block = block;
                        block.add(inst);
                    }
                    next.successors.forEach(x -> {
                        x.precursors.remove(next);
                        x.precursors.add(block);
                        block.successors.add(x);
                    });
                    function.blocks.remove(next);
                    updated = true;
                    break;
                }
            }
        } while (updated);
    }
    public static void run(RVRoot root) {
        Peephole.root = root;
        root.functions.forEach(Peephole::removeIdMove);
        root.functions.forEach(Peephole::combineBlocks);
        root.functions.forEach(Peephole::redundantEliminator);
    }
}
