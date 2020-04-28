package Ornn;

import Ornn.AST.ProgramNode;
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

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName;
        if (args.length > 0) {
            fileName = args[0];
        } else {
            fileName = "code.mx";
        }
//        System.err.println("File name = " + fileName);
        try {
            InputStream file = new FileInputStream(fileName);
            ProgramNode ast = buildAST(file);

            ToplevelScope toplevelScope = (new ToplevelScopeBuilder(ast)).getToplevelScope();
            new ScopeResolver(toplevelScope).visit(ast);
            new SemanticChecker(toplevelScope).visit(ast);
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
