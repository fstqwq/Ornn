package Ornn.optim;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.Global;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.type.Pointer;
import Ornn.IR.util.CallGraphUpdater;
import Ornn.IR.util.DominatorTreeBuilder;
import Ornn.RISCV.instrution.St;

import java.util.*;

/*
    Global 2 Register, for globals appeared some times in no-callee functions.
    Run before mem2reg.
    It must be better if the code is normal human code.
    Otherwise, **** code writers should go **** themselves.
 */

public class Global2Local implements Pass {
    Root root;
    HashMap<Global, Register> globalReg;
    HashSet<Inst> preserveInst;
    public Global2Local(Root root) {
        this.root = root;
    }

    void localizedGlobalDetect(Function function) {
        globalReg = new HashMap<>();
        preserveInst = new HashSet<>();
        if (function.blocks.size() < 2) {
            return;
        }
        HashMap<Global, Integer> cnt = new HashMap<>();
        for (BasicBlock block : function.blocks) {
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Load) {
                    if (((Load) inst).addr instanceof Global) {
                        Global global = (Global) ((Load) inst).addr;
                        if (!global.isArray) {
                            cnt.put(global, cnt.getOrDefault(global, 0) + 1);
                        }
                    }
                } else if (inst instanceof Store) {
                    if (((Store) inst).addr instanceof Global) {
                        Global global = (Global) ((Store) inst).addr;
                        if (!global.isArray) {
                            cnt.put(global, cnt.getOrDefault(global, 0) + 1);
                        }
                    }
                }
            }
        }
        ArrayList <Inst> entryAdd = new ArrayList<>();
        int threshold = -1, inlinedCnt;
        do {
            threshold++;
            inlinedCnt = 0;
            for (Map.Entry<Global, Integer> entry : cnt.entrySet()) {
                if (entry.getValue() > threshold) {
                    inlinedCnt++;
                }
            }
        } while (inlinedCnt > 8);
        for (Map.Entry<Global, Integer> entry : cnt.entrySet()) {
            Global global = entry.getKey();
            Integer integer = entry.getValue();
            if (integer > threshold) {
                Register addr = new Register("_addr_" + global.name, global.type);
                Register loadTmp = new Register("_ld_" + global.name, ((Pointer) global.type).typePointedTo);
                Load load = new Load(loadTmp, global, function.entryBlock);
                Store store = new Store(addr, loadTmp, function.entryBlock);
                function.allocVar.add(addr);
                function.entryBlock.pushFront(new Alloca(addr, function.entryBlock));
                entryAdd.add(load);
                entryAdd.add(store);
                Register loadTmpEnd = new Register("_lde_" + global.name, ((Pointer) global.type).typePointedTo);
                Load loadEnd = new Load(loadTmpEnd, addr, function.exitBlock);
                Store storeEnd = new Store(global, loadTmpEnd, function.exitBlock);
                function.exitBlock.back.insertBefore(loadEnd);
                function.exitBlock.back.insertBefore(storeEnd);
                globalReg.put(global, addr);
                preserveInst.add(load);
                preserveInst.add(storeEnd);
            }
        }
        if (entryAdd.isEmpty()) return;
        for (Inst inst = function.entryBlock.front; inst != null; inst = inst .next) {
            if (!(inst instanceof Alloca) && !(inst.prev instanceof Alloca)) {
                for (Inst inst1 : entryAdd) {
                    inst.insertBefore(inst1);
                }
                break;
            }
        }
    }

    void runForFunction(Function function) {
        localizedGlobalDetect(function);
        if (globalReg.isEmpty()) return;
        for (BasicBlock block : function.blocks) {
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Load) {
                    if (((Load) inst).addr instanceof Global) {
                        Global global = (Global) ((Load) inst).addr;
                        if (globalReg.containsKey(global) && !preserveInst.contains(inst)) {
                            ((Load) inst).addr = globalReg.get(global);
                        }
                    }
                } else if (inst instanceof Store) {
                    if (((Store) inst).addr instanceof Global) {
                        Global global = (Global) ((Store) inst).addr;
                        if (globalReg.containsKey(global) && !preserveInst.contains(inst)) {
                            ((Store) inst).addr = globalReg.get(global);
                        }
                    }
                }
            }
        }
/*        System.err.println("==========" + function.name + "==========");
        for (Inst inst = function.entryBlock.front; inst != null; inst = inst.next) {
            System.err.println(inst);
        }
        System.err.println("^^^^^^^^^^" + function.name + "^^^^^^^^^^");*/
    }

    @Override
    public boolean run() {
        /*
        * SUPERNATURAL EVENT HERE:
        * Replace for with forEach, run on WSL, jvav version :  14 2020-03-17
        * It will cause a null pointer exception in SSA destruction! WTF is that ?
        * */
        for (Map.Entry<String, Function> entry : root.functions.entrySet()) {
            Function function = entry.getValue();
            if (function.callee != null && function.callee.isEmpty()) {
                runForFunction(function);
            }
        }
        return true;
    }
}
