package Ornn.RISCV;

import Ornn.RISCV.instrution.Br;
import Ornn.RISCV.instrution.Jmp;
import Ornn.RISCV.instrution.RVInst;
import Ornn.RISCV.operand.GReg;
import Ornn.RISCV.operand.VReg;
import Ornn.util.StringParser;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class RISCVDebugger {
    RVRoot root;
    PrintStream file;
    public RISCVDebugger(RVRoot root, PrintStream file) {
        this.root = root;
        this.file = file;
    }

    HashSet<RVBlock> visited;
    int blockCount;
    RVFunction currentFunction;

    boolean dfsBlock(RVBlock block) {
        if (visited.contains(block)) return false;
        visited.add(block);
        block.comment = block.name;
        block.name = "." + currentFunction.name + "_." + blockCount++;
        if (block.back instanceof Jmp) {
            if (dfsBlock(((Jmp) block.back).offset)) {
            }
        }
        for (RVInst inst = block.front; inst != null; inst = inst.next) {
            if (inst instanceof Br) {
                dfsBlock(((Br) inst).offset);
            }
        }
        return true;
    }

    void renameFunction(RVFunction function) {
        blockCount = 0;
        currentFunction = function;
        visited = new LinkedHashSet<>();
        dfsBlock(function.entryBlock);
    }

    void runForBlock(RVBlock block) {
        file.println(block.name + ": " /*+ " # " + block.comment*/);
        for (RVInst inst = block.front; inst != null; inst = inst.next) {
            file.print("\t" + inst.toString() + " #");
            inst.getDefs().forEach(x -> {
                if (x instanceof VReg) {
                    file.print(" def " + ((VReg) x).name);
                }
            } );
            inst.getUses().forEach(x -> {
                if (x instanceof VReg) {
                    file.print(" use " + ((VReg) x).name);
                }
            } );
            file.println();
        }
    }

    void runForFunction(RVFunction function) {
        file.println("\t.globl\t" + function.name);
        file.println("\t.p2align\t1");
        file.println("\t.type\t" + function.name +",@function");
        file.println(function.name + ":");
        renameFunction(function);
        visited.forEach(this::runForBlock);
        file.println();
    }

    void runForGlobal(GReg gReg) {
        file.println("\t.type\t" + gReg.name + ",@object");
        if (gReg.isArray) {
            file.println("\t.data");
        } else {
            file.println("\t.section\t.data");
        }
        file.println("\t.globl\t" + gReg.name);
        file.println("\t.p2align\t2");
        file.println(gReg.name + ":");
        if (gReg.initialization == null) {
            file.println("\t.zero\t" + gReg.size);
        } else {
            for (String s : gReg.initialization) {
                file.println("\t" + s);
            }
        }
        file.println("\t.size\t" + gReg.name + ", " + gReg.size + "\n");
    }

    void runForConstStr(GReg gReg, String s) {
        file.println("\t.type\t" + gReg.name + ",@object");
        file.println("\t.section\t.rodata");
        file.println(gReg.name + ":");
        String str = StringParser.asmTransform(s);
        file.println("\t.asciz\t\"" + str + "\"");
        file.println("\t.size\t" + gReg.name + ", " + (s.length() + 1) + "\n");
    }

    public void run() {
        file.println("\t.text");
        root.functions.forEach(this::runForFunction);
        root.global.forEach(this::runForGlobal);
        root.constStr.forEach(this::runForConstStr);
    }



}
