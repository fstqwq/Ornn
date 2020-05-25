package Ornn.AST;

import Ornn.IR.operand.Operand;
import Ornn.AST.util.Position;
import Ornn.util.UnreachableCodeError;

public class NullLiteralNode extends ExprNode implements Literal {
    public NullLiteralNode(Position position) {
        super(position);
        equivalentConstant = this;
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
        throw new UnreachableCodeError();
    }
    @Override
    public Operand getResult() {
        return result;
    }
}
