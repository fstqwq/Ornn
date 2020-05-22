package Ornn.optim;

import Ornn.IR.*;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.*;
import Ornn.IR.util.FunctionBlockCollector;
import Ornn.IR.util.IRReplicator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Inline implements Pass {
    static final int forcedInstLimit = 128;
    static final int instLimit = 2048;
    Root root;
    boolean updated = false, forced = false;
    public Inline(Root root) {
        this.root = root;
    }

    HashSet<Function> visited = new HashSet<>(), canInline = new HashSet<>();
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

    void FunctionDFS(Function function) {
        visited.add(function);
        for (BasicBlock block : function.blocks) {
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Call) {
                    Function callee = ((Call) inst).callee;
                    if (root.isBuiltin(callee.name)) {
                        continue;
                    }
                    callee.caller.add(function);
                    function.callee.add(function);
                    if (!visited.contains(callee)) {
                        FunctionDFS(callee);
                    }
                }
            }
        }
        recollectInfo(function);
    }

    void updateCallGraph() {
        root.functions.forEach(((s, function) -> {
            function.callee.clear();
            function.caller.clear();
        }));
        visited.clear();
        numberOfInst.clear();
        Function main = root.getFunction("main");
        main.caller.add(null); // prevent from deleting
        FunctionDFS(main);
        root.builtinFunctions.forEach(((s, function) -> {
            numberOfInst.put(function, instLimit);
        }));
    }

    Function currentFunction;
    BasicBlock currentBlock;

    boolean doInline(Call call, Function caller) {
        currentFunction = caller;
        currentBlock = call.basicBlock;
        IRReplicator replicator = new IRReplicator();
        Function callee = call.callee;
        if (numberOfInst.get(caller) + numberOfInst.get(callee) >= instLimit) {
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
        HashMap <Call, Function> inlineCandidate = new HashMap<>();
        root.functions.forEach((s, function) -> function.blocks.forEach(block -> {
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Call) {
                    Call call = (Call) inst;
                    if ((forced                             && numberOfInst.get(call.callee) < forcedInstLimit)
                    ||  (canInline.contains(call.callee)    && numberOfInst.get(call.callee) < instLimit)) {
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
        return updated;
    }

    @Override
    public boolean run() {
        updated = false;
        do updateCallGraph(); while (inlineFunctions());
        return updated;
    }
}
