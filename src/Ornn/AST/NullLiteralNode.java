package Ornn.AST;

import Ornn.IR.operand.Operand;
import Ornn.util.Position;
import Ornn.util.UnreachableError;

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
        throw new UnreachableError();
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
