package Ornn.IR;

import Ornn.IR.instruction.*;

public interface IRVisitor {
    void visit(Binary inst);
    void visit(Branch inst);
    void visit(Call inst);
    void visit(Cast inst);
    void visit(Cmp inst);
    void visit(GEP inst);
    void visit(Jump inst);
    void visit(Load inst);
    void visit(Malloc inst);
    void visit(Move inst);
    void visit(Return inst);
    void visit(Store inst);
}
