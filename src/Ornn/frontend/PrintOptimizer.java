package Ornn.frontend;

import Ornn.AST.*;
import Ornn.AST.semantic.FunctionSymbol;
import Ornn.AST.semantic.ToplevelScope;
import Ornn.AST.semantic.TypeCategory;

import java.util.ArrayList;
import java.util.HashMap;

import static Ornn.AST.semantic.TypeCategory.RVALUE;


/*
    It's peephole at the HIR stage.
    String operations are slow and nasty, so even this optimization is ugly, I have to do it.
    With help of frontend constant folding, it can be magically useful.
 */

public class PrintOptimizer implements ASTVisitor {
    FunctionSymbol print, println, printInt, printlnInt, toString;
    HashMap<FunctionSymbol, FunctionSymbol> asInt = new HashMap<>();
    HashMap<FunctionSymbol, FunctionSymbol> asStr = new HashMap<>();
    public PrintOptimizer(ToplevelScope toplevelScope) {
        print = (FunctionSymbol) toplevelScope.resolveSymbol("print", null);
        println = (FunctionSymbol) toplevelScope.resolveSymbol("println", null);
        printInt = (FunctionSymbol) toplevelScope.resolveSymbol("printInt", null);
        printlnInt = (FunctionSymbol) toplevelScope.resolveSymbol("printlnInt", null);
        toString = (FunctionSymbol) toplevelScope.resolveSymbol("toString", null);
        asInt.put(print, printInt);
        asInt.put(println, printlnInt);
        asStr.put(printInt, print);
        asStr.put(printlnInt, println);
    }

    private FuncCallExprNode newFuncCall(FunctionSymbol symbol, ExprNode arg) {
        IDExprNode node = new IDExprNode(symbol.getSymbolName(), arg.getPosition());
        node.setType(symbol.getType());
        node.setTypeCategory(TypeCategory.FUNCTION);
        node.setFunctionSymbol(symbol);
        FuncCallExprNode funNode = new FuncCallExprNode(node, new ArrayList<>(){{add(arg);}}, arg.getPosition());
        funNode.setFunctionSymbol(symbol);
        funNode.setType(symbol.getType());
        funNode.setTypeCategory(RVALUE);
        return funNode;
    }
    private ArrayList<FuncCallExprNode> reprint(FuncCallExprNode start) {
        ArrayList <FuncCallExprNode> list = new ArrayList<>(){{add(start);}};
        boolean changed = true;
        while (changed) {
            changed = false;
            ArrayList <FuncCallExprNode> newList = new ArrayList<>();
            for (FuncCallExprNode node : list) {
                FunctionSymbol symbol = node.getFunctionSymbol();
                switch (symbol.getSymbolName()) {
                    case "print": case "println":
                        changed |= testPrint(newList, node, node.getParameterList().get(0), symbol, asInt.get(symbol));
                        break;
                    default:
                        newList.add(node);
                        break;
                }
            }
            list = newList;
        }
        return list;
    }

    private boolean testPrint(ArrayList<FuncCallExprNode> newList, FuncCallExprNode node, ExprNode arg, FunctionSymbol symbol, FunctionSymbol symbolAsInt) {
        if (arg.isPureConstant()) {
            newList.add(node);
            return false;
        }
        else if (arg instanceof BinaryExprNode) {
            newList.add(newFuncCall(print, ((BinaryExprNode) arg).getLhs()));
            newList.add(newFuncCall(symbol, ((BinaryExprNode) arg).getRhs()));
            return true;
        } else if (arg instanceof FuncCallExprNode && arg.getFunctionSymbol().equals(toString)) {
            newList.add(newFuncCall(symbolAsInt, ((FuncCallExprNode) arg).getParameterList().get(0)));
            return true;
        } else {
            newList.add(node);
            return false;
        }
    }

