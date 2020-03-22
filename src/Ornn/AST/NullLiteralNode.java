package Ornn.AST;

import Ornn.util.Position;

public class NullLiteralNode extends ExprNode {
    public NullLiteralNode(Position position) {
        super(position);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
