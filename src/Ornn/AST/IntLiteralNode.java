package Ornn.AST;

import Ornn.IR.operand.Operand;
import Ornn.AST.util.Position;
import Ornn.util.UnreachableError;

public class IntLiteralNode extends ExprNode implements Literal {
    private long value;
    // why long ?
    // well, it's not required to process -2147483648, but will you think it ugly if we can't do that?
    // with long and frontend constant folding, we can do it
    public IntLiteralNode(long value, Position position) {
        super(position);
        this.value = value;
        equivalentConstant = this;
    }

    public long getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long getInt() {
        return value;
    }

    @Override
    public boolean getBool() {
        throw new UnreachableError();
    }
    @Override
    public String getStr() {
        throw new UnreachableError();
    }
    @Override
    public Operand getResult() {
        return result;
    }
}
