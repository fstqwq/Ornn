package Ornn.frontend;

import Ornn.AST.*;
import Ornn.parser.MxstarBaseVisitor;
import Ornn.parser.MxstarParser;
import Ornn.util.Position;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends MxstarBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(MxstarParser.ProgramContext ctx) {
        List<DeclNode> declNodeList = new ArrayList<>();
        if (ctx.programSection() != null) {
            for (ParserRuleContext programSection : ctx.programSection()) {
                ASTNode decl = visit(programSection);
                if (decl instanceof VarDeclNode) declNodeList.addAll(((VarDeclListNode) decl).getDeclList());
                else declNodeList.add((DeclNode) decl);
            }
        }
        return new ProgramNode(declNodeList, new Position(ctx.getStart()));
    }
}
