package Ornn.AST;

import Ornn.IR.operand.Operand;
import Ornn.AST.util.Position;
import Ornn.util.UnreachableCodeError;

public class BoolLiteralNode extends ExprNode implements Literal{
    boolean value;
    public BoolLiteralNode (boolean value, Position position) {
        super(position);
        this.value = value;
        equivalentConstant = this;
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
        throw new UnreachableCodeError();
    }

    @Override
    public boolean getBool() {
        return value;
    }
    @Override
    public String getStr() {
        throw new UnreachableCodeError();
    }
    @Override
    public Operand getResult() {
        return result;
    }
}
