package Ornn.IR;

import Ornn.IR.instruction.Inst;
import Ornn.IR.operand.*;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.ClassType;
import Ornn.IR.type.Pointer;
import Ornn.IR.util.FunctionBlockCollector;
import Ornn.util.StringParser;

import java.io.PrintStream;
import java.util.HashSet;

public class IRPrinter {
    int nSymbol;
    Root root;
    PrintStream file;

    public IRPrinter(Root root, PrintStream file) {
        this.file = file;
        this.root = root;
    }

    HashSet<Register> dest = new HashSet<>(); // for non-SSA printer

    void renameBlock(BasicBlock block) {
        block.name = String.format("%d", nSymbol++);
        block.phiInst.forEach((reg, phi) -> reg.name = String.format("%d", nSymbol++));
        for (Inst inst = block.front; inst != null; inst = inst.next) {
            if (inst.getDest() != null && !dest.contains(inst.getDest())) {
                dest.add(inst.getDest());
                inst.getDest().name = String.format("%d", nSymbol++);
            }
        }
    }

    HashSet <Function> printedFunctions = new HashSet<>(); // for alias

    void printHeader(Function function, boolean isBuiltin) {
        if (function == null || printedFunctions.contains(function)) return;
        printedFunctions.add(function);
        nSymbol = 0;
        dest.clear();
        file.print(isBuiltin ? "declare " : "define ");
        file.print(function.returnType.toString() + " @" + function.name + "(");
        String divider = "";
        for (Register param : function.params) {
            param.name = String.format("%d", nSymbol++);
            dest.add(param);
            file.print(divider);
            divider = ", ";
            file.print(param.type.toString() + " " + param.toString());
        }
        if (isBuiltin && function.name.charAt(function.name.length() - 1) == 'f') {
            file.print(", ...");
        }
        file.print(")");
        if (isBuiltin) {
            file.println();
        }
    }
    void printType(String name, ClassType type) {
        file.print("%struct." + name + " = " + "type {");
        String divider = "";
        for (BaseType member : type.members) {
            file.print(divider);
            divider = ", ";
            file.print(member.toString());
        }
        file.print("}\n");
    }

    void printGlobal(Global global) {
        file.println("@" + global.name + " = global " + ((Pointer)global.type).typePointedTo.toString()
                + "  zeroinitializer, align " + global.type.size() / 8);
    }

    void printConstStr(String value, ConstStr str) {
        file.println("@" + str.name + " = private unnamed_addr constant "
                + "[" + (value.length() + 1) + " x i8] c"
                + "\"" + StringParser.llvmTransform(value) + "\", align 1");
    }

    private void printBlock(BasicBlock block) {
        file.println(block.name + ":");
        block.phiInst.forEach((reg, phi) -> file.println("\t" + phi.toString()));
        for (Inst inst = block.front; inst != null; inst = inst.next) {
            file.println("\t" + inst.toString() + (inst.comment == null ? "" : "\t\t\t; " + inst.comment));
        }
    }



    HashSet<Function> visited = new HashSet<>();

    void FunctionDFS(Function function) {
        visited.add(function);
        for (Function callee : function.callee) {
            if (!visited.contains(callee)) {
                FunctionDFS(callee);
            }
        }
    }
    void printFunction(String name, Function function) {
        if (!name.equals("main") && !visited.contains(function)) return;
        printHeader(function, false);
        file.println(" {");
        function.blocks = FunctionBlockCollector.run(function.entryBlock);
        function.blocks.forEach(this::renameBlock);
        function.blocks.forEach(this::printBlock);
        file.println("}");
    }

    public boolean run() {
        file.println("target triple = \"riscv32\"");
        visited = new HashSet<>();
        FunctionDFS(root.getFunction("main"));
        root.builtinFunctions.forEach((name, func) -> {
            if (visited.contains(func)) printHeader(func, true);
        });
        root.types.forEach(this::printType);
        root.globals.forEach(this::printGlobal);
        root.proxyStatics.forEach(this::printGlobal);
        root.constStrings.forEach(this::printConstStr);
        root.functions.forEach(this::printFunction);
        return true;
    }
}
