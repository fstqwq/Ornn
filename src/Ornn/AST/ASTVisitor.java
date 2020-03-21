package Ornn.AST;

public interface ASTVisitor {
    /* program root*/
    void visit(ProgramNode node);

    /* declarations */
    void visit(FuncDeclNode node);
    void visit(ClassDeclNode node);
    void visit(VarDeclNode node);

    /* types */
    void visit(ArrayTypeNode node);
    void visit(ClassTypeNode node);
    void visit(BoolTypeNode node);
    void visit(IntTypeNode node);
    void visit(VoidTypeNode node);
    void visit(StringTypeNode node);

    /* statements */
    void visit(BlockStmtNode node);
    void visit(VarDeclStmtNode node);
    void visit(ExprStmtNode node);
    void visit(IfStmtNode node);
    void visit(WhileStmtNode node);
    void visit(ForStmtNode node);
    void visit(ReturnNode node);
    void visit(BreakNode node);
    void visit(ContinueNode node);

    /* expressions */
    void visit(IDExprNode node);
    void visit(ArrayIndexNode node);
    void visit(BinaryExprNode node);
    void visit(ClassMemberNode node);
    void visit(FuncCallExprNode node);
    void visit(NewExprNode node);
    void visit(ThisExprNode node);
    void visit(UnaryExprNode node);

    /* literals */
    void visit(IntLiteralNode node);
    void visit(BoolLiteralNode node);
    void visit(NullLiteralNode node);
    void visit(StringLiteralNode node);
}
