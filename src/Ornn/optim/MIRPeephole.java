package Ornn.optim;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.Call;
import Ornn.IR.instruction.Inst;
import Ornn.IR.instruction.Load;
import Ornn.IR.instruction.Store;
import Ornn.IR.operand.Global;
import Ornn.IR.operand.Register;

import java.util.*;

public class MIRPeephole implements Pass {
    Root root;
    public MIRPeephole(Root root) {
        this.root = root;
    }

    boolean modified;

    void runForBlock(BasicBlock block) {
        boolean changed;
        do {
            changed = false;
            HashMap <Global, Inst> lastLoadStore = new HashMap<>();
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Load ) {
                    if (((Load) inst).addr instanceof Global && lastLoadStore.containsKey((Global) ((Load) inst).addr) && !((Global) ((Load) inst).addr).isArray) {
                        Global global = (Global) ((Load) inst).addr;
                        Inst last = lastLoadStore.get(global);
                        if (last instanceof Load) {
                            ((Load) inst).dest.replaceAll(((Load)last).dest);
                        } else {
                            assert last instanceof Store;
                            ((Load) inst).dest.replaceAll(((Store)last).value);
                        }
                        changed = true;
                        inst.delete();
                    } else {
                        if (((Load) inst).addr instanceof Global) lastLoadStore.put((Global) ((Load) inst).addr, inst);
                    }
                } else if (inst instanceof Store) {
                    if (((Store) inst).addr instanceof Global && lastLoadStore.containsKey((Global) ((Store) inst).addr) && !((Global) ((Store) inst).addr).isArray) {
                        Global global = (Global) ((Store) inst).addr;
                        Inst last = lastLoadStore.get(global);
                        if (last instanceof Store && ((Store) last).value instanceof Register) {
                            last.delete();
                            changed = true;
                        }
                    }
                    if (((Store) inst).addr instanceof Global) {
                        lastLoadStore.put((Global) ((Store) inst).addr, inst);
                    }
                } else if (inst instanceof Call) {
                    lastLoadStore.clear();
                }
            }
            modified |= changed;
        } while (changed);
    }

    void runForFunction(Function function) {
        function.blocks.forEach(this::runForBlock);
    }

    @Override
    public boolean run() {
        modified = false;
        root.functions.forEach((s, function) -> runForFunction(function));
        return modified;
    }
}
