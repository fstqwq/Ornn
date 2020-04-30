package Ornn;

import Ornn.AST.ProgramNode;
import Ornn.backend.IRBuilder;
import Ornn.backend.IRPrinter;
import Ornn.frontend.ASTBuilder;
import Ornn.frontend.ScopeResolver;
import Ornn.frontend.SemanticChecker;
import Ornn.frontend.ToplevelScopeBuilder;
import Ornn.parser.ErrorListener;
import Ornn.parser.MxstarLexer;
import Ornn.parser.MxstarParser;
import Ornn.semantic.ToplevelScope;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "code.mx";
        boolean runSemanticOnly = false;
        boolean runCodegenOnly = false;

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].charAt(0) == '-') {
                    switch (args[i]) {
                        case "-semantic":
                            runSemanticOnly = true;
                            break;
                        case "-codegen":
                            runCodegenOnly = true;
                            break;
                        default:
                            throw new RuntimeException("unknown option " + args[i]);
                    }
                } else {
                    fileName = args[i];
                }
            }
        } else {
            fileName = "code.mx";
        }
        String pureName = fileName .substring(0, fileName.lastIndexOf("."));
//        System.err.println("File name = " + fileName);
        try {
            InputStream file = new FileInputStream(fileName);
            ProgramNode ast = buildAST(file);

            ToplevelScope toplevelScope = (new ToplevelScopeBuilder(ast)).getToplevelScope();
            new ScopeResolver(toplevelScope).visit(ast);
            new SemanticChecker(toplevelScope).visit(ast);

            IRBuilder irBuilder = new IRBuilder(toplevelScope);
            irBuilder.visit(ast);

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
