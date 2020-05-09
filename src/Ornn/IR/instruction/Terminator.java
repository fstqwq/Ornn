package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;

public interface Terminator {
    void redirect(BasicBlock from, BasicBlock to);
}
