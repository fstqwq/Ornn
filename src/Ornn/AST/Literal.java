package Ornn.AST;

import Ornn.IR.operand.Operand;

public interface Literal {
    long getInt();
    String getStr();
    boolean getBool();
    void accept(ASTVisitor visitor);
    Operand getResult();
}
