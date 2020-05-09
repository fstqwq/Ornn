package Ornn;

import Ornn.AST.ProgramNode;
import Ornn.backend.IRBuilder;
import Ornn.IR.IRPrinter;
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

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "code.mx";
        boolean runSemanticOnly = false;
        boolean runCodegenOnly = false;

        if (args.length > 0) {
            for (String arg : args) {
                if (arg.charAt(0) == '-') {
                    switch (arg) {
                        case "-semantic":
                            runSemanticOnly = true;
                            break;
                        case "-codegen":
                            runCodegenOnly = true;
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

            new Mem2Reg(irBuilder.root).run();
            new SSADestruction(irBuilder.root).run();

            PrintStream IRFile = new PrintStream(pureName + ".ll");
            new IRPrinter(irBuilder.root, IRFile).run();

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
