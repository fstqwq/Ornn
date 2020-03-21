package Ornn.util;

import org.antlr.v4.runtime.Token;

public class Position {
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
    public String toString() {return "at line " + line + ", " +  col;}
}
