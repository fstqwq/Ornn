package Ornn.IR;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class FunctionBlockCollector {
    static HashSet<BasicBlock> forwardReachable;
    public static void forwardDFS(BasicBlock x) {
        if (forwardReachable.contains(x)) return;
        forwardReachable.add(x);
        for (BasicBlock successor : x.successors) {
            forwardDFS(successor);
        }
    }
    public static HashSet<BasicBlock> run(BasicBlock entry) {
        forwardReachable = new LinkedHashSet<>();
        forwardDFS(entry);
        return forwardReachable;
    }
}
