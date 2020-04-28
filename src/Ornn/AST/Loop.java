package Ornn.AST;

import Ornn.IR.BasicBlock;

public interface Loop {
    BasicBlock getDestBlock();
    BasicBlock getContinueBlock();
}
