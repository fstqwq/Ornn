package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.util.UnreachableError;

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
        values.forEach(x -> x.uses.add(this));
        dest.def = this;
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

    @Override
    public void replaceUse(Register old, Operand newOpr) {
        boolean success = false;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).equals(old)) {
                values.set(i, newOpr);
                success = true;
            }
        }
        assert success;
    }
    public void add(Operand value, BasicBlock block) {
        values.add(value);
        blocks.add(block);
        value.uses.add(this);
    }

    @Override
    public void accept(IRVisitor visitor) {
        throw new UnreachableError();
    }

}
