package Ornn.IR.util;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.Call;
import Ornn.IR.instruction.Inst;

import java.util.HashSet;

public class CallGraphUpdater {

    static Root root;
    static HashSet<Function> visited = new HashSet<>();

    static void FunctionDFS(Function function) {
        visited.add(function);
        for (BasicBlock block : function.blocks) {
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst instanceof Call) {
                    Function callee = ((Call) inst).callee;
                    if (root.isBuiltin(callee.name)) {
                        continue;
                    }
                    callee.caller.add(function);
                    function.callee.add(callee);
                    if (!visited.contains(callee)) {
                        FunctionDFS(callee);
                    }
                }
            }
        }
    }

    public static void run(Root root, boolean runForInline) {
        CallGraphUpdater.root = root;
        root.functions.forEach(((s, function) -> {
            function.callee.clear();
            function.caller.clear();
        }));
        visited.clear();
        Function main = root.getFunction("main");
        FunctionDFS(main);
        if (runForInline) main.caller.add(main);
    }
}
