package Ornn.optim;

import Ornn.CompileParameter;
import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.Global;

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
            HashMap <Inst, Integer> available = new HashMap<>(); // used as pair, store the time stamp
            ArrayList<Store> protectedStore = new ArrayList<>(); // should not delete cross-block store
            int timeStamp = 0;
            if (block.precursors.contains(block.iDom) && block.precursors.size() == 1) {
                for (Inst inst = block.iDom.back.prev; inst != null; inst = inst.prev) {
                    timeStamp++;
                    if (inst instanceof Store) {
                        boolean collision = false;
                        for (Store store : protectedStore) {
                            if (store.addr.type.isSameWith(((Store) inst).addr.type)) {
                                collision = true;
                                break;
                            }
                        }
                        if (collision) continue;
                        available.put(inst, timeStamp);
                        protectedStore.add((Store) inst);
                    } else if (inst instanceof Load) {
                        boolean collision = false;
                        for (Store store : protectedStore) {
                            if (store.addr.type.isSameWith(((Load) inst).addr.type)) {
                                collision = true;
                                break;
                            }
                        }
                        if (collision) continue;
                        available.put(inst, timeStamp);
                    } else if (inst instanceof Call) {
                        break;
                    }
                }
            }
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                timeStamp++;
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
                            for (Iterator<Map.Entry<Inst, Integer>> iter = available.entrySet().iterator(); iter.hasNext(); ) {
                                Map.Entry<Inst, Integer> entry = iter.next();
                                if (entry.getValue() - timeStamp > CompileParameter.MIRPeepholeMaxGap) {
                                    iter.remove();
                                } else {
                                    Inst i = entry.getKey();
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
                            }
                            if (!replaced) {
                                available.put(inst, timeStamp);
                            } else {
                                changed = true;
                            }
                        }
                    }
                } else if (inst instanceof Store) {
                    if (((Store) inst).addr instanceof Global && globalLoadStore.containsKey((Global) ((Store) inst).addr) && !((Global) ((Store) inst).addr).isArray) {
                        Global global = (Global) ((Store) inst).addr;
                        Inst last = globalLoadStore.get(global);
                        if (last instanceof Store && !protectedStore.contains(last)) {
                            last.delete();
                            changed = true;
                        }
                    }
                    if (((Store) inst).addr instanceof Global) {
                        globalLoadStore.put((Global) ((Store) inst).addr, inst);
                    } else {
                        boolean replaced = false;
                        for (Iterator<Map.Entry<Inst, Integer>> iter = available.entrySet().iterator(); iter.hasNext(); ) {
                            Map.Entry<Inst, Integer> entry = iter.next();
                            if (entry.getValue() - timeStamp > CompileParameter.MIRPeepholeMaxGap) {
                                iter.remove();
                            } else {
                                Inst i = entry.getKey();
                                if (i instanceof Store && !protectedStore.contains(i)) {
                                    if (((Store) i).addr.isSameWith(((Store) inst).addr)) {
                                        i.delete();
                                        iter.remove();
                                        replaced = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (replaced) changed = true;
                        for (Iterator<Map.Entry<Inst, Integer>> iter = available.entrySet().iterator(); iter.hasNext(); ) {
                            Map.Entry<Inst, Integer> entry = iter.next();
                            Inst cur = entry.getKey();
                            if (cur instanceof Store) {
                                if (((Store) cur).addr.type.isSameWith(((Store) inst).addr.type)) {
                                    iter.remove();
                                }
                            } else if (cur instanceof Load) {
                                if (((Load) cur).addr.type.isSameWith(((Store) inst).addr.type)) {
                                    iter.remove();
                                }
                            }
                        }
                        available.put(inst, timeStamp);
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
