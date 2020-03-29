package Ornn.frontend;

import Ornn.AST.*;
import Ornn.semantic.*;
import Ornn.util.CompilationError;

import java.util.Iterator;
import java.util.Map;

import static Ornn.semantic.TypeCategory.*;

public class SemanticChecker implements ASTVisitor {
    private ToplevelScope toplevelScope;
    private PrimitiveTypeSymbol Int;
    private PrimitiveTypeSymbol Void;
    private PrimitiveTypeSymbol Bool;
    private ClassSymbol string;
    private NullType Null;
    private FunctionSymbol arraySize;
    public SemanticChecker(ToplevelScope toplevelScope) {
        this.toplevelScope = toplevelScope;
        Int = (PrimitiveTypeSymbol) toplevelScope.resolveType("int", null);
        Void = (PrimitiveTypeSymbol) toplevelScope.resolveType("void", null);
        Bool = (PrimitiveTypeSymbol) toplevelScope.resolveType("bool", null);
        string = (ClassSymbol) toplevelScope.resolveType("string", null);
        Null = (NullType) toplevelScope.resolveType("null", null);
        arraySize = (FunctionSymbol) toplevelScope.resolveSymbol("<size>", null);
    }

    @Override
    public void visit(ProgramNode node) {
        node.getDeclNodeList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(FuncDeclNode node) {
        node.getBlock().accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        for (FuncDeclNode funcDeclNode : node.getFuncDeclNodes()) {
            funcDeclNode.accept(this);
        }
    }

    @Override
    public void visit(VarDeclNode node) {
        Type type = node.getTypeAfterResolve();
        if (node.getExpr() != null) {
            node.getExpr().accept(this);
            type.compatible(node.getExpr().getType(), node.getPosition());
        }
    }

    @Override
    public void visit(BlockStmtNode node) {
        node.getStmtList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        node.getVarDeclList().getDeclList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(IfStmtNode node) {
        node.getExpr().accept(this);
        Bool.compatible(node.getExpr().getType(), node.getPosition());
        node.getThenStmt().accept(this);
        if (node.getElseStmt() != null) {
            node.getElseStmt().accept(this);
        }
    }

    @Override
    public void visit(WhileStmtNode node) {
        node.getExpr().accept(this);
        Bool.compatible(node.getExpr().getType(), node.getPosition());
        node.getStmt().accept(this);
    }

    @Override
    public void visit(ForStmtNode node) {
        if (node.getInit() != null) node.getInit().accept(this);
        if (node.getCond() == null) {
            node.setCond(new BoolLiteralNode(true, node.getPosition()));
        } else {
            node.getCond().accept(this);
            Bool.compatible(node.getCond().getType(), node.getCond().getPosition());
        }
        if (node.getStep() != null) node.getStep().accept(this);
        node.getStmt().accept(this);
    }

    @Override
    public void visit(ReturnNode node) {
        Type returnType = node.getFunctionSymbol().getType();
        if (node.getExpr() == null) {
            if (returnType != null && returnType != Void) {
                throw new CompilationError("return expression expected", node.getPosition());
            }
        } else {
            if (returnType == null || returnType == Void) {
                throw new CompilationError("return expression unexpected", node.getPosition());
            }
            node.getExpr().accept(this);
            returnType.compatible(node.getExpr().getType(), node.getPosition());
        }
    }

    @Override
    public void visit(IDExprNode node) {
        Symbol symbol = node.getSymbol();
        if (symbol instanceof VariableSymbol) {
            node.setTypeCategory(LVALUE);
            node.setType(symbol.getType());
        } else if (symbol instanceof ClassSymbol) {
            node.setTypeCategory(CLASS);
            node.setType((ClassSymbol) symbol);
        } else if (symbol instanceof FunctionSymbol) {
            node.setTypeCategory(FUNCTION);
            node.setType(symbol.getType());
            node.setFunctionSymbol((FunctionSymbol) symbol);
        } else {
            throw new CompilationError("unexpected " + symbol.getSymbolName(), node.getPosition());
        }
    }

    @Override
    public void visit(ArrayIndexNode node) {
        node.getArray().accept(this);
        node.getIndex().accept(this);
        if (!(node.getArray().getType() instanceof ArrayType)) {
            throw new CompilationError("non-array can't be indexed", node.getPosition());
        }
        if (!node.getIndex().isInt()) {
            throw new CompilationError("only int can be index", node.getPosition());
        }
        node.setTypeCategory(LVALUE);
        ArrayType type = (ArrayType) node.getArray().getType();
        if (type.getDimension() == 1) {
            node.setType(type.getBaseType());
        } else {
            node.setType(new ArrayType(type.getBaseType(), type.getDimension() - 1));
        }
    }

    @Override
    public void visit(BinaryExprNode node) {
        ExprNode lhs = node.getLhs();
        ExprNode rhs = node.getRhs();
        lhs.accept(this);
        rhs.accept(this);
        switch (node.getOp()) {
            case "*": case "/":  case "%":
            case "-":
            case "<<": case ">>":
            case "&":
            case "^":
            case "|":
                Int.compatible(lhs.getType(), node.getPosition());
                Int.compatible(rhs.getType(), node.getPosition());
                node.setType(Int);
                node.setTypeCategory(RVALUE);
                break;
            case "&&":
            case "||":
                Bool.compatible(lhs.getType(), node.getPosition());
                Bool.compatible(rhs.getType(), node.getPosition());
                node.setType(Bool);
                node.setTypeCategory(RVALUE);
                break;
            case "+":
                if (!lhs.isInt() || !rhs.isInt()) {
                    if (!lhs.isString() || !rhs.isString()) {
                        throw new CompilationError(
                                "unable to evaluate "
                                        + lhs.getType().getTypeName()
                                        + "+"
                                        + rhs.getType().getTypeName(),
                                node.getPosition()
                        );
                    }
                }
                node.setType(lhs.getType());
                node.setTypeCategory(RVALUE);
                break;
            case "<": case ">": case "<=": case ">=":
                if (!lhs.isInt() || !rhs.isInt()) {
                    if (!lhs.isString() || !rhs.isString()) {
                        throw new CompilationError(
                                "unable to evaluate "
                                + lhs.getType().getTypeName()
                                + node.getOp()
                                + rhs.getType().getTypeName(),
                                node.getPosition()
                        );
                    }
                }
                node.setType(Bool);
                node.setTypeCategory(RVALUE);
                break;
            case "==": case "!=":
                lhs.getType().equable(rhs.getType(), node.getPosition());
                node.setType(Bool);
                node.setTypeCategory(RVALUE);
                break;
            case "=":
                if (lhs.isLvalue()) {
                    lhs.getType().compatible(rhs.getType(), node.getPosition());
                } else {
                    throw new CompilationError("left value can't be assigned", node.getPosition());
                }
                node.setType(lhs.getType());
                node.setTypeCategory(LVALUE);
                break;
            default:
                throw new CompilationError("unknown error", node.getPosition());
        }
    }

    @Override
    public void visit(UnaryExprNode node) {
        node.getExpr().accept(this);
        switch (node.getOp()) {
            case "++i": case "--i":
                if (!node.getExpr().isLvalue()) {
                    throw new CompilationError(node.getExpr().getType().getTypeName() + " is not left value", node.getPosition());
                }
                Int.compatible(node.getExpr().getType(), node.getPosition());
                node.setType(Int);
                node.setTypeCategory(LVALUE);
                break;
            case "i++": case "i--":
                if (!node.getExpr().isLvalue()) {
                    throw new CompilationError(node.getExpr().getType().getTypeName() + " is not left value", node.getPosition());
                }
            case "+":
            case "-":
            case "~":
                Int.compatible(node.getExpr().getType(), node.getPosition());
                node.setType(Int);
                node.setTypeCategory(RVALUE);
                break;
            case "!":
                Bool.compatible(node.getExpr().getType(), node.getPosition());
                node.setType(Bool);
                node.setTypeCategory(RVALUE);
                break;
            default:
                throw new CompilationError("unknown unary operator", node.getPosition());
        }
    }

    @Override
    public void visit(ClassMemberNode node) {
        node.getExpr().accept(this);
        if (node.getExpr().isClassValue()) {
            Symbol memberSymbol = ((ClassSymbol) node.getExpr().getType()).accessMember(node.getIdentifier(), node.getPosition());
            if (memberSymbol instanceof FunctionSymbol) {
                node.setTypeCategory(FUNCTION);
                node.setType(memberSymbol.getType());
                node.setFunctionSymbol((FunctionSymbol) memberSymbol);
            } else {
                node.setTypeCategory(LVALUE);
                node.setType(memberSymbol.getType());
            }
            node.setSymbol(memberSymbol);
        } else if (node.getExpr().isArray()) {
            if (node.getIdentifier().equals("size")) {
                node.setType(Int);
                node.setTypeCategory(FUNCTION);
                node.setFunctionSymbol(arraySize);
            } else {
                throw new CompilationError("size is the only member of array", node.getPosition());
            }
        } else {
            throw new CompilationError("accessing inaccessible expression", node.getPosition());
        }
    }

    @Override
    public void visit(FuncCallExprNode node) {
        ExprNode function = node.getFunction();
        function.accept(this);
        if (!function.isFunction()) {
            throw new CompilationError("calling expression which is not a function", node.getPosition());
        }
        if (node.getParameterList().size() != function.getFunctionSymbol().getArguments().size()) {
            throw new CompilationError("number of calling parameters don't match with arguments", node.getPosition());
        }
        for (ExprNode exprNode : node.getParameterList()) {
            exprNode.accept(this);
        }
        Iterator<ExprNode> exprNodeIterator = node.getParameterList().iterator();
        for (Map.Entry<String, VariableSymbol> entry : function.getFunctionSymbol().getArguments().entrySet()) {
            VariableSymbol variableSymbol = entry.getValue();
            ExprNode exprNode = exprNodeIterator.next();
            variableSymbol.getType().compatible(exprNode.getType(), exprNode.getPosition());
        }
        node.setFunctionSymbol(function.getFunctionSymbol());
        node.setType(node.getFunctionSymbol().getType());
        node.setTypeCategory(RVALUE);
    }

    @Override
    public void visit(NewExprNode node) {
        for (ExprNode exprNode : node.getExprNodeList()) {
            exprNode.accept(this);
            if (!(exprNode.isInt())) {
                throw new CompilationError("only int can be subscript", node.getPosition());
            }
        }
        Type type = node.getBaseTypeAfterResolve();
        if (node.getDimension() == 0) {
            if (type instanceof ClassSymbol) {
                if (((ClassSymbol) type).getConstructor() != null){
                    node.setFunctionSymbol(((ClassSymbol) type).getConstructor());
                }
            }
            node.setType(type);
        } else {
            node.setType(new ArrayType(type, node.getDimension()));
        }
        node.setTypeCategory(RVALUE);
    }

    @Override
    public void visit(ThisExprNode node) {
        node.setType((ClassSymbol) node.getScope());
        node.setTypeCategory(RVALUE);
    }

    @Override
    public void visit(IntLiteralNode node) {
        node.setType(Int);
        node.setTypeCategory(RVALUE);
    }
    @Override
    public void visit(BoolLiteralNode node) {
        node.setType(Bool);
        node.setTypeCategory(RVALUE);
    }
    @Override
    public void visit(NullLiteralNode node) {
        node.setType(Null);
        node.setTypeCategory(RVALUE);
    }
    @Override
    public void visit(StringLiteralNode node) {
        node.setType(string);
        node.setTypeCategory(RVALUE);
    }

    @Override public void visit(ArrayTypeNode node) {}
    @Override public void visit(ClassTypeNode node) {}
    @Override public void visit(BoolTypeNode node) {}
    @Override public void visit(IntTypeNode node) {}
    @Override public void visit(VoidTypeNode node) {}
    @Override public void visit(StringTypeNode node) {}
    @Override public void visit(BreakNode node) {}
    @Override public void visit(ContinueNode node) {}
}
