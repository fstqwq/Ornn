package Ornn.optim;

import Ornn.CompileParameter;
import Ornn.IR.*;
import Ornn.IR.instruction.*;
import Ornn.IR.util.CallGraphUpdater;
import Ornn.IR.util.DominatorTreeBuilder;
import Ornn.IR.util.FunctionBlockCollector;
import Ornn.IR.util.IRReplicator;

import java.util.*;

public class Inlining implements Pass {
    Root root;
    boolean updated = false, forced = false;
    public Inlining(Root root) {
        this.root = root;
    }

    HashSet<Function> canInline = new HashSet<>();
    HashMap<Function, Integer> numberOfInst = new HashMap<>();

    void recollectInfo(Function function) {
        int cnt = 0;
        for (BasicBlock block : function.blocks) {
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                cnt++;
            }
        }
        numberOfInst.put(function, cnt);
    }

    void updateCallGraph() {
        CallGraphUpdater.run(root, true);
        numberOfInst.clear();
        root.functions.forEach(((s, function) -> {
            recollectInfo(function);
        }));
        root.builtinFunctions.forEach(((s, function) -> {
            numberOfInst.put(function, CompileParameter.inlineInstLimit);
        }));
    }

    Function currentFunction;
    BasicBlock currentBlock;

    boolean doInline(Call call, Function caller) {
        currentFunction = caller;
        currentBlock = call.basicBlock;
        IRReplicator replicator = new IRReplicator();
        Function callee = call.callee;
        if (numberOfInst.get(caller) + numberOfInst.get(callee) >= CompileParameter.inlineInstLimit) {
            return false;
        }
        for (int i = 0; i < call.params.size(); i++) {
            replicator.put(callee.params.get(i), call.params.get(i));
        }
        HashSet<BasicBlock> fromBlocks = callee.blocks;
        for (BasicBlock block : fromBlocks) {
            replicator.put(block, new BasicBlock(currentFunction, block.name + "I"));
        }
        for (BasicBlock block : fromBlocks) {
            BasicBlock replicatedBlock = replicator.blockMap.get(block);
            block.phiInst.forEach(((register, phi) -> phi.copySelfTo(replicatedBlock, replicator)));
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                inst.copySelfTo(replicatedBlock, replicator);
            }
        }
        BasicBlock after = new BasicBlock(caller, currentBlock.name + "s");
        currentBlock.spiltAndCopyTo(after, call);

        BasicBlock entry = replicator.get(callee.entryBlock);
        BasicBlock exit = replicator.get(callee.exitBlock);

        Return ret = (Return) exit.back;
        if (ret.value != null) {
            call.dest.replaceAll(ret.value);
        }
        exit.removeTerminator();
        exit.merge(after);
        currentBlock.merge(entry);

        if (caller.exitBlock.equals(currentBlock) && entry != exit) {
            caller.exitBlock = exit;
        }

        caller.blocks = FunctionBlockCollector.run(caller.entryBlock);
        recollectInfo(caller);
        return true;
    }


    boolean inlineFunctions() {
        boolean modified = false;
        // remove unused functions
        HashSet<String> unusedFunctions = new HashSet<>();
        root.functions.forEach(((s, function) -> {
            if (function.caller.size() == 0) {
                unusedFunctions.add(s);
            } else if (function.callee.size() == 0) {
                canInline.add(function);
            }
        }));
        for (String unusedFunction : unusedFunctions) {
            root.functions.remove(unusedFunction);
            modified = true;
        }
        HashMap <Call, Function> inlineCandidate = new LinkedHashMap<>();
        root.functions.forEach((s, function) -> function.blocks.forEach(block -> {
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Call) {
                    Call call = (Call) inst;
                    if (!call.tailCallable
                    &&  (   (forced                             && numberOfInst.get(call.callee) < CompileParameter.forcedInlineInstLimit)
                        ||  (canInline.contains(call.callee)    && numberOfInst.get(call.callee) < CompileParameter.inlineInstLimit))) {
                        inlineCandidate.put(call, function);
                    }
                }
            }
        }));
        for (Map.Entry<Call, Function> entry : inlineCandidate.entrySet()) {
            Call call = entry.getKey();
            Function function = entry.getValue();
            modified |= doInline(call, function);
        }
        updated |= modified;
        return modified;
    }

    public boolean runForced() {
        updated = false;
        forced = true;
        do updateCallGraph(); while (inlineFunctions());
        forced = false;
        root.functions.forEach((s, function) -> DominatorTreeBuilder.runForFunction(function));
        return updated;
    }

    @Override
    public boolean run() {
        updated = false;
        do updateCallGraph(); while (inlineFunctions());
        root.functions.forEach((s, function) -> DominatorTreeBuilder.runForFunction(function));
        return updated;
    }
}
