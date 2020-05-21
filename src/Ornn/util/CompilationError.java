package Ornn.util;

import Ornn.AST.util.Position;

public class CompilationError extends RuntimeException {
    private Position pos;

    public CompilationError() {
        pos = null;
    }

    public CompilationError(String msg, Position pos) {
        super(msg);
        this.pos = pos;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + pos.toString();
    }
}
