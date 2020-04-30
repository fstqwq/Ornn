package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;

import java.util.HashSet;

public class GEP extends Inst {
    public Register dest;
    public BaseType type;
    public Operand ptr, arrayOffset, elementOffset;
    public GEP(BaseType type, Operand ptr, Operand arrayOffset, Operand elementOffset, Register dest, BasicBlock block) {
        super(block);
        this.type = type;
        this.ptr = ptr;
        this.arrayOffset = arrayOffset;
        this.elementOffset = elementOffset;
        this.dest = dest;
        dest.def = this;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>(){{ add(ptr); add(arrayOffset);}};
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return dest.toString() + " = getelementptr inbounds "
                + type.toString() + ", "
                + ptr.type.toString() + " " + ptr.toString() + ", "
                + arrayOffset.type.toString() + " " + arrayOffset.toString() +
                (elementOffset == null ? "" : ", " + elementOffset.type.toString() + " " + elementOffset.toString());
    }
    @Override
    public Register getDest() {
        return dest;
    }
}
