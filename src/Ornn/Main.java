package Ornn;

import Ornn.AST.ProgramNode;
import Ornn.RISCV.RISCVDebugger;
import Ornn.RISCV.RISCVPrinter;
import Ornn.RISCV.RVRoot;
import Ornn.frontend.IRBuilder;
import Ornn.IR.IRPrinter;
import Ornn.backend.InstSelector;
import Ornn.backend.RegisterAllocation;
import Ornn.frontend.*;
import Ornn.optim.Global2Local;
import Ornn.optim.Mem2Reg;
import Ornn.optim.Optimization;
import Ornn.optim.SSADestruction;
import Ornn.parser.ErrorListener;
import Ornn.parser.MxstarLexer;
import Ornn.parser.MxstarParser;
import Ornn.semantic.ToplevelScope;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "code.mx";
        String outputFile = "output.s";
        boolean runSemanticOnly = false, emitLLVM = false, debugCodegen = false, debugPhi = false, outputToStdout = false;
        int optLevel = 2;
        if (args.length > 0) {
            boolean readFileName = false;
            for (String arg : args) {
                if (arg.charAt(0) == '-') {
                    if (readFileName) {
                        throw new RuntimeException("expect file name, got " + arg);
                    }
                    switch (arg) {
                        case "-o":
                            readFileName = true;
                            break;
                        case "-ostdout":
                            outputToStdout = true;
                            break;
                        case "-O0":
                            optLevel = 0;
                            break;
                        case "-O1":
                            optLevel = 1;
                            break;
                        case "-O2":
                            optLevel = 2;
                            break;
                        case "-semantic":
                            runSemanticOnly = true;
                            break;
                        case "-emit-llvm":
                            emitLLVM = true;
                            break;
                        case "-debug-phi":
                            debugPhi = true;
                            break;
                        case "-debug-codegen":
                            debugCodegen = true;
                            break;
                        default:
                            throw new RuntimeException("unknown option " + arg);
                    }
                } else {
                    if (!readFileName) {
                        fileName = arg;
                    } else {
                        outputFile = arg;
                        readFileName = false;
                    }
                }
            }
        }
        //System.err.println("read from " + fileName + ", output to " + outputFile);
        /* Leak test data */
/*        {
            InputStream file = new FileInputStream(fileName);
            System.err.println(Arrays.toString(file.readAllBytes()));
            file.close();
        }
*/
        String pureName = fileName .substring(0, fileName.lastIndexOf("."));
        try {
            InputStream file = new FileInputStream(fileName);
            ProgramNode ast = buildAST(file);

            ToplevelScope toplevelScope = (new ToplevelScopeBuilder(ast)).getToplevelScope();
            new ScopeResolver(toplevelScope).visit(ast);
            new SemanticChecker(toplevelScope).visit(ast);

            if (runSemanticOnly) return;

            if (optLevel > 0) {
                new ConstantFolding(toplevelScope).visit(ast);
                new PrintOptimization(toplevelScope).visit(ast);
                new StaticArrayDetector().visit(ast);
            }

            IRBuilder irBuilder = new IRBuilder(toplevelScope, emitLLVM);
            irBuilder.visit(ast);

            if (optLevel > 1) {
                new Global2Local(irBuilder.root).run();
            }
            new Mem2Reg(irBuilder.root).run();
            if (optLevel > 1) {
                new Optimization(irBuilder.root).run();
            }
            if (emitLLVM) {
                PrintStream IRFile = new PrintStream(pureName + ".ll");
                new IRPrinter(irBuilder.root, IRFile).run();
                return;
            }

            new SSADestruction(irBuilder.root).run();
            if (debugPhi) {
                PrintStream IRFile = new PrintStream(pureName + ".ll");
                new IRPrinter(irBuilder.root, IRFile).run();
                return;
            }

            RVRoot rvRoot = (new InstSelector(irBuilder.root)).run();
            if (debugCodegen) {
                new RISCVPrinter(rvRoot, new PrintStream(outputFile), true).run();
                return;
            }

            new RegisterAllocation(rvRoot).run();
            //new RISCVDebugger(rvRoot, new PrintStream(outputFile)).run();
            //if (true) return;
            if (outputToStdout) {
                new RISCVPrinter(rvRoot, new PrintStream(System.out), true).run();
            }
            else {
                new RISCVPrinter(rvRoot, new PrintStream(outputFile), true).run();
            }
        } catch (Exception err) {
            //err.printStackTrace();
            System.err.println(err.getMessage());
            throw err;
        }
    }

    public static ProgramNode buildAST(InputStream file) throws Exception {
        MxstarLexer lexer = new MxstarLexer(CharStreams.fromStream(file));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ErrorListener());
        MxstarParser parser = new MxstarParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener());
        return (ProgramNode) (new ASTBuilder()).visit(parser.program());
    }
}
