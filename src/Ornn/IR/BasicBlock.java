package Ornn.IR;

import Ornn.IR.instruction.*;
import Ornn.IR.operand.Register;
import Ornn.util.UnreachableError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BasicBlock {
    public Inst front;
    public Inst back;
    public Function function;
    public String name;

    public ArrayList<BasicBlock> precursors = new ArrayList<>();
    public ArrayList<BasicBlock> successors = new ArrayList<>();
    public HashMap<Register, Phi> phiInst = new LinkedHashMap<>();
    public boolean isTerminated;


    public BasicBlock (Function function, String name) {
        this.function = function;
        this.name = name;
    }

    private void processAddInst(Inst inst) {
        if (inst.isTerminal()) {
            isTerminated = true;
            if (inst instanceof Jump) {
                linkSuccessor(((Jump) inst).dest);
            } else if (inst instanceof Branch) {
                linkSuccessor(((Branch) inst).thenDest);
                linkSuccessor(((Branch) inst).elseDest);
            } else if (inst instanceof Return) {
                // pass
            } else {
                throw new UnreachableError();
            }
        }
    }

    public void processRemoveInst(Inst inst) {
        if (inst instanceof Phi) {
            phiInst.remove(((Phi) inst).dest);
        }
        else if (inst.isTerminal()) {
            isTerminated = false;
            splitSuccessors();
        }
    }

    public void pushFront(Inst inst) {
        if (inst instanceof Phi) {
            phiInst.put(((Phi) inst).dest, (Phi) inst);
            return;
        }
        if (front == null) {
            front = back = inst;
        }
        else {
            front.insertBefore(inst);
        }
        processAddInst(inst);
    }

    public void pushBack(Inst inst) {
        if (inst.isTerminal() && isTerminated) {
            return;
        }
        if (inst instanceof Phi) {
            phiInst.put(((Phi) inst).dest, (Phi) inst);
            return;
        }
        if (back == null) {
            front = back = inst;
        }
        else {
            back.insertAfter(inst);
        }
        processAddInst(inst);
    }

    public void removeTerminator() {
        assert isTerminated;
        isTerminated = false;
        Inst terminator = back;
        terminator.delete();
        splitSuccessors();
    }

    public void linkSuccessor(BasicBlock block) {
        successors.add(block);
        block.precursors.add(this);
    }

    public void splitSuccessors() {
        successors.forEach(x -> x.precursors.remove(this));
        successors.clear();
    }

    public String toString() {
        return "%" + name;
    }

}
