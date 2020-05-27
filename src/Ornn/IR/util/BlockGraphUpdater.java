package Ornn.IR.util;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.instruction.Branch;
import Ornn.IR.instruction.Jump;
import Ornn.IR.instruction.Return;
import Ornn.util.UnreachableCodeError;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class BlockGraphUpdater {
    static HashSet<BasicBlock> visited;

    static void DFS(BasicBlock x) {
        if (visited.contains(x)) return;
        visited.add(x);
        if (x.back instanceof Return) {
        } else if (x.back instanceof Branch) {
            x.linkSuccessor(((Branch) x.back).thenDest);
            x.linkSuccessor(((Branch) x.back).elseDest);
        } else if (x.back instanceof Jump) {
            x.linkSuccessor(((Jump) x.back).dest);
        } else {
            throw new UnreachableCodeError();
        }
        x.successors.forEach(BlockGraphUpdater::DFS);
    }

    public static void runForFunction(Function function) {
        function.blocks.forEach(x -> x.precursors.clear());
        function.blocks.forEach(x -> x.successors.clear());
        visited = new LinkedHashSet<>();
        DFS(function.entryBlock);
        function.blocks.forEach(BasicBlock::removeUnnecessaryPhi);
        function.blocks = visited;
    }
}
