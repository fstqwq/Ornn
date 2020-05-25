package Ornn.util;

public class UnreachableCodeError extends RuntimeException {
    public UnreachableCodeError() {
        super("You asshole have written some shit code and things are fucked up!");
    }
}
