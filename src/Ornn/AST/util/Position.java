package Ornn.AST.util;

import org.antlr.v4.runtime.Token;

public class Position {
    public static final Position nowhere = new Position(-1, -1);
    private int line, col;
    public Position(int Line, int Col) {
        line    = Line;
        col     = Col;
    }
    public Position(Token token) {
        line    = token.getLine();
        col     = token.getCharPositionInLine();
    }
    public int getLine() {return line;}
    public int getCol() {return col;}
    public String toString() {return String.format(" (at line : %d, col : %d)", line, col);}
}
