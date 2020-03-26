package Ornn.util;

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
        return "[Error]: " + super.getMessage() + " " + pos.toString();
    }
}
