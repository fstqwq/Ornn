package Ornn.optim;

import Ornn.IR.Root;

import java.util.ArrayList;


public class Optimization {
    Inline inline;
    DeadCodeElimination DCE;
    CommonSubexpressionElimination CSE;
    ConstantPropagation CP;
    CFGSimplification CFGS;
    MIRPeephole PEEP;
    public ArrayList<Pass> passes = new ArrayList<>();

    public Optimization(Root root) {
        inline = new Inline(root);
        DCE = new DeadCodeElimination(root);
        CSE = new CommonSubexpressionElimination(root);
        CP = new ConstantPropagation(root);
        CFGS = new CFGSimplification(root);
        PEEP = new MIRPeephole(root);
        passes.add(CP);
        passes.add(DCE);
        passes.add(CSE);
        passes.add(inline);
        passes.add(CFGS);
        passes.add(PEEP);
    }
    public void run() {
        do {
            boolean updated;
            do {
                updated = false;
                for (Pass pass : passes) {
                    updated |= pass.run();
                }
            } while (updated);
        } while (inline.runForced());
    }
}
