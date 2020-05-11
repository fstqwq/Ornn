package Ornn.backend;

import Ornn.AST.*;
import Ornn.IR.*;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.*;
import Ornn.IR.type.*;
import Ornn.semantic.*;
import Ornn.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static Ornn.util.Constant.*;

/*
Naming of registers : for debug, will rename in the future
 */


public class IRBuilder implements ASTVisitor {
    public Root root;
    ClassSymbol currentClass = null;
    Function currentFunction = null;
    BasicBlock currentBlock = null;
    boolean visitingParams = false;
    ArrayList <Return> returns = new ArrayList<>();
    public HashSet<Symbol> constVars;

    private static class PhiValue {
        public ArrayList<BasicBlock> blocks = new ArrayList<>();
        public ArrayList<Operand> values = new ArrayList<>();
    };

    public IRBuilder (ToplevelScope toplevelScope) {
        root = new Root(toplevelScope);
    }

    @Override
    public void visit(ProgramNode node) {
        for (DeclNode declNode : node.getDeclNodeList()) {
            if (declNode instanceof ClassDeclNode) {
                root.addType(((ClassDeclNode) declNode).getClassSymbol().getTypeName(), new ClassType(((ClassDeclNode) declNode).getClassSymbol().getTypeName()));
            }
        }
        for (DeclNode declNode : node.getDeclNodeList()) {
            if (declNode instanceof ClassDeclNode) {
                ClassType type = root.getType(((ClassDeclNode) declNode).getClassSymbol().getTypeName());
                for (FuncDeclNode funcDeclNode : ((ClassDeclNode) declNode).getFuncDeclNodes()) {
                    Function function = new Function(((ClassDeclNode) declNode).getIdentifier() + "." + funcDeclNode.getIdentifier(), true);
                    root.addFunction(function);
                    function.returnType = root.resolveType(funcDeclNode.getFunctionSymbol().getType(), false);
                    function.paramTypes.add(new Pointer(type));
                    for (VarDeclNode param : funcDeclNode.getParameterList()) {
                        function.paramTypes.add(root.resolveType(param.getTypeAfterResolve(), false));
                    }
                    funcDeclNode.getFunctionSymbol().function = function;
                }
                for (VarDeclNode member : ((ClassDeclNode) declNode).getVarDeclNodes()) {
                    BaseType memberType = root.resolveType(member.getTypeAfterResolve(), true);
                    type.addMember(memberType);
                    member.getVariableSymbol().operand = new Register(member.getIdentifier(), memberType);
                    member.getVariableSymbol().index = new ConstInt(type.members.size() - 1, 32);
                }
                if (type.size == 0) {
                    type.size = 8; // malloc works
                }
            } else if (declNode instanceof FuncDeclNode) {
                Function function = new Function(((FuncDeclNode) declNode).getIdentifier(), true);
                root.addFunction(function);
                function.returnType = root.resolveType(((FuncDeclNode) declNode).getFunctionSymbol().getType(), false);
                for (VarDeclNode param : ((FuncDeclNode)declNode).getParameterList()) {
                    function.paramTypes.add(root.resolveType(param.getTypeAfterResolve(), false));
                }
                ((FuncDeclNode) declNode).getFunctionSymbol().function = function;
            }
        }
        node.getDeclNodeList().forEach(x -> x.accept(this));
    }

