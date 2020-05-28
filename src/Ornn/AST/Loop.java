package Ornn.AST;

import Ornn.IR.BasicBlock;

public interface Loop {
    BasicBlock getDestBlock();
    BasicBlock getContinueBlock();
    void setLoopDepth(int depth);
    int getLoopDepth();
}