    @Override
    public void visit(ProgramNode node) {
        for (DeclNode declNode : node.getDeclNodeList()) {
            declNode.accept(this);
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        node.getBlock().accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        node.getFuncDeclNodes().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(VarDeclNode node) {}

    @Override
    public void visit(BlockStmtNode node) {
        ArrayList<StmtNode> list = new ArrayList<>();
        for (StmtNode stmtNode : node.getStmtList()) {
            boolean success = false;
            if (stmtNode instanceof ExprStmtNode) {
                ExprNode expr = ((ExprStmtNode) stmtNode).getExpr();
                if (expr instanceof FuncCallExprNode) {
                    FunctionSymbol functionSymbol = expr.getFunctionSymbol();
                    if (functionSymbol.equals(print) || functionSymbol.equals(println)) {
                        ArrayList<FuncCallExprNode> ret = reprint((FuncCallExprNode)expr);
                        ret.forEach(x -> list.add(new ExprStmtNode(x, stmtNode.getPosition())));
                        success = true;
                    } else if (functionSymbol.equals(printInt) || functionSymbol.equals(printlnInt)) {
                        if (((FuncCallExprNode) expr).getParameterList().get(0).isPureConstant()) {
                            FuncCallExprNode funcCall = newFuncCall(asStr.get(functionSymbol),
                                    new StringLiteralNode("" + ((FuncCallExprNode) expr).getParameterList().get(0).equivalentConstant.getInt(), stmtNode.getPosition()));
                            list.add(new ExprStmtNode(funcCall, stmtNode.getPosition()));
                            success = true;
                        }
                    }
                }
            } else {
                stmtNode.accept(this);
            }
            if (!success) {
                list.add(stmtNode);
            }
        }
        ArrayList<StmtNode> prelist = new ArrayList<>(list);
        list.clear();
        for (StmtNode stmtNode : prelist) {
            list.add(stmtNode);
            if (list.size() > 1) { // combine contiguous print const str
                int last = list.size() - 1;
                if (list.get(last) instanceof ExprStmtNode && list.get(last - 1) instanceof ExprStmtNode
                        && ((ExprStmtNode) list.get(last)).getExpr() instanceof FuncCallExprNode
                        && ((ExprStmtNode) list.get(last - 1)).getExpr() instanceof FuncCallExprNode) {
                    FuncCallExprNode las = ((FuncCallExprNode)((ExprStmtNode) list.get(last)).getExpr());
                    FuncCallExprNode sec = ((FuncCallExprNode)((ExprStmtNode) list.get(last - 1)).getExpr());
                    if ((las.getFunctionSymbol().equals(print) || las.getFunctionSymbol().equals(println))
                            && (sec.getFunctionSymbol().equals(print) || sec.getFunctionSymbol().equals(println))
                            && las.getParameterList().get(0).isPureConstant()
                            && sec.getParameterList().get(0).isPureConstant()) {
                        String newStr = sec.getParameterList().get(0).equivalentConstant.getStr()
                                + (sec.getFunctionSymbol().equals(print) ? "" : "\n")
                                + las.getParameterList().get(0).equivalentConstant.getStr();
                        FunctionSymbol symbol = las.getFunctionSymbol();
                        list.remove(last);
                        list.remove(last - 1);
                        list.add(new ExprStmtNode(newFuncCall(symbol, new StringLiteralNode(newStr, node.getPosition())), node.getPosition()));
                    }
                }
            }
        }
        node.setStmtList(list);
    }

    @Override
    public void visit(VarDeclStmtNode node) {
    }

    @Override
    public void visit(ExprStmtNode node) {
    }

    @Override
    public void visit(IfStmtNode node) {
        node.getThenStmt().accept(this);
        if (node.getElseStmt() != null) {
            node.getElseStmt().accept(this);
        }
    }

    @Override
    public void visit(WhileStmtNode node) {
        node.getStmt().accept(this);
    }

    @Override
    public void visit(ForStmtNode node) {
        node.getStmt().accept(this);
    }

    @Override
    public void visit(ReturnNode node) {}

    @Override
    public void visit(BreakNode node) {
    }

    @Override
    public void visit(ContinueNode node) {
    }

    @Override
    public void visit(IDExprNode node) {
    }

    @Override
    public void visit(ArrayIndexNode node) {

    }

    @Override
    public void visit(BinaryExprNode node) {
    }

    @Override
    public void visit(UnaryExprNode node) {
    }

    @Override
    public void visit(ClassMemberNode node) {
    }

    @Override
    public void visit(FuncCallExprNode node) {

    }

    @Override
    public void visit(NewExprNode node) {
    }

    @Override
    public void visit(ThisExprNode node) {
    }


    @Override public void visit(IntLiteralNode node) {
    }
    @Override public void visit(BoolLiteralNode node) {
    }
    @Override public void visit(NullLiteralNode node) {
    }
    @Override public void visit(StringLiteralNode node) {
    }

    @Override public void visit(ArrayTypeNode node) {}
    @Override public void visit(ClassTypeNode node) {}
    @Override public void visit(BoolTypeNode node) {}
    @Override public void visit(IntTypeNode node) {}
    @Override public void visit(VoidTypeNode node) {}
    @Override public void visit(StringTypeNode node) {}
}