    /*  When we refer to global values, we use the pointer to refer to it.
        Corner cases: Pointer comparisons, int[] to null, string to string, so on.
     */
    private Register loadPointer(Operand operand) {
        Register dest;
        if (((Pointer) operand.type).typePointedTo instanceof ArrayType) {
            // is a string!
            dest = new Register("load_str", STR);
            currentBlock.pushBack(new GEP(((Pointer) operand.type).typePointedTo, operand, I32ZERO, I32ZERO, dest, currentBlock));
        }  else {
            dest = new Register("load_" + operand.name, ((Pointer) operand.type).typePointedTo);
            currentBlock.pushBack(new Load(dest, operand, currentBlock));
            if (dest.type.isSame(I8)) { // stored bool is i8
                Register castDest = new Register("i82bool", BOOL);
                currentBlock.pushBack(new Cast(dest, castDest, currentBlock));
                return castDest;
            }
        }
        return dest;
    }
    private Operand matchType(Operand operand, BaseType expectedType) {
        if (operand.type.isSame(expectedType)) {
            return operand;
        } else if (expectedType instanceof Pointer && operand instanceof Null) {
            return operand;
        }
        assert operand.type instanceof Pointer;
        assert ((Pointer) operand.type).typePointedTo.isSame(expectedType);
        return loadPointer(operand);
    }
    private Operand loadValue(Operand operand) {
        if (operand.type instanceof Pointer) {
            return loadPointer(operand);
        }
        return operand;
    }
    private void insertAssignment(Operand lhs, ExprNode rhsNode) {
        rhsNode.accept(this);
        Operand rhs = rhsNode.result;
        assert lhs.type instanceof Pointer;
        Operand value;
        Inst inst;
        if (rhs.type instanceof BoolType) {
            if (rhs instanceof ConstBool) {
                value = ((ConstBool) rhs).value ? I8TRUE : I8FALSE;
            } else {
                inst = new Cast((Register) rhs, new Register("bool2i8",I8), currentBlock);
                value = ((Cast)inst).dest;
                currentBlock.pushBack(inst);
            }
        } else {
            value = matchType(rhs, ((Pointer) lhs.type).typePointedTo);
        }
        inst = new Store(lhs, value, currentBlock);
        currentBlock.pushBack(inst);
    }

    HashMap<BasicBlock, PhiValue> requestPhi = new HashMap<>();
    private void solveBranch(ExprNode expr) {
        if (expr.hasCondition()) {
            Operand res = matchType(expr.result, BOOL);
            currentBlock.pushBack(new Branch(res, expr.thenDest, expr.elseDest, currentBlock));
            if (requestPhi.containsKey(expr.thenDest)) {
                PhiValue phiValue = requestPhi.get(expr.thenDest);
                phiValue.values.add(TRUE);
                phiValue.blocks.add(currentBlock);
            }
            if (requestPhi.containsKey(expr.elseDest)) {
                PhiValue phiValue = requestPhi.get(expr.elseDest);
                phiValue.values.add(FALSE);
                phiValue.blocks.add(currentBlock);
            }
        }
    }

    @Override
    public void visit(FuncDeclNode node) {
        FunctionSymbol functionSymbol = node.getFunctionSymbol();

        currentFunction = functionSymbol.function;
        currentBlock = currentFunction.entryBlock;
        assert currentFunction.exitBlock == null;
        if (functionSymbol.isMember()) {
            currentFunction.classPtr = new Register(functionSymbol.getSymbolName() + ".this",
                    root.resolveType(currentClass, false));
            currentFunction.params.add(currentFunction.classPtr);
        }

        visitingParams = true;
        node.getParameterList().forEach(x -> x.accept(this));
        visitingParams = false;

        returns.clear();
        node.getBlock().accept(this);
        if (!currentBlock.isTerminated) {
            Return ret;
            if (currentFunction.returnType instanceof VoidType) {
                ret = new Return(currentBlock, null);
            } else {
                System.err.println("warning: no return statement in function returning non-void : " + currentFunction.name + node.getPosition().toString());
                if (currentFunction.returnType instanceof IntType) {
                    ret = new Return(currentBlock, new ConstInt(0, currentFunction.returnType.size()));
                } else if (currentFunction.returnType instanceof BoolType) {
                    ret = new Return(currentBlock, FALSE);
                } else if (currentFunction.returnType instanceof Pointer) {
                    ret = new Return(currentBlock, new Null());
                } else {
                    throw new UnreachableError();
                }
            }
            currentBlock.pushBack(ret);
            returns.add(ret);
        }

        for (Register var : currentFunction.allocVar) {
            BaseType type = ((Pointer) var.type).typePointedTo;
            /* Dummy for Mem2Reg */
            Operand value = new Undef(type);
            currentFunction.entryBlock.pushFront(new Store(var, value, currentFunction.entryBlock));
            currentFunction.entryBlock.pushFront(new Alloca(var, currentFunction.entryBlock));
        }

        currentFunction.blocks = FunctionBlockCollector.run(currentFunction.entryBlock);

        for (Iterator<Return> iter = returns.iterator(); iter.hasNext();) {
            Return ret = iter.next();
            if (!currentFunction.blocks.contains(ret.basicBlock)) {
                iter.remove();
                ret.basicBlock.removeTerminator();
            }
        }

        if (returns.size() == 1) {
            currentFunction.exitBlock = returns.get(0).basicBlock;
        } else {
            BasicBlock dest = new BasicBlock(currentFunction, "exit_" + functionSymbol.getSymbolName());
            currentFunction.blocks.add(dest);
            Register returnValue = new Register("exit_ret_val", currentFunction.returnType);
            ArrayList<Operand> values = new ArrayList<>();
            ArrayList<BasicBlock> blocks = new ArrayList<>();
            for (Return ret : returns) {
                ret.basicBlock.removeTerminator();
                ret.basicBlock.pushBack(new Jump(dest, ret.basicBlock));
                values.add(ret.value);
                blocks.add(ret.basicBlock);
            }
            if (currentFunction.returnType != VOID) {
                dest.pushBack(new Phi(returnValue, blocks, values, dest));
                dest.pushBack(new Return(dest, returnValue));
            } else {
                dest.pushBack(new Return(dest, null));
            }
            currentFunction.exitBlock = dest;
        }

    }

