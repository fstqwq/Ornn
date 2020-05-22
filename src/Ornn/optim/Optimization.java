package Ornn.optim;

import Ornn.IR.Root;

import java.util.ArrayList;


public class Optimization {
    public Inline inline;
    public ArrayList<Pass> passes = new ArrayList<>();

    public Optimization(Root root) {
        inline = new Inline(root);
        passes.add(inline);
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
