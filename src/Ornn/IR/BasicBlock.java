package Ornn.IR;

import Ornn.IR.instruction.*;
import Ornn.IR.operand.Register;
import Ornn.util.UnreachableCodeError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    public BasicBlock iDom;
    public HashSet<BasicBlock> domFrontier;

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
                throw new UnreachableCodeError();
            }
        }
    }

    public void processRemoveInst(Inst inst) {
        if (inst instanceof Phi) {
            phiInst.remove(((Phi) inst).dest);
        }
        else if (inst.isTerminal()) {
            isTerminated = false;
            //splitSuccessors();
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
        Inst terminator = back;
        terminator.delete();
        splitSuccessors();
    }

    public void cleanUpTerminator() {
        assert isTerminated;
        Inst terminator = back;
        terminator.delete();
        cleanSuccessors();
    }
    public void splitSuccessors() {
        successors.forEach(x -> {
            x.precursors.remove(this);
            //x.phiRemove(this); // may keep when transforming jumps
        });
        successors.clear();
    }
    public void cleanSuccessors() {
        successors.forEach(x -> {
            x.precursors.remove(this);
            x.phiRemove(this);
        });
        successors.clear();
    }

    public void linkSuccessor(BasicBlock block) {
        successors.add(block);
        block.precursors.add(this);
    }

    public void redirectPrecursor(BasicBlock from, BasicBlock to) {
        precursors.remove(from);
        precursors.add(to);
        phiRedirect(from, to);
    }

    public String toString() {
        return "%" + name;
    }

    public void resetDomInfo() {
        iDom = null;
        domFrontier = new HashSet<>();
    }

    public void redirect(BasicBlock from, BasicBlock to) {
        assert isTerminated;
        ((Terminator) back).redirect(from, to);
        successors.remove(from);
        to.precursors.remove(from);
        linkSuccessor(to);
    }

    public void phiRedirect(BasicBlock from, BasicBlock to) {
        phiInst.forEach((register, phi) -> {
            for (int i = 0; i < phi.blocks.size(); i++) {
                if (phi.blocks.get(i).equals(from)) {
                    phi.blocks.set(i, to);
                }
            }
        });
    }
    public void phiRemove(BasicBlock from) {
        phiInst.forEach((register, phi) -> {
            for (int i = 0; i < phi.blocks.size(); i++) {
                if (phi.blocks.get(i).equals(from)) {
                    phi.blocks.remove(i);
                    phi.values.remove(i);
                    i--;
                }
            }
        });
    }
    public void removeUnnecessaryPhi() {
        HashSet<BasicBlock> deprecated = new HashSet<>();
        phiInst.forEach((register, phi) -> {
            for (int i = 0; i < phi.blocks.size(); i++) {
                if (!precursors.contains(phi.blocks.get(i))) {
                    deprecated.add(phi.blocks.get(i));
                }
            }
        });
        deprecated.forEach(this::phiRemove);
    }

    public void spiltAndCopyTo(BasicBlock dest, Inst inst) {
        assert dest.front == null && dest.back == null;
        for (BasicBlock successor : successors) {
            successor.phiRedirect(this, dest);
            successor.precursors.remove(this);
            successor.precursors.add(dest);
        }
        dest.successors = successors;
        successors = new ArrayList<>();

        dest.front = inst.next;
        dest.front.prev = null;
        dest.back = back;
        isTerminated = false;
        dest.isTerminated = true;
        back = inst.prev;
        if (back == null) {
            front = null;
        } else {
            back.next = null;
        }

        for (Inst i = dest.front; i != null; i = i.next) {
            i.basicBlock = dest;
        }
    }

    public void merge(BasicBlock block) {
        assert block.phiInst.size() == 0;
        successors = block.successors;
        block.successors.forEach(x -> x.redirectPrecursor(block, this));
        for (Inst inst = block.front; inst != null; inst = inst.next) {
            inst.basicBlock = this;
        }
        if (front == null) {
            front = block.front;
        }
        else {
            back.next = block.front;
        }
        if (block.front != null) {
            block.front.prev = back;
        }
        back = block.back;
        isTerminated = block.isTerminated;
    }

    public boolean isDomedBy(BasicBlock block) {
        for (BasicBlock cur = iDom; cur != null; cur = cur.iDom) {
            if (cur.equals(block)) return true;
        }
        return false;
    }

}
