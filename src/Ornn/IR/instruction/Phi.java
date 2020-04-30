package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;

import java.util.ArrayList;
import java.util.HashSet;

public class Phi extends Inst {
    public Register dest;
    public ArrayList<BasicBlock> blocks;
    public ArrayList<Operand> values;
    public Phi (Register dest, ArrayList<BasicBlock> blocks, ArrayList<Operand> values, BasicBlock block) {
        super(block);
        this.dest = dest;
        this.blocks = blocks;
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(dest.toString()).append(" = phi ").append(dest.type.toString());
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) ret.append(",");
            ret.append(" [ ").append(values.get(i).toString()).append(", ").append(blocks.get(i).toString()).append(" ]");
        }
        return ret.toString();
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>( values );
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
    @Override
    public Register getDest() {
        return dest;
    }
}
