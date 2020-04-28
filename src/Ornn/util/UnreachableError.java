package Ornn.util;

public class UnreachableError extends RuntimeException {
    public UnreachableError() {
        super("You asshole have written some shit code and things are fucked up!");
    }
}
