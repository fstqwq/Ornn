package Ornn.IR;

import Ornn.IR.instruction.*;
import Ornn.util.UnreachableError;

import java.util.ArrayList;

public class BasicBlock {
    public Inst front;
    public Inst back;
    public Function function;
    public String name;

    public ArrayList<BasicBlock> precursors = new ArrayList<>();
    public ArrayList<BasicBlock> successors = new ArrayList<>();

    public boolean isTerminated;


    public BasicBlock (Function function, String name) {
        this.function = function;
        this.name = name;
    }

    public void pushFront(Inst inst) {
        if (front == null) {
            front = back = inst;
        }
        else {
            front.insertBefore(inst);
        }
    }

    public void pushBack(Inst inst) {
        if (back == null) {
            front = back = inst;
        }
        else {
            back.insertAfter(inst);
        }
        if (inst.isTerminal()) {
            isTerminated = true;
            if (inst instanceof Jump) {
                linkSuccessor(((Jump) inst).dest);
            } else if (inst instanceof Branch) {
                linkSuccessor(((Branch) inst).thenDest);
                linkSuccessor(((Branch) inst).elseDest);
            } else {
                throw new UnreachableError();
            }
        }
    }

    public void removeTerminator() {
        assert isTerminated;
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
