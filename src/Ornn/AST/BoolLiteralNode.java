package Ornn.AST;

import Ornn.IR.operand.Operand;
import Ornn.util.Position;
import Ornn.util.UnreachableError;

public class BoolLiteralNode extends ExprNode implements Literal{
    boolean value;
    public BoolLiteralNode (boolean value, Position position) {
        super(position);
        this.value = value;
    }
    public boolean getValue() {
        return value;
    }
    public void setValue(boolean value) {
        this.value = value;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long getInt() {
        throw new UnreachableError();
    }

    @Override
    public boolean getBool() {
        return value;
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
