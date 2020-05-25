package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.IRVisitor;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.IR.util.IRReplicator;
import Ornn.util.UnreachableCodeError;

import java.util.ArrayList;
import java.util.HashMap;
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
    public void copySelfTo(BasicBlock dest, IRReplicator replicator) {
        ArrayList<BasicBlock> newBlocks = new ArrayList<>();
        ArrayList<Operand> newValues = new ArrayList<>();
        for (int i = 0; i < blocks.size(); i++) {
            newBlocks.add(replicator.get(blocks.get(i)));
            newValues.add(replicator.get(values.get(i)));
        }
        dest.pushBack(new Phi(replicator.get(this.dest), newBlocks, newValues, dest));
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
        throw new UnreachableCodeError();
    }

    @Override
    public boolean isSameWith(Inst inst) {
        if (!(inst instanceof Phi)) return false;
        if (((Phi) inst).values.size() != values.size()) return false;

        HashMap<BasicBlock, Operand> tmp = new HashMap<>();
        for (int i = 0; i < blocks.size(); i++) {
            tmp.put(blocks.get(i), values.get(i));
        }
        for (int i = 0; i < blocks.size(); i++) {
            BasicBlock block  = ((Phi) inst).blocks.get(i);
            Operand value  = ((Phi) inst).values.get(i);
            if (!tmp.containsKey(block)) return false;
            if (!tmp.get(block).equals(value)) return false;
        }
        return true;
    }

    @Override
    public void delete() {
        values.forEach(value -> value.uses.remove(this));
    }
}