    @Override
    public void visit(ClassDeclNode node) {
        currentClass = node.getClassSymbol();
        node.getFuncDeclNodes().forEach(x -> x.accept(this));
        /*
        if (currentClass.getConstructor() != null) {
            currentClass.getConstructor().getDefineNode().accept(this);
        }
        // You had done it in Toplevel Scope Building, idiot.
         */
    }

    @Override
    public void visit(BlockStmtNode node) {
        for (StmtNode stmtNode : node.getStmtList()) {
            stmtNode.accept(this);
            if (currentBlock.isTerminated) { // unreachable code after
                return;
            }
        }
    }

    @Override
    public void visit(VarDeclStmtNode node) {
        node.getVarDeclList().getDeclList().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(VarDeclNode node) {
        VariableSymbol symbol = node.getVariableSymbol();
        BaseType type = root.resolveType(symbol.getType(), true);
        if (symbol.isGlobal()) {
            Global var = new Global(new Pointer(type), symbol.getSymbolName());
            symbol.operand = var;
            root.addGlobal(var);
        } else if (visitingParams) {
            Register param = new Register(symbol.getSymbolName(), type);
            currentFunction.params.add(param);
            Register register = new Register("_addr_" + symbol.getSymbolName(), new Pointer(type));
            symbol.operand = register;
            currentFunction.allocVar.add(register);
            currentBlock.pushBack(new Store(register, param, currentBlock));
        } else if (currentFunction != null) {
            Register register = new Register("_addr_" + symbol.getSymbolName(), new Pointer(type));
            if (node.getExpr() != null) {
                insertAssignment(register, node.getExpr());
            }
            currentFunction.allocVar.add(register);
            symbol.operand = register;
        } else if (currentClass != null) {
            if (type instanceof ClassType) {
                type = new Pointer(type);
            }
            symbol.operand = new Register("_addr_" + symbol.getSymbolName(), new Pointer(type));
        } else {
            throw new UnreachableError();
        }
    }

    @Override
    public void visit(ExprStmtNode node) {
        if (node.getExpr().isPureConstant()) {
            System.err.println("warning: result is not used" + node.getPosition());
        } else {
            node.getExpr().accept(this);
        }
    }

    @Override
    public void visit(IfStmtNode node) {
        if (node.getExpr().isPureConstant() && !node.getExpr().equivalentConstant.getBool()) return;
        BasicBlock destBlock = new BasicBlock(currentFunction, "dest_block");
        BasicBlock thenBlock = node.getThenStmt() == null || node.getThenStmt().getStmtList().size() == 0 ? destBlock : new BasicBlock(currentFunction, "then_block");
        BasicBlock elseBlock = node.getElseStmt() == null || node.getElseStmt().getStmtList().size() == 0 ? destBlock : new BasicBlock(currentFunction, "else_block");
        if (thenBlock == elseBlock) return;
        node.getExpr().thenDest = thenBlock;
        node.getExpr().elseDest = elseBlock;
        node.getExpr().accept(this);
        currentBlock = thenBlock;
        node.getThenStmt().accept(this);
        currentBlock.pushBack(new Jump(destBlock, currentBlock));

        if (node.getElseStmt() != null) {
            currentBlock = elseBlock;
            node.getElseStmt().accept(this);
            currentBlock.pushBack(new Jump(destBlock, currentBlock));
        }
        currentBlock = destBlock;
    }

    @Override
    public void visit(WhileStmtNode node) {
        if (node.getExpr().isPureConstant() && !node.getExpr().equivalentConstant.getBool()) return;
        BasicBlock destBlock = new BasicBlock(currentFunction, "while_dest");
        BasicBlock condBlock = new BasicBlock(currentFunction, "while_cond");
        BasicBlock stmtBlock = node.getStmt() == null || node.getStmt().getStmtList().size() == 0 ? condBlock : new BasicBlock(currentFunction, "while_stmt");

        node.destBlock = destBlock;
        node.condBlock = condBlock;

        currentBlock.pushBack(new Jump(condBlock, currentBlock));
        if (node.getExpr() != null) {
            currentBlock = condBlock;
            node.getExpr().thenDest = stmtBlock;
            node.getExpr().elseDest = destBlock;
            node.getExpr().accept(this);
        }
        currentBlock = stmtBlock;
        node.getStmt().accept(this);
        if (!currentBlock.isTerminated) {
            currentBlock.pushBack(new Jump(condBlock, currentBlock));
        }
        currentBlock = destBlock;
    }

    @Override
    public void visit(ForStmtNode node) {
        if (node.getCond() != null && node.getCond().isPureConstant() && !node.getCond().equivalentConstant.getBool()) {
            return;
        }
        BasicBlock stmtBlock = new BasicBlock(currentFunction, "for_stmt");
        BasicBlock condBlock = node.getCond() == null ? stmtBlock : new BasicBlock(currentFunction, "for_cond");
        BasicBlock stepBlock = node.getStep() == null ? condBlock : new BasicBlock(currentFunction, "for_step");
        BasicBlock destBlock = new BasicBlock(currentFunction, "for_dest");

        node.destBlock = destBlock;
        node.stepBlock = stepBlock;

        if (node.getInit() != null) {
            node.getInit().accept(this);
        }

        currentBlock.pushBack(new Jump(condBlock, currentBlock));
        if (node.getCond() != null) {
            currentBlock = condBlock;
            node.getCond().thenDest = stmtBlock;
            node.getCond().elseDest = destBlock;
            node.getCond().accept(this);
        }
        if (node.getStep() != null) {
            currentBlock = stepBlock;
            node.getStep().accept(this);
            if (!currentBlock.isTerminated) {
                currentBlock.pushBack(new Jump(condBlock, currentBlock));
            }
        }
        currentBlock = stmtBlock;
        node.getStmt().accept(this);
        if (!currentBlock.isTerminated) {
            currentBlock.pushBack(new Jump(stepBlock, currentBlock));
        }
        currentBlock = destBlock;
    }

    @Override
    public void visit(ReturnNode node) {
        Return ret;
        if (node.getExpr() == null) {
            ret = new Return(currentBlock, null);
        } else {
            node.getExpr().accept(this);
            Operand value = matchType(node.getExpr().result, currentFunction.returnType);
            ret = new Return(currentBlock, value);
        }
        currentBlock.pushBack(ret);
        returns.add(ret);
    }

    @Override public void visit(BreakNode node) {
        currentBlock.pushBack(new Jump(node.getLoop().getDestBlock(), currentBlock));
    }
    @Override public void visit(ContinueNode node) {
        currentBlock.pushBack(new Jump(node.getLoop().getContinueBlock(), currentBlock));
    }

    @Override
    public void visit(IDExprNode node) {
        VariableSymbol variableSymbol;
        FunctionSymbol functionSymbol;
        switch (node.getTypeCategory()) {
            case FUNCTION:
                functionSymbol = node.getFunctionSymbol();
                if (functionSymbol.isMember()) {
                    node.result = currentFunction.classPtr;
                }
                break;
            case LVALUE: case RVALUE:
                variableSymbol = (VariableSymbol) (node.getVariableSymbol());
                if (variableSymbol.isMember()) { // accessing current function's member variable
                    Register classPtr = currentFunction.classPtr;
                    Register register = new Register("_addr_this." + variableSymbol.getSymbolName(), new Pointer(root.resolveType(variableSymbol.getType(), true)));
                    node.result = register;
                    currentBlock.pushBack(new GEP(((Pointer)classPtr.type).typePointedTo,
                            classPtr, I32ZERO, variableSymbol.index, register, currentBlock));
                } else {
                    node.result = variableSymbol.operand;
                }
                solveBranch(node);
                break;
            /*
                variableSymbol = (VariableSymbol) (node.getVariableSymbol());
                if (variableSymbol.isMember()) { // accessing current function's member variable
                    Register classPtr = currentFunction.classPtr;
                    Register register = new Register("_addr_this." + variableSymbol.getSymbolName(), new Pointer(root.resolveType(variableSymbol.getType(), false)));
                    currentBlock.pushBack(new GEP(((Pointer)classPtr.type).typePointedTo,
                            classPtr, I32ZERO, variableSymbol.index, register, currentBlock));
                    node.result = loadValue(register);
                } else {
                    node.result = loadValue(variableSymbol.operand);
                }
                break;*/
            case CLASS:
                throw new UnreachableError();
        }

    }

    @Override
    public void visit(ArrayIndexNode node) {
        node.getArray().accept(this);
        node.getIndex().accept(this);
        Operand array = matchType(node.getArray().result, root.resolveType(node.getArray().getType(), true));
        Operand index = loadValue(node.getIndex().result);
        Register register = new Register("_array_access_result",
                new Pointer(root.resolveType(node.getType(), true)));
        node.result = register;
        currentBlock.pushBack(new GEP(
                ((Pointer) array.type).typePointedTo,
                array, index,null, register, currentBlock));
        solveBranch(node);
    }

    @Override
    public void visit(BinaryExprNode node) {
        if (node.isPureConstant()) {
            node.equivalentConstant.accept(this);
            node.result = node.equivalentConstant.getResult();
            solveBranch(node);
            return;
        }
        String op = node.getOp();
        Operand lhs, rhs;
        if (op.equals("||") || op.equals("&&")) {
            if (node.hasCondition()) {
                BasicBlock rhsBlock = new BasicBlock(currentFunction, "rhs_block");
                node.getLhs().thenDest = op.equals("||") ? node.thenDest : rhsBlock;
                node.getLhs().elseDest = op.equals("||") ? rhsBlock : node.elseDest;
                node.getLhs().accept(this);
                currentBlock = rhsBlock;
                node.getRhs().thenDest = node.thenDest;
                node.getRhs().elseDest = node.elseDest;
                node.getRhs().accept(this);
            } else {
                BasicBlock condBlock = new BasicBlock(currentFunction, "logicalCondBlock"),
                        destBlock = new BasicBlock(currentFunction, "logicalDestBlock");

                node.result = new Register("logicalResult", BOOL);
                PhiValue phiValue = new PhiValue();
                requestPhi.put(destBlock, phiValue);

                if (op.equals("||")) {
                    node.getLhs().thenDest = destBlock;
                    node.getLhs().elseDest = condBlock;
                } else {
                    node.getLhs().thenDest = condBlock;
                    node.getLhs().elseDest = destBlock;
                }

                node.getLhs().accept(this);

                currentBlock = condBlock;
                node.getRhs().accept(this);
                rhs = loadValue(node.getRhs().result);
                currentBlock.pushBack(new Jump(destBlock, currentBlock));
                phiValue.blocks.add(currentBlock);
                phiValue.values.add(rhs);

                currentBlock = destBlock;
                destBlock.pushBack(new Phi((Register) node.result, phiValue.blocks, phiValue.values, destBlock));
            }
        } else {
            if (op.equals("=")) {
                node.getLhs().accept(this);
                insertAssignment(node.getLhs().result, node.getRhs());
            }
            else if (node.getLhs().isString()) {
                node.getLhs().accept(this);
                node.getRhs().accept(this);
                Function function;
                String instName = Op2Inst.translate(op);
                if (instName.charAt(0) == 's') { // 'signed' icmp
                    instName = instName.substring(1);
                }
                instName = "string_" + instName;
                function = root.getFunction(instName);
                node.result = new Register(instName, op.equals("+") ? STR : BOOL);
                lhs = matchType(node.getLhs().result, STR);
                rhs = matchType(node.getRhs().result, STR);
                Inst inst = new Call(function, new ArrayList<>() {{
                    add(lhs);
                    add(rhs);
                }}, (Register) node.result, currentBlock);
                currentBlock.pushBack(inst);
                currentFunction.callee.add(function);
            } else {
                node.getLhs().accept(this);
                node.getRhs().accept(this);
                Inst inst;
                switch (op) {
                    case "*": case "/": case "%":
                    case "+": case "-":
                    case "<<": case ">>":
                    case "&": case "^": case "|":
                        lhs = loadValue(node.getLhs().result);
                        rhs = loadValue(node.getRhs().result);
                        node.result = new Register("binary_op_" + Op2Inst.translate(op), lhs.type);
                        inst = new Binary(lhs, rhs, (Register) node.result, op, currentBlock);
                        currentBlock.pushBack(inst);
                        break;
                    case "<": case ">":
                    case "<=": case ">=":
                    case "==": case "!=":
                        /*
                        No pointer operations. So, if a pointer comes, possibly:
                        1. It's a int / bool operation, so load it.
                        2. It's a comparision to null, but since no pointer operation, currently we hold the
                        name of the register, say, the pointer of the pointer. So load it.
                        3. It's null. Should not load it, and change it's type when instruction issues.
                        */
                        lhs = node.getLhs().result instanceof Null ? node.getLhs().result : loadValue(node.getLhs().result);
                        rhs = node.getRhs().result instanceof Null ? node.getRhs().result : loadValue(node.getRhs().result);
                        node.result = new Register("cmp_op_" + Op2Inst.translate(op), BOOL);
                        inst = new Cmp(lhs, rhs, (Register) node.result, op, currentBlock);
                        currentBlock.pushBack(inst);
                        break;
                    default:
                        throw new UnreachableError();
                }
            }
            solveBranch(node);
        }
    }

    @Override
    public void visit(UnaryExprNode node) {
        if (node.isPureConstant()) {
            node.equivalentConstant.accept(this);
            node.result = node.equivalentConstant.getResult();
            solveBranch(node);
            return;
        }

        String op = node.getOp();
        node.getExpr().accept(this);

        Operand src;
        switch (op) {
            case "+":
                node.result = node.getExpr().result;
                break;
            case "-":
                src = loadValue(node.getExpr().result);
                node.result = new Register("unary_res", I32);
                currentBlock.pushBack(new Binary(
                        I32ZERO, src,
                        (Register) node.result, "-", currentBlock)
                );
                break;
            case "~":
                src = loadValue(node.getExpr().result);
                node.result = new Register("unary_res", I32);
                currentBlock.pushBack(new Binary(
                        I32NEGONE, src,
                        (Register) node.result, "^", currentBlock)
                );
                break;
            case "!":
                src = loadValue(node.getExpr().result);
                node.result = new Register("unary_res", BOOL);
                currentBlock.pushBack(new Binary(
                        TRUE, src,
                        (Register) node.result, "^", currentBlock)
                );
                break;
            case "++i": case "--i":
                src = loadValue(node.getExpr().result);
                Register ans = new Register("unary_res", I32);
                currentBlock.pushBack(new Binary(src, I32ONE, ans, op.substring(0, 1), currentBlock));
                currentBlock.pushBack(new Store(node.getExpr().result, ans, currentBlock));
                node.result = node.getExpr().result;
                break;
            case "i++": case "i--":
                src = loadValue(node.getExpr().result);
                Register tmp = new Register("unary_tmp", I32);
                currentBlock.pushBack(new Binary(
                        src, I32ONE,
                        tmp, op.substring(1, 2), currentBlock)
                );
                node.result = src;
                currentBlock.pushBack(new Store(node.getExpr().result, tmp, currentBlock));
                break;
            default:
                assert false;
        }
        solveBranch(node);
    }

    @Override
    public void visit(ClassMemberNode node) {
        node.getExpr().accept(this);
        switch (node.getTypeCategory()) {
            case FUNCTION:
                node.result = node.getExpr().result; // this for func call
                break;
            case LVALUE:
            case RVALUE:
                VariableSymbol variableSymbol = (VariableSymbol) (node.getSymbol());
                Operand classPtr = matchType(node.getExpr().result,   root.resolveType(node.getExpr().getType(), true));
                Register res = new Register("_this_" + variableSymbol.getSymbolName(), new Pointer((variableSymbol).operand.type));
                node.result = res;
                currentBlock.pushBack(new GEP(((Pointer) classPtr.type).typePointedTo,
                        classPtr, I32ZERO, variableSymbol.index,
                        res, currentBlock));
                solveBranch(node);
                break;
            case CLASS:
                throw new UnreachableError();
        }
    }

    @Override
    public void visit(FuncCallExprNode node) {
        node.getFunctionNode().accept(this);
        FunctionSymbol symbol = node.getFunctionSymbol();
        if (symbol.getSymbolName().equals("array_size")) {
            Register ret = new Register("array_size", I32);
            node.result = ret;
            assert node.getFunctionNode().result.type instanceof Pointer; // *array
            Register ptr = (Register) loadValue(node.getFunctionNode().result);
            assert ptr.type instanceof Pointer; // array
            Register i32Ptr;
            if (ptr.type.isSame(I32Array)) {
                i32Ptr = ptr;
            } else {
                i32Ptr = new Register("cast_i32_arr", I32Array);
                currentBlock.pushBack(new Cast(ptr, i32Ptr, currentBlock));
            }
            Register sizePtr = new Register("size_addr", new Pointer(I32));
            currentBlock.pushBack(new GEP(I32, i32Ptr, I32NEGONE, null, sizePtr, currentBlock));
            currentBlock.pushBack(new Load(ret, sizePtr, currentBlock));
        } else {
            node.result = symbol.function.returnType.isSame(VOID) ?
                    null : new Register("ret_val", symbol.function.returnType);
            ArrayList <Operand> params = new ArrayList<>();
            int base = 0;
            if (symbol.isMember()) {
                base = 1;
                params.add(matchType(node.getFunctionNode().result, symbol.function.getParamType(0)));
            }
            for (int i = 0; i < node.getParameterList().size(); i++) {
                ExprNode exprNode = node.getParameterList().get(i);
                exprNode.accept(this);
//                System.err.println(String.format("ltype : %s rtype : %s", exprNode.result.type, symbol.function.params.get(base + i).type));
                params.add(matchType(exprNode.result, symbol.function.getParamType(base + i)));
            }
            currentBlock.pushBack(new Call(symbol.function, params, (Register) node.result, currentBlock));
            currentFunction.callee.add(symbol.function);
        }
        solveBranch(node);
    }

    @Override
    public void visit(ThisExprNode node) {
        node.result = currentFunction.classPtr;
    }

    @Override
    public void visit(IntLiteralNode node) {
        node.result = new ConstInt((int)(node.getValue()), 32);
    }
    @Override
    public void visit(BoolLiteralNode node) {
        node.result = node.getValue() ? TRUE : FALSE;
        solveBranch(node);
    }
    @Override
    public void visit(NullLiteralNode node) {
        node.result = new Null();
    }
    @Override
    public void visit(StringLiteralNode node) {
        root.addConstStr(node.getValue());
        ConstStr constStr = root.getConstStr(node.getValue());
        node.result = new Register("str_literal_addr", STR);
        currentBlock.pushBack(new GEP(((Pointer)constStr.type).typePointedTo, constStr, I32ZERO, I32ZERO, (Register) node.result, currentBlock));
    }
    @Override
    public void visit(NewExprNode node) {
        currentFunction.callee.add(Root.builtinMalloc);
        if (node.getDimension() > 0) {
            BaseType type = root.resolveType(node.getBaseTypeAfterResolve(), true);
            for (int i = 0; i < node.getDimension(); i++) {
                type = new Pointer(type);
            }
            Register ret = new Register("new_addr", type);
            node.result = ret;
            newArray(node, 0, ret);
        } else if (node.getBaseTypeAfterResolve() instanceof ClassSymbol){
            Register mallocAddr = new Register("_addr_malloc", STR);
            Register castAddr = new Register("_addr_cast", root.resolveType(node.getBaseTypeAfterResolve(), true));
            node.result = castAddr;
            currentBlock.pushBack(new Malloc(mallocAddr, new ConstInt(((ClassType)((Pointer)castAddr.type).typePointedTo).size / 8, 32), currentBlock));
            currentBlock.pushBack(new Cast(mallocAddr, castAddr, currentBlock));
            if (((ClassSymbol) node.getBaseTypeAfterResolve()).getConstructor() != null) {
                Function constructFunction = ((ClassSymbol) node.getBaseTypeAfterResolve()).getConstructor().function;
                currentBlock.pushBack(new Call(constructFunction, new ArrayList<>() {{ add(castAddr);}}, null, currentBlock));
                currentFunction.callee.add(constructFunction);
            }
        } else {
            throw new UnreachableError();
        }
    }




    void newArray(NewExprNode node, int cur, Register ret) {
    /*
       typeWidth = sizeof type
       dataWidth = curSize * typeWidth
       allocWidth = dataWidth + 4
       mallocAddr = malloc(allocWidth)
       intArrAddr = (int*) mallocAddr;
       *intArrAddr = curSize
       arrayPtr = (type*)(intArrAddr + 1)
       *ret = arrayPtr;

       // if more dimensions required
       int cnt = 0
       while cnt < *intArrAddr:
           intArrAddr[cnt] = new type
           cnt += 1
     */
        if (cur == node.getDimension()) return;
        node.getExprNodeList().get(cur).accept(this);
        Register arrayPtr = cur == 0 ? ret : new Register("malloc_ptr", ((Pointer) ret.type).typePointedTo);
        BaseType typePointTo = ((Pointer) arrayPtr.type).typePointedTo;
        Operand curSize = loadValue(node.getExprNodeList().get(cur).result);
        ConstInt typeWidth = new ConstInt(typePointTo.size() / 8, 32);
        Register dataWidth = new Register("data_width", I32);
        Register allocWidth = new Register("alloc_width", I32);
        Register mallocAddr = new Register("_addr_malloc", STR);
        Register intArrAddr = new Register("_addr_int", I32Array);
        currentBlock.pushBack(new Binary(curSize, typeWidth, dataWidth, "*", currentBlock));
        currentBlock.back.comment = "Binary *";
        currentBlock.pushBack(new Binary(dataWidth, new ConstInt(4, 32), allocWidth, "+", currentBlock));
        currentBlock.back.comment = "Binary +";
        currentBlock.pushBack(new Malloc(mallocAddr, allocWidth, currentBlock));
        currentBlock.back.comment = "Malloc";
        currentBlock.pushBack(new Cast(mallocAddr, intArrAddr, currentBlock));
        currentBlock.back.comment = "Cast";
        currentBlock.pushBack(new Store(intArrAddr, curSize, currentBlock));
        currentBlock.back.comment = "Store";
        if (typePointTo.isSame(I32)) {
            currentBlock.pushBack(new GEP(I32, intArrAddr, I32ONE, null, arrayPtr, currentBlock));
        } else {
            Register intArrAddr_1 = new Register("_addr_int_1", I32Array);
            currentBlock.pushBack(new GEP(I32, intArrAddr, I32ONE, null, intArrAddr_1, currentBlock));
            currentBlock.pushBack(new Cast(intArrAddr_1, arrayPtr, currentBlock));
        }
        if (cur > 0) {
            currentBlock.pushBack(new Store(ret, arrayPtr, currentBlock));
        }
        if (cur < node.getExprNodeList().size() - 1) {
            BasicBlock condBlock = new BasicBlock(currentFunction, "new_cond");
            BasicBlock stmtBlock = new BasicBlock(currentFunction, "new_stmt");
            BasicBlock destBlock = new BasicBlock(currentFunction, "new_dest");
            Register typePtr = new Register("_addr_type", arrayPtr.type);
            Register cnt = new Register("cnt", I32);
            Register cnt_nxt = new Register("cnt_nxt", I32);
            Register cond = new Register("cnt", BOOL);
            ArrayList<Operand> values = new ArrayList<>() {{ add(I32ZERO); }};
            ArrayList<BasicBlock> blocks = new ArrayList<>() {{ add(currentBlock); }};

            currentBlock.pushBack(new Jump(condBlock, currentBlock));

            currentBlock = condBlock;
            currentBlock.pushBack(new Cmp(cnt, curSize, cond,"<", currentBlock));
            currentBlock.pushBack(new Branch(cond, stmtBlock, destBlock, currentBlock));

            currentBlock = stmtBlock;
            currentBlock.pushBack(new GEP(typePointTo, arrayPtr, cnt, null, typePtr, currentBlock));
            newArray(node, cur + 1, typePtr);
            currentBlock.pushBack(new Binary(cnt, I32ONE, cnt_nxt, "+", currentBlock));
            values.add(cnt_nxt);
            blocks.add(currentBlock);
            currentBlock.pushBack(new Jump(condBlock, currentBlock));

            condBlock.pushFront(new Phi(cnt, blocks, values, currentBlock));

            currentBlock = destBlock;
        }

    }

    @Override public void visit(ArrayTypeNode node) {}
    @Override public void visit(ClassTypeNode node) {}
    @Override public void visit(BoolTypeNode node) {}
    @Override public void visit(IntTypeNode node) {}
    @Override public void visit(VoidTypeNode node) {}
    @Override public void visit(StringTypeNode node) {}
}
