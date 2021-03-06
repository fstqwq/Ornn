package Ornn.AST;

import Ornn.IR.operand.Operand;
import Ornn.AST.util.Position;
import Ornn.util.UnreachableCodeError;

public class StringLiteralNode extends ExprNode implements Literal {
    private String value;
    public StringLiteralNode(String value, Position position) {
        super(position);
        this.value = value;
        equivalentConstant = this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
        throw new UnreachableCodeError();
    }
    @Override
    public String getStr() {
        return value;
    }
    @Override
    public Operand getResult() {
        return result;
    }
}
