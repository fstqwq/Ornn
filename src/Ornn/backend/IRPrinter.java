package Ornn.backend;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.Root;
import Ornn.IR.instruction.Inst;
import Ornn.IR.operand.ConstStr;
import Ornn.IR.operand.Global;
import Ornn.IR.operand.Register;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.ClassType;
import Ornn.IR.type.Pointer;
import Ornn.util.StringParser;

import java.io.PrintStream;
import java.util.HashSet;

public class IRPrinter {
    int nSymbol;
    Root root;
    PrintStream out;

    public IRPrinter(Root root, PrintStream out) {
        this.out = out;
        this.root = root;
    }
    void renameBlock(BasicBlock block) {
        if (true) { // debug
            block.name = String.format("%d", nSymbol++);
            block.phiInst.forEach((reg, phi) -> reg.name = String.format("%d", nSymbol++));
            for (Inst inst = block.front; inst != null; inst = inst.next) {
                if (inst.getDest() != null) {
                    inst.getDest().name = String.format("%d", nSymbol++);
                }
            }
        }
    }
    void printHeader(Function function, boolean isBuiltin) {
        nSymbol = 0;
        out.print(isBuiltin ? "declare " : "define ");
        out.print(function.returnType.toString() + " @" + function.name + "(");
        String divider = "";
        for (Register param : function.params) {
            param.name = String.format("%d", nSymbol++);
            out.print(divider);
            divider = ", ";
            out.print(param.type.toString() + " " + param.toString());
        }
        out.print(")");
        if (isBuiltin) {
            out.println();
        }
    }
    void printType(String name, ClassType type) {
        out.print("%struct." + name + " = " + "type {");
        String divider = "";
        for (BaseType member : type.members) {
            out.print(divider);
            divider = ", ";
            out.print(member.toString());
        }
        out.print("}\n");
    }

    void printGlobal(Global global) {
        out.println("@" + global.name + " = global " + ((Pointer)global.type).typePointedTo.toString()
                + "  zeroinitializer, align " + global.type.size() / 8);
    }

    void printConstStr(String value, ConstStr str) {
        out.println("@" + str.name + " = private unnamed_addr constant "
                + "[" + value.length() + " x i8] c"
                + "\"" + StringParser.llvmTransform(value) + "\", align 1");;
    }

    private void printBlock(BasicBlock block) {
        out.println(block.name + ":");
        block.phiInst.forEach((reg, phi) -> out.println("\t" + phi.toString()));
        for (Inst inst = block.front; inst != null; inst = inst.next) {
            out.println("\t" + inst.toString());
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
        out.println(" {");
        function.blocks.forEach(this::renameBlock);
        function.blocks.forEach(this::printBlock);
        out.println("}");
    }

    public void run() {
        visited = new HashSet<>();
        FunctionDFS(root.getFunction("main"));
        root.builtinFunctions.forEach((name, func) -> {
            if (visited.contains(func)) printHeader(func, true);
        });
        root.types.forEach(this::printType);
        root.globals.forEach(this::printGlobal);
        root.constStrings.forEach(this::printConstStr);
        root.functions.forEach(this::printFunction);
    }
}
