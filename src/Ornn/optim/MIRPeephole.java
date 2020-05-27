package Ornn.optim;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.Global;
import Ornn.IR.operand.Register;
import Ornn.IR.util.DominatorTreeBuilder;

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
            HashMap <Global, Inst> globalLoadStore = new HashMap<>(); // globals never have aliases
            ArrayList <Inst> available = new ArrayList<>();
            Store protectedStore = null; // should not delete cross-block store
            if (block.precursors.contains(block.iDom) && block.precursors.size() == 1) {
                for (Inst inst = block.iDom.back.prev; inst != null; inst = inst.prev) {
                    if (inst instanceof Store) {
                        available.add(inst);
                        protectedStore = (Store) inst;
                        break;
                    } else if (inst instanceof Load) {
                        available.add(inst);
                    }
                }
            }
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Load ) {
                    if (((Load) inst).addr instanceof Global && globalLoadStore.containsKey((Global) ((Load) inst).addr) && !((Global) ((Load) inst).addr).isArray) {
                        Global global = (Global) ((Load) inst).addr;
                        Inst last = globalLoadStore.get(global);
                        if (last instanceof Load) {
                            ((Load) inst).dest.replaceAll(((Load)last).dest);
                        } else {
                            assert last instanceof Store;
                            ((Load) inst).dest.replaceAll(((Store)last).value);
                        }
                        changed = true;
                        inst.delete();
                    } else {
                        if (((Load) inst).addr instanceof Global) {
                            globalLoadStore.put((Global) ((Load) inst).addr, inst);
                        }
                        else {
                            boolean replaced = false;
                            for (Inst i : available) {
                                if (i instanceof Load) {
                                    if (((Load) i).addr.isSameWith(((Load) inst).addr)) {
                                        ((Load) inst).dest.replaceAll(((Load) i).dest);
                                        inst.delete();
                                        replaced = true;
                                        break;
                                    }
                                } else if (i instanceof Store) {
                                    if (((Store) i).addr.isSameWith(((Load) inst).addr)) {
                                        ((Load) inst).dest.replaceAll(((Store) i).value);
                                        inst.delete();
                                        replaced = true;
                                        break;
                                    }
                                }
                            }
                            if (!replaced) {
                                available.add(inst);
                            } else {
                                changed = true;
                            }
                        }
                    }
                } else if (inst instanceof Store) {
                    if (((Store) inst).addr instanceof Global && globalLoadStore.containsKey((Global) ((Store) inst).addr) && !((Global) ((Store) inst).addr).isArray) {
                        Global global = (Global) ((Store) inst).addr;
                        Inst last = globalLoadStore.get(global);
                        if (last instanceof Store && last != protectedStore) {
                            last.delete();
                            changed = true;
                        }
                    }
                    if (((Store) inst).addr instanceof Global) {
                        globalLoadStore.put((Global) ((Store) inst).addr, inst);
                    } else {
                        boolean replaced = false;
                        for (Inst i : available) {
                            if (i instanceof Store && i != protectedStore) {
                                if (((Store) i).addr.isSameWith(((Store) inst).addr)) {
                                    i.delete();
                                    replaced = true;
                                    break;
                                }
                            }
                        }
                        if (replaced) changed = true;
                        available.clear();
                        available.add(inst);
                    }
                } else if (inst instanceof Call) {
                    globalLoadStore.clear();
                    available.clear();
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
