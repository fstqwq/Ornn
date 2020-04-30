package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.type.ArrayType;
import Ornn.IR.type.IntType;
import Ornn.IR.type.Pointer;

import java.util.HashSet;

public class Cast extends Inst {
    public Register src, dest;
    public Cast (Register src, Register dest, BasicBlock block) {
        super(block);
        this.src = src;
        this.dest = dest;
        dest.def = this;
    }

    @Override
    public String toString() {
        String cmd;
        if (src.type.size() < dest.type.size()) cmd = "zext ";
        else if (src.type.size() > dest.type.size()) cmd = "trunc ";
        else if (src.type instanceof IntType && dest.type instanceof Pointer) cmd = "inttoptr ";
        else if (src.type instanceof Pointer && dest.type instanceof IntType) cmd = "ptrtoint ";
        else cmd = "bitcast ";

        return dest.toString() + " = " + cmd + src.type + " " + src.toString() + " to " + dest.type;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>() {{ add(src);}};
    }

    @Override
    public Register getDest() {
        return dest;
    }
}
