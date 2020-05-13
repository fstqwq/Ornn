package Ornn.backend;

import Ornn.RISCV.*;
import Ornn.RISCV.instrution.*;
import Ornn.RISCV.operand.*;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Stack;

import static java.lang.Integer.min;

/*
    Tiger book! Eternal god!
 */

public class RegisterAllocation {
    static class Edge {
        public Reg u, v;
        public Edge(Reg u, Reg v) {
            this.u = u;
            this.v = v;
        }
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Edge && ((Edge) obj).u.equals(u) && ((Edge) obj).v.equals(v);
        }
        @Override
        public int hashCode() {
            return u.hashCode() ^ v.hashCode();
        }
    }
    HashSet<Reg> preColored = new LinkedHashSet<>();
    HashSet<Reg> initial = new LinkedHashSet<>();
    HashSet<Reg> simplifyWorkList = new LinkedHashSet<>();
    HashSet<Reg> freezeWorkList = new LinkedHashSet<>();
    HashSet<Reg> spillWorkList = new LinkedHashSet<>();
    HashSet<Reg> spilledNodes = new LinkedHashSet<>();
    HashSet<Reg> coalescedNodes = new LinkedHashSet<>();
    HashSet<Reg> coloredNodes = new LinkedHashSet<>();
    HashSet<Edge> adjSet = new LinkedHashSet<>();
    Stack<Reg> selectStack = new Stack<>();
    int stackOffset, K;
    RVFunction currentFunction;
    HashSet<Mv> coalescedMoves = new LinkedHashSet<>();
    HashSet<Mv> constrainedMoves = new LinkedHashSet<>();
    HashSet<Mv> frozenMoves = new LinkedHashSet<>();
    HashSet<Mv> workListMoves = new LinkedHashSet<>();
    HashSet<Mv> activeMoves = new LinkedHashSet<>();

    HashSet<Reg> spillTemps = new LinkedHashSet<>();

    RVRoot root;
    public RegisterAllocation (RVRoot root) {
        this.root = root;
        preColored.addAll(root.pRegs);
        K = root.allocatableRegs.size();
    }
    void debug(String s) {
        System.err.println(s);
    }

    void init() {
        initial.clear();
        simplifyWorkList.clear();
        freezeWorkList.clear();
        spillWorkList.clear();
        spilledNodes.clear();
        coalescedNodes.clear();
        coloredNodes.clear();
        selectStack.clear();

        coalescedMoves.clear();
        constrainedMoves.clear();
        frozenMoves.clear();
        activeMoves.clear();
        workListMoves.clear();

        adjSet.clear();
        currentFunction.blocks.forEach(block -> {
            for (RVInst inst = block.front; inst != null; inst = inst.next){
                initial.addAll(inst.getDefs());
                initial.addAll(inst.getUses());
            }
        });
        initial.removeAll(preColored);
        initial.forEach(Reg::reset);
        preColored.forEach(Reg::setPreColored);

        for (RVBlock block : currentFunction.blocks) {
            double weight = Math.pow(10, min(block.successors.size(), block.precursors.size()));
            for (RVInst inst = block.front; inst != null; inst = inst.next) {
                inst.getUses().forEach(reg -> reg.weight += weight);
                if (inst.getDefs().size() > 0) {
                    inst.getDefs().iterator().next().weight += weight;
                }
            }
        }
    }
    private void addEdge(Reg u, Reg v) {
        if (u != v && !adjSet.contains(new Edge(u, v))) {
            adjSet.add(new Edge(u, v));
            adjSet.add(new Edge(v, u));
            if (!preColored.contains(u)) {
                u.addAdj(v);
            }
            if (!preColored.contains(v)) {
                v.addAdj(u);
            }
        }
    }
    HashSet<Reg> adjacent(Reg n) {
        return new HashSet<>(n.adjList) {{removeAll(selectStack); removeAll(coalescedNodes);}};
    }
    HashSet<Reg> adjacent(Reg u, Reg v) {
        return new HashSet<>(adjacent(u)) {{addAll(adjacent(v));}};
    }
    HashSet<Mv> nodeMoves(Reg n) {
        return new HashSet<>(workListMoves) {{ addAll(activeMoves); retainAll(n.moveList);}};
    }
    boolean moveRelated(Reg n) {
        return !nodeMoves(n).isEmpty();
    }
    void enableMoves(HashSet<Reg> nodes) {
        //if nothing in active moves, it is not needed
        nodes.forEach(node -> nodeMoves(node).forEach(move -> {
            if (activeMoves.contains(move)) {
                activeMoves.remove(move);
                workListMoves.add(move);
            }
        }));
    }
    void decrementDegree(Reg reg) {
        if (reg.degree-- == K) {
            HashSet<Reg> nodes = new HashSet<>(adjacent(reg));
            nodes.add(reg);
            enableMoves(nodes);
            spillWorkList.remove(reg);
            if (moveRelated(reg)) freezeWorkList.add(reg);
            else simplifyWorkList.add(reg);
        }
    }
    void simplify() {
        Reg reg = simplifyWorkList.iterator().next();
        simplifyWorkList.remove(reg);
        selectStack.push(reg);
        for (Reg reg1 : adjacent(reg)) {
            decrementDegree(reg1);
        }
    }
    void build() {
        for (RVBlock block : currentFunction.blocks) {
            HashSet<Reg> currentLive = new HashSet<>(block.liveOut);
            for (RVInst inst = block.back; inst != null; inst = inst.prev) {
                if (inst instanceof Mv) {
                    currentLive.removeAll(inst.getUses());
                    HashSet<Reg> mvAbout = inst.getUses();
                    mvAbout.addAll(inst.getDefs());
                    for (Reg reg : mvAbout) {
                        reg.moveList.add((Mv) inst);
                    }
                    workListMoves.add((Mv) inst);
                }
                HashSet<Reg> defs = inst.getDefs();
                currentLive.add(root.pRegs.get(0));
                currentLive.addAll(defs);
                for (Reg def : defs) {
                    for (Reg reg : currentLive) {
                        addEdge(reg, def);
                    }
                }
                currentLive.removeAll(defs);
                currentLive.addAll(inst.getUses());
            }
        }
    }

    Reg getAlias(Reg n) {
        if (coalescedNodes.contains(n)) {
            n.alias = getAlias(n.alias);
            return n.alias;
        } else {
            return n;
        }
    }

    void addWorkList(Reg node) {
        if (!preColored.contains(node) && !moveRelated(node) && node.degree < K) {
            freezeWorkList.remove(node);
            simplifyWorkList.add(node);
        }
    }

    boolean OK(Reg t, Reg r) {
        return t.degree < K || preColored.contains(t) || adjSet.contains(new Edge(t, r));
    }

    boolean forAllOK(Reg u, Reg v) {
        for (Reg w : adjacent(v)) {
            if (!OK(w, u)) {
                return false;
            }
        }
        return true;
    }

    boolean conservative(HashSet<Reg> nodes) {
        int count = 0;
        for (Reg node : nodes) {
            if (node.degree >= K) ++count;
        }
        return count < K;
    }

    void combine(Reg u, Reg v) {
        if (freezeWorkList.contains(v)) {
            freezeWorkList.remove(v);
        }
        else {
            spillWorkList.remove(v);
        }
        coalescedNodes.add(v);
        v.alias = u;
        u.moveList.addAll(v.moveList);
        HashSet<Reg> tmp = new HashSet<>() {{add(v);}};
        enableMoves(tmp);
        adjacent(v).forEach(t -> {
            addEdge(t, u);
            decrementDegree(t);
        });
        if (u.degree >= K && freezeWorkList.contains(u)) {
            freezeWorkList.remove(u);
            spillWorkList.add(u);
        }
    }

    void coalesce() {
        Mv move = workListMoves.iterator().next();
        Reg x = getAlias(move.rd);
        Reg y = getAlias(move.rs);
        Reg u, v;
        if (preColored.contains(y)) {
            u = y;
            v = x;
        }else {
            u = x;
            v = y;
        }
        workListMoves.remove(move);
        if (u == v) {
            coalescedMoves.add(move);
            addWorkList(u);
        } else if (preColored.contains(v) || adjSet.contains(new Edge(u, v))) {
            constrainedMoves.add(move);
            addWorkList(u);
            addWorkList(v);
        } else {
            if ((preColored.contains(u) && forAllOK(u, v)) || (!preColored.contains(u) && conservative(adjacent(u, v)))) {
                coalescedMoves.add(move);
                combine(u, v);
                addWorkList(u);
            } else {
                activeMoves.add(move);
            }
        }
    }
    void freeze() {
        Reg u = freezeWorkList.iterator().next();
        freezeWorkList.remove(u);
        simplifyWorkList.add(u);
        freezeMoves(u);
    }
    void freezeMoves(Reg u) {
        for (Mv mv : nodeMoves(u)) {
            Reg x = mv.rd, y = mv.rs;
            Reg v;
            if (getAlias(u) == getAlias(y)) {
                v = getAlias(x);
            }
            else {
                v = getAlias(y);
            }
            activeMoves.remove(mv);
            frozenMoves.add(mv);
            if (v.degree < K && nodeMoves(v).isEmpty()) {
                freezeWorkList.remove(v);
                simplifyWorkList.add(v);
            }
        }
    }

    void selectSpill() {
        Reg m = null;
        double minCost = 1e9;
        boolean hasCandidate = false;
        for (Reg reg : spillWorkList) {
            double cost = reg.weight / reg.degree;
            if (!spillTemps.contains(reg)) {
                hasCandidate = true;
                if (cost < minCost) {
                    minCost = cost;
                    m = reg;
                }
            } else if (!hasCandidate) {
                m = reg;
            }
        }
        spillWorkList.remove(m);
        simplifyWorkList.add(m);
        freezeMoves(m);
    }
    void assignColors() {
        while (!selectStack.isEmpty()) {
            Reg n = selectStack.pop();
            ArrayList<PReg> okColors = new ArrayList<>(root.allocatableRegs);
            HashSet<Reg> colored = new HashSet<>(coloredNodes);
            colored.addAll(preColored);
            for (Reg w : n.adjList) {
                if (colored.contains(getAlias(w))) {
                    okColors.remove(getAlias(w).color);
                }
            }
            if (okColors.isEmpty()) {
                spilledNodes.add(n);
            }
            else {
                coloredNodes.add(n);
                n.color = okColors.get(0);
            }
        }
        coalescedNodes.forEach(n -> n.color = getAlias(n).color);
    }

    void runForFunction(RVFunction function) {
        currentFunction = function;
        for ( ; ; ) {
            init();
            LivenessAnalysis.runForFunction(function);
            build();
            for (Reg node : initial) {
                if (node.degree >= K) {
                    spillWorkList.add(node);
                }
                else if (moveRelated(node)) {
                    freezeWorkList.add(node);
                }
                else {
                    simplifyWorkList.add(node);
                }
            }
            do{
                if (!simplifyWorkList.isEmpty()) {
                    simplify();
                }
                else if (!workListMoves.isEmpty()) {
                    coalesce();
                }
                else if (!freezeWorkList.isEmpty()) {
                    freeze();
                }
                else if (!spillWorkList.isEmpty()) {
                    selectSpill();
                }
            }
            while (!(freezeWorkList.isEmpty() && simplifyWorkList.isEmpty() && spillWorkList.isEmpty() && workListMoves.isEmpty()));
            assignColors();
            if (spilledNodes.isEmpty()) {
                break;
            }
            rewriteProgram();
        }
    }

    void rewriteProgram() {
        HashSet<Reg> newTemps = new HashSet<>();

        spilledNodes.forEach(v -> {
            v.stackOffset = new SImm(-stackOffset - 4, false);
            stackOffset += 4;
        });
        for (RVBlock block : currentFunction.blocks) {
            for (RVInst inst = block.front; inst != null; inst = inst.next) {
                if (inst.getDefs().size() == 1) {
                    Reg dest = inst.getDefs().iterator().next();
                    if (dest instanceof VReg) {
                        getAlias(dest);
                    }
                }
            }
        }
        PReg sp = root.regMap.get("sp");
        for (RVBlock block : currentFunction.blocks) {
            for (RVInst inst = block.front; inst != null; inst = inst.next) {
                for (Reg reg : inst.getUses()) {
                    if (reg.stackOffset != null) {
                        if (inst.getDefs().contains(reg)) {
                            VReg tmp = new VReg(currentFunction.vRegCount++, 4);
                            inst.replaceUse(reg, tmp);
                            inst.replaceRd(reg, tmp);
                            inst.insertBefore(new Ld(sp, reg.stackOffset, tmp,  tmp.size, block));
                            inst.insertAfter(new St(sp, reg.stackOffset, tmp,  tmp.size, block));
                            newTemps.add(tmp);
                        }
                        else {
                            if (inst instanceof Mv && ((Mv)inst).rs == reg && ((Mv)inst).rd.stackOffset == null) {
                                RVInst replace = new Ld(sp, reg.stackOffset, ((Mv)inst).rd,  ((VReg)reg).size, block);
                                inst.replace(replace);
                                inst = replace;
                            } else {
                                VReg tmp = new VReg(currentFunction.vRegCount++, 4);
                                inst.insertBefore(new Ld(sp, reg.stackOffset, tmp, tmp.size, block));
                                inst.replaceUse(reg, tmp);
                                newTemps.add(tmp);
                            }
                        }
                    }
                }
                for (Reg def : inst.getDefs()) {
                    if (def.stackOffset != null) {
                        if (!inst.getUses().contains(def)) {
                            if (inst instanceof Mv && ((Mv) inst).rs.stackOffset == null){
                                RVInst replace = new St(sp, def.stackOffset, ((Mv) inst).rs,  ((VReg)def).size, block);
                                inst.replace(replace);
                                inst = replace;
                            }
                            else {
                                VReg tmp = new VReg(currentFunction.vRegCount++, 4);
                                inst.replaceRd(def, tmp);
                                inst.insertAfter(new St(sp, def.stackOffset, tmp, ((VReg)def).size, block));
                                newTemps.add(tmp);
                            }
                        }
                    }
                }
            }
        }

        spillTemps.addAll(newTemps);
        initial.addAll(coloredNodes);
        initial.addAll(coalescedNodes);
        initial.addAll(newTemps);
    }

    void applyStackOffset() {
        for (RVBlock block : currentFunction.blocks) {
            for (RVInst inst = block.front; inst != null; inst = inst.next) {
                inst.applyStackOffset(stackOffset);
            }
        }
    }

    void removeIdMove() {
        for (RVBlock block : currentFunction.blocks) {
            for (RVInst inst = block.front, next; inst != null; inst = inst.next) {
//                System.err.println(block.name + " : " +inst.prev + " (" + inst + ") " + inst.next + " " + (inst instanceof Mv ? ((Mv) inst).rs.color + " " + ((Mv) inst).rd.color : ""));
                if (inst instanceof Mv && ((Mv) inst).rs.color.equals(((Mv) inst).rd.color)) {
                    inst.delete();
                } else if (inst instanceof IType && ((IType) inst).op == RVInst.SCategory.add && ((IType) inst).rd.color.equals(((IType) inst).rs.color) && ((IType) inst).imm.value == 0) {
                    inst.delete();
                }
            }
        }
    }

    public void run() {
        for (RVFunction function : root.functions) {
            stackOffset = 0;
            runForFunction(function);
            stackOffset += function.paramInStackOffset;
            stackOffset = (stackOffset + 15) / 16 * 16;
            applyStackOffset();
            removeIdMove();
        }
    }
}
