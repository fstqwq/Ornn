package Ornn.frontend;

import Ornn.AST.*;
import Ornn.AST.semantic.*;
import Ornn.util.CompilationError;
import Ornn.AST.util.Position;

import java.util.ArrayList;
import java.util.List;

/*
Toplevel Scope Builder: to do some nasty stuff
1. Define all builtin functions
2. Define types, and global functions
3. Check main function
4. Explicitly state all initializations
*/
public class ToplevelScopeBuilder {
    public final static ToplevelScope toplevelScope = new ToplevelScope();
    public final static ClassSymbol string = new ClassSymbol("string", null, toplevelScope);
    public final static PrimitiveTypeSymbol Int = new PrimitiveTypeSymbol("int");
    public final static PrimitiveTypeSymbol Bool = new PrimitiveTypeSymbol("bool");
    public final static PrimitiveTypeSymbol Void = new PrimitiveTypeSymbol("void");
    public final static NullType Null = new NullType();

    public ToplevelScopeBuilder(ProgramNode ast) {
        // define primitive types
        toplevelScope.defineType(Int);
        toplevelScope.defineType(Bool);
        toplevelScope.defineType(Void);
        toplevelScope.defineNull(Null);
        // define string
        string.defineFunction(new FunctionSymbol("length", Int, null, string));
        string.defineFunction(new FunctionSymbol("substring", string, null, string) {{
            defineVariable(new VariableSymbol("left", Int, null));
            defineVariable(new VariableSymbol("right", Int, null));
        }});
        string.defineFunction(new FunctionSymbol("parseInt", Int, null, string));
        string.defineFunction(new FunctionSymbol("ord", Int, null, string) {{
            defineVariable(new VariableSymbol("i", Int, null));
        }});
        toplevelScope.defineClass(string);
        // define builtin functions
        toplevelScope.defineFunction(new FunctionSymbol("array_size", Int, null, toplevelScope));
        toplevelScope.defineFunction(new FunctionSymbol("print", Void, null, toplevelScope) {{
            defineVariable(new VariableSymbol("str", string, null));
        }});
        toplevelScope.defineFunction(new FunctionSymbol("println", Void, null, toplevelScope) {{
            defineVariable(new VariableSymbol("str", string, null));
        }});
        toplevelScope.defineFunction(new FunctionSymbol("printInt", Void, null, toplevelScope) {{
            defineVariable(new VariableSymbol("n", Int, null));
        }});
        toplevelScope.defineFunction(new FunctionSymbol("printlnInt", Void, null, toplevelScope) {{
            defineVariable(new VariableSymbol("n", Int, null));
        }});
        toplevelScope.defineFunction(new FunctionSymbol("getString", string, null, toplevelScope));
        toplevelScope.defineFunction(new FunctionSymbol("getInt", Int, null, toplevelScope));
        toplevelScope.defineFunction(new FunctionSymbol("toString", string, null, toplevelScope) {{
            defineVariable(new VariableSymbol("i", Int, null));
        }});

        // add class declaration
        for (DeclNode x : ast.getDeclNodeList()) {
            if (x instanceof ClassDeclNode) {
                ClassSymbol classSymbol = new ClassSymbol(((ClassDeclNode) x).getIdentifier(), (ClassDeclNode) x, toplevelScope);
                toplevelScope.defineClass(classSymbol);
                ((ClassDeclNode) x).setClassSymbol(classSymbol);
            }
        }
        // add class member function declaration
        for (DeclNode x : ast.getDeclNodeList()) {
            if (x instanceof ClassDeclNode) {
                ClassSymbol classSymbol = ((ClassDeclNode) x).getClassSymbol();
                for (FuncDeclNode node : ((ClassDeclNode) x).getFuncDeclNodes()) {
                    SemanticType returnType = node.getReturnType() == null ? null : toplevelScope.resolveType(node.getReturnType());
                    FunctionSymbol functionSymbol = new FunctionSymbol(node.getIdentifier(), returnType, node, classSymbol);
                    node.setFunctionSymbol(functionSymbol);
                    classSymbol.defineFunction(functionSymbol);
                    if (functionSymbol.getSymbolName().equals(classSymbol.getSymbolName())) {
                        if (returnType != null) {
                            throw new CompilationError("constructor should not have a return type", node.getPosition());
                        } else if (classSymbol.getConstructor() != null) {
                            throw new CompilationError("multiple constructor in a single class", node.getPosition());
                        } else {
                            classSymbol.setConstructor(functionSymbol);
                        }
                    } else if (returnType == null) {
                        throw new CompilationError(String.format("constructor for %s appeared in %s", classSymbol.getSymbolName(), functionSymbol.getSymbolName()), node.getPosition());
                    }
                }
            }
        }
        // add var/func declaration
        List<StmtNode> initStmts = new ArrayList<>();
        FunctionSymbol mainFunc = null;
        for (DeclNode x : ast.getDeclNodeList()) {
            if (x instanceof FuncDeclNode) {
                FunctionSymbol functionSymbol = new FunctionSymbol(
                        ((FuncDeclNode) x).getIdentifier(),
                        toplevelScope.resolveType(((FuncDeclNode) x).getReturnType()),
                        (FuncDeclNode) x,
                        toplevelScope
                );
                ((FuncDeclNode) x).setFunctionSymbol(functionSymbol);
                toplevelScope.defineFunction(functionSymbol);
                functionSymbol.setScope(toplevelScope);

                if (functionSymbol.getSymbolName().equals("main")) {
                    if (mainFunc != null) {
                        throw new CompilationError("duplicate main function", x.getPosition());
                    } else {
                        mainFunc = functionSymbol;
                        if (!((FuncDeclNode) x).getParameterList().isEmpty()) {
                            throw new CompilationError("main function supports no arguments", mainFunc.getDefineNode().getPosition());
                        }
                        if (mainFunc.getType() != Int) {
                            throw new CompilationError("main function should return int" , mainFunc.getDefineNode().getPosition());
                        }
                    }
                }
            } else if (x instanceof VarDeclNode) {
                /*VariableSymbol variableSymbol = new VariableSymbol(
                        ((VarDeclNode) x).getIdentifier(),
                        toplevelScope.resolveType(((VarDeclNode) x).getType()),
                        (VarDeclNode) x
                );
                toplevelScope.defineVariable(variableSymbol);
                variableSymbol.setScope(toplevelScope);*/
                /* NOTE : Not support forward reference for global variables */

                if (((VarDeclNode) x).getExpr() != null) {
                    initStmts.add(
                            new ExprStmtNode(
                                    new BinaryExprNode(
                                            new IDExprNode(
                                                    ((VarDeclNode) x).getIdentifier(),
                                                    x.getPosition()
                                            ),
                                            ((VarDeclNode) x).getExpr(),
                                            "=",
                                            x.getPosition()
                                    ),
                                    x.getPosition()
                            )
                    );
                }
            }
        }
        // add init to check global initialization in semantic
        FuncDeclNode initFuncNode = new FuncDeclNode(
                new VoidTypeNode(Position.nowhere),
                "__init",
                new ArrayList<>(),
                new BlockStmtNode(initStmts, ast.getPosition()),
                Position.nowhere
        );
        FunctionSymbol initFuncSymbol = new FunctionSymbol("__init", Void, initFuncNode, toplevelScope);
        initFuncNode.setFunctionSymbol(initFuncSymbol);
        toplevelScope.defineFunction(initFuncSymbol);
        initFuncSymbol.setScope(toplevelScope);
        ast.getDeclNodeList().add(initFuncNode);

        // check main
        if (mainFunc == null) {
            throw new CompilationError("no main function", ast.getPosition());
        }

        ((FuncDeclNode) mainFunc.getDefineNode()).getBlock().getStmtList().
                add(0, new ExprStmtNode(new FuncCallExprNode(new IDExprNode("__init", Position.nowhere), new ArrayList<>(), Position.nowhere), Position.nowhere));
        ((FuncDeclNode) mainFunc.getDefineNode()).getBlock().getStmtList().add(
                new ReturnNode(new IntLiteralNode(0, Position.nowhere), mainFunc, Position.nowhere)
        );
    }

    public ToplevelScope getToplevelScope() {
        return toplevelScope;
    }
}
