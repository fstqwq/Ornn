package Ornn;

import Ornn.AST.ProgramNode;
import Ornn.frontend.ASTBuilder;
import Ornn.parser.MxstarLexer;
import Ornn.parser.MxstarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
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
        System.err.println("File name = " + fileName);
        try {
            InputStream file = new FileInputStream(fileName);

            ProgramNode ast = buildAST(file);

        } catch (Exception err) {
            err.printStackTrace();
            System.err.println(err.getMessage());
            throw new RuntimeException();
        }
    }

    public static ProgramNode buildAST(InputStream file) throws Exception {
        MxstarParser parser = new MxstarParser(new CommonTokenStream(new MxstarLexer(CharStreams.fromStream(file))));
        return (ProgramNode) (new ASTBuilder()).visit(parser.program());
    }
}
