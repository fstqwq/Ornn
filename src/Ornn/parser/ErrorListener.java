package Ornn.parser;

import Ornn.util.CompilationError;
import Ornn.AST.util.Position;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new CompilationError("[Syntax Error] " + msg, new Position(line, charPositionInLine));
    }
}
