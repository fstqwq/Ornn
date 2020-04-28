package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.Pointer;

import java.util.HashSet;

public class Malloc extends Inst {
    public Register dest;
    public Operand size;
    public Malloc(Register dest, Operand size, BasicBlock block) {
        super(block);
        this.dest = dest;
        this.size = size;
        dest.def = this;
    }
    @Override
    public String toString() {
        return dest.toString() + " = call i8* @malloc(" +
                size.type.toString() + " " + size.toString() + ")";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>();
    }
}
