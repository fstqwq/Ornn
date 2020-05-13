package Ornn;

import Ornn.AST.ProgramNode;
import Ornn.RISCV.RISCVPrinter;
import Ornn.RISCV.RVRoot;
import Ornn.backend.IRBuilder;
import Ornn.IR.IRPrinter;
import Ornn.backend.InstSelector;
import Ornn.backend.RegisterAllocation;
import Ornn.frontend.*;
import Ornn.optim.Mem2Reg;
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
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "code.mx";
        boolean runSemanticOnly = false, emitLLVM = false, debugCodegen = false;
        int optLevel = 2;
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.charAt(0) == '-') {
                    switch (arg) {
                        case "-semantic":
                            runSemanticOnly = true;
                            break;
                        case "-emit-llvm":
                            emitLLVM = true;
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
                        case "-debug-codegen":
                            debugCodegen = true;
                            break;
                        default:
                            throw new RuntimeException("unknown option " + arg);
                    }
                } else {
                    fileName = arg;
                }
            }
        } else {
            fileName = "code.mx";
        }

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

            new ConstantFolding(toplevelScope).visit(ast);
            new PrintOptimization(toplevelScope).visit(ast);

            IRBuilder irBuilder = new IRBuilder(toplevelScope);
            irBuilder.visit(ast);

            if (optLevel > 1) {
                new Mem2Reg(irBuilder.root).run();
            }
            if (emitLLVM) {
                PrintStream IRFile = new PrintStream(pureName + ".ll");
                new IRPrinter(irBuilder.root, IRFile).run();
            }

            new SSADestruction(irBuilder.root).run();


            RVRoot rvRoot = (new InstSelector(irBuilder.root)).run();

            if (debugCodegen) {
                new RISCVPrinter(rvRoot, System.out, true).run();
                return;
            }

            new RegisterAllocation(rvRoot).run();

            new RISCVPrinter(rvRoot, System.out, true).run();

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
