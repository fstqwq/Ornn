package Ornn.backend;

import Ornn.IR.*;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.*;
import Ornn.IR.type.BaseType;
import Ornn.IR.type.ClassType;
import Ornn.IR.type.Pointer;
import Ornn.RISCV.*;
import Ornn.RISCV.instrution.*;
import Ornn.RISCV.operand.*;
import Ornn.util.UnreachableCodeError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static Ornn.RISCV.instrution.RVInst.BCategory;
import static Ornn.RISCV.instrution.RVInst.SCategory;
import static Ornn.RISCV.instrution.RVInst.SzCategory;
import static java.lang.Integer.*;

/*
Consider a function f calling a function g:
    int g(int x, int y);
    int f(int x, int y) {g(y, x);}

In stack:
                    f.y
    sp of f.caller  f.x
                    <Register stores>
                    <Empty for longer params>
                    g.x
    sp of f->       g.y

 */

public class InstSelector implements IRVisitor {
    public static int paramRegNum = TargetInfo.argumentRegNames.length;
    public static int minImm = -(1 << 11), maxImm = (1 << 11) - 1;
    Root root;
    RVRoot rvRoot = new RVRoot();
    PReg zero, sp, ra;
    ArrayList<PReg> a = new ArrayList<>();
    HashMap<Function, RVFunction> functionMap = new LinkedHashMap<>();
    HashMap<BasicBlock, RVBlock> blockMap = new LinkedHashMap<>();
    HashMap<Operand, Reg> operandMap = new LinkedHashMap<>();
    int vRegCount = 0;
    public InstSelector(Root root) {
        this.root = root;
        zero = rvRoot.regMap.get("zero");
        sp = rvRoot.regMap.get("sp");
        ra = rvRoot.regMap.get("ra");
        for (int i = 0; i < paramRegNum; i++) {
            a.add(rvRoot.regMap.get("a" + i));
        }
    }

    RVBlock currentBlock;
    RVFunction currentFunction;

    Reg regTrans(Operand operand) {
        if (operand instanceof Register) {
            Reg ret;
            if (operandMap.containsKey(operand)) {
                ret = operandMap.get(operand);
            } else {
                ret = new VReg(vRegCount++, max(1, operand.type.size() / 8));
                operandMap.put(operand, ret);
            }
            return ret;
        }
        if (operand instanceof ConstStr || operand instanceof Global) {
            Reg ret;
            if (!operandMap.containsKey(operand)) {
                GReg reg = new GReg(operand.name, ((Pointer) operand.type).typePointedTo.size() / 8);
                if (operand instanceof Global) {
                    if (((Global) operand).isArray) {
                        reg.isArray = ((Global) operand).isArray;
                        reg.size = ((Global) operand).arraySize;
                        reg.initialization = ((Global) operand).initialization;
                    }
                    rvRoot.global.add(reg);
                } else {
                    rvRoot.constStr.put(reg, ((ConstStr) operand).value);
                }
                operandMap.put(operand, reg);
                ret = reg;
            } else {
                ret = operandMap.get(operand);
            }
            return ret;
        }
        if (operand instanceof ConstBool) {
            if (((ConstBool) operand).value) {
                VReg reg = new VReg(vRegCount++, 1);
                currentBlock.add(new Li(1, reg, currentBlock));
                return reg;
            } else {
                return zero;
            }
        }
        if (operand instanceof ConstInt) {
            if (((ConstInt) operand).value != 0) {
                VReg reg = new VReg(vRegCount++, 4);
                currentBlock.add(new Li(((ConstInt) operand).value, reg, currentBlock));
                return reg;
            } else {
                return zero;
            }
        }
        if (!(operand instanceof Null)) {
            System.err.println("warning : undefined");
        }
        return zero;
    }

    boolean checkImm(int x) {
        return minImm <= x && x <= maxImm;
    }

    boolean isZero(Operand operand) {
        return
                (operand instanceof ConstInt && ((ConstInt) operand).value == 0)
                        ||  (operand instanceof ConstBool && !((ConstBool) operand).value)
                        ||  (operand instanceof Null)
                        ||  (operand instanceof Undef);
    }

    boolean checkOperandConst(Operand operand) {
        return isZero(operand) ||operand instanceof ConstInt;
    }

    int getOperandConst(Operand operand) {
        return isZero(operand) ? 0 : ((ConstInt) operand).value;
    }

    boolean isPowerOfTwo(int x) {
        return (x & (x - 1)) == 0;
    }

    boolean checkOperandImm(Operand operand) {
        if (isZero(operand)) return true;
        else return operand instanceof ConstInt && checkImm(((ConstInt) operand).value);
    }

    boolean checkNegOperandImm(Operand operand) {
        if (isZero(operand)) return true;
        else return operand instanceof ConstInt && checkImm(-((ConstInt) operand).value);
    }

    Imm getImm(Operand operand) {
        if (isZero(operand)) return new Imm(0);
        else if (operand instanceof ConstInt) return new Imm(((ConstInt) operand).value);
        else throw new UnreachableCodeError();
    }
    Imm getNegImm(Operand operand) {
        if (isZero(operand)) return new Imm(0);
        else if (operand instanceof ConstInt) return new Imm(-((ConstInt) operand).value);
        else throw new UnreachableCodeError();
    }

    void makeBinary(Operand src1, Operand src2, String op, Reg rd) {
        SCategory sop;
        boolean abelian = false, iType = false;
        switch (op) {
            case "+": sop = SCategory.add; abelian = true; iType = true; break;
            case "-": sop = SCategory.sub; iType = true; break;
            case "*": sop = SCategory.mul; break;
            case "/": sop = SCategory.div; break;
            case "%": sop = SCategory.rem; break;
            case "^": sop = SCategory.xor; abelian = true; iType = true; break;
            case "&": sop = SCategory.and; abelian = true; iType = true; break;
            case "|": sop = SCategory.or; abelian = true; iType = true; break;
            case "<<": sop = SCategory.sll; iType = true; break;
            case ">>": sop = SCategory.sra; iType = true; break;
            default: throw new UnreachableCodeError();
        }
        if (isZero(src2)) {
            if (sop == SCategory.and || sop == SCategory.mul) {
                currentBlock.add(new Mv(zero, rd, currentBlock));
            } else {
                currentBlock.add(new Mv(regTrans(src1), rd, currentBlock));
            }
        } else if (isZero(src1)) {
            if (sop == SCategory.add || sop == SCategory.sub || sop == SCategory.or || sop == SCategory.xor) {
                currentBlock.add(new RType(zero, regTrans(src2), sop, rd, currentBlock));
            } else {
                currentBlock.add(new Mv(zero, rd, currentBlock));
            }
        } else if (iType) {
            if (sop.equals(SCategory.sub) && checkNegOperandImm(src2)) {
                currentBlock.add(new IType(regTrans(src1), getNegImm(src2), SCategory.add, rd, currentBlock));
            } else if (!sop.equals(SCategory.sub) && checkOperandImm(src2)) {
                currentBlock.add(new IType(regTrans(src1), getImm(src2), sop, rd, currentBlock));
            } else if (abelian && checkOperandImm(src1)) {
                currentBlock.add(new IType(regTrans(src2), getImm(src1), sop, rd, currentBlock));
            } else {
                currentBlock.add(new RType(regTrans(src1), regTrans(src2), sop, rd, currentBlock));
            }
        } else {
            if (sop == SCategory.mul && checkOperandConst(src1) && isPowerOfTwo(getOperandConst(src1))) {
                currentBlock.add(new IType(regTrans(src2), new Imm(Integer.numberOfTrailingZeros(getOperandConst(src1))), SCategory.sll, rd, currentBlock));
            } else if (sop == SCategory.mul && checkOperandConst(src2) && isPowerOfTwo(getOperandConst(src2))) {
                currentBlock.add(new IType(regTrans(src1), new Imm(Integer.numberOfTrailingZeros(getOperandConst(src2))), SCategory.sll, rd, currentBlock));
            } else if (sop == SCategory.div && checkOperandConst(src2) && isPowerOfTwo(getOperandConst(src2))) { // wrong when negative, assumed undefined
                currentBlock.add(new IType(regTrans(src1), new Imm(Integer.numberOfTrailingZeros(getOperandConst(src2))), SCategory.sra, rd, currentBlock));
            } else if (sop == SCategory.rem && checkOperandConst(src2) && isPowerOfTwo(getOperandConst(src2))) { // wrong when negative, assumed undefined
                currentBlock.add(new IType(regTrans(src1), new Imm((1 << Integer.numberOfTrailingZeros(getOperandConst(src2))) - 1), SCategory.and, rd, currentBlock));
            } else {
                currentBlock.add(new RType(regTrans(src1), regTrans(src2), sop, rd, currentBlock));
            }
        }
    }
    void makeCmp(Operand src1, Operand src2, SCategory op, Reg rd) {
        if (checkOperandImm(src2)) {
            currentBlock.add(new IType(regTrans(src1), getImm(src2), op, rd, currentBlock));
        } else {
            currentBlock.add(new RType(regTrans(src1), regTrans(src2), op, rd, currentBlock));
        }
    }
    void makeCmpz(Operand src, SzCategory op, Reg rd) {
        currentBlock.add(new Sz(regTrans(src), op, rd, currentBlock));
    }
    @Override
    public void visit(Binary inst) {
        makeBinary(inst.src1, inst.src2, inst.op, regTrans(inst.dest));
    }
    @Override
    public void visit(GEP inst) {
        Reg dest;
        BaseType type = inst.type;
        if (inst.arrayOffset instanceof ConstInt) {
            int index = ((ConstInt) inst.arrayOffset).value;
            if (index != 0) {
                dest = new VReg(vRegCount++, 4);
                makeBinary(inst.ptr, new ConstInt(index * (type.size() / 8), 32), "+", dest);
            } else {
                Reg ptr = regTrans(inst.ptr);
                if (ptr instanceof GReg) {
                    dest = new VReg(vRegCount++, 4);
                    currentBlock.add(new La((GReg) ptr, dest, currentBlock));
                    if (rvRoot.constStr.containsKey(ptr)) {
                        currentBlock.back.comment = "\"" + rvRoot.constStr.get(ptr) + "\"";
                    }
                } else {
                    dest = ptr;
                }
            }
        } else {
            VReg tmp = new VReg(vRegCount++, 4);
            dest = new VReg(vRegCount++, 4);
            makeBinary(inst.arrayOffset, new ConstInt(type.size() / 8, 4), "*", tmp);
            currentBlock.add(new RType(regTrans(inst.ptr), tmp, SCategory.add, dest, currentBlock));
        }
        if (!(inst.elementOffset == null || inst.elementOffset.value == 0)) {
            int offset = ((ClassType) ((Pointer) inst.ptr.type).typePointedTo).offsets.get(inst.elementOffset.value).value / 8;
            Reg newDest = new VReg(vRegCount++, 4);
            if (checkImm(offset)) {
                currentBlock.add(new IType(dest, new Imm(offset), SCategory.add, newDest, currentBlock));
            } else {
                currentBlock.add(new RType(dest, regTrans(new ConstInt(offset, 32)), SCategory.add, newDest, currentBlock));
            }
            dest = newDest;
        }
        currentBlock.add(new Mv(dest, regTrans(inst.dest), currentBlock));
    }
    @Override
    public void visit(Cmp inst) {
        if (inst.dest.uses.size() == 1
        &&  inst.dest.uses.iterator().next().isTerminal()) {
            /* Branch will handle it */
            return;
        }
        VReg tmp;
        switch (inst.op) {
            case "<":
                makeCmp(inst.src1, inst.src2, SCategory.slt, regTrans(inst.dest));
                break;
            case ">":
                makeCmp(inst.src2, inst.src1, SCategory.slt, regTrans(inst.dest));
                break;
            case "<=":
                tmp = new VReg(vRegCount++, 4);
                makeCmp(inst.src2, inst.src1, SCategory.slt, tmp);
                currentBlock.add(new IType(tmp, new Imm(1), SCategory.xor, regTrans(inst.dest), currentBlock));
                break;
            case ">=":
                tmp = new VReg(vRegCount++, 4);
                makeCmp(inst.src1, inst.src2, SCategory.slt, tmp);
                currentBlock.add(new IType(tmp, new Imm(1), SCategory.xor, regTrans(inst.dest), currentBlock));
                break;
            case "==":
                if (isZero(inst.src1)) {
                    makeCmpz(inst.src2, SzCategory.seqz, regTrans(inst.dest));
                } else if (isZero(inst.src2)) {
                    makeCmpz(inst.src1, SzCategory.seqz, regTrans(inst.dest));
                } else {
                    tmp = new VReg(vRegCount++, 4);
                    makeBinary(inst.src1, inst.src2, "^", tmp);
                    currentBlock.add(new Sz(tmp, SzCategory.seqz, regTrans(inst.dest), currentBlock));
                }
                break;
            case "!=":
                if (isZero(inst.src1)) {
                    makeCmpz(inst.src2, SzCategory.snez, regTrans(inst.dest));
                } else if (isZero(inst.src2)) {
                    makeCmpz(inst.src1, SzCategory.snez, regTrans(inst.dest));
                } else {
                    tmp = new VReg(vRegCount++, 4);
                    makeBinary(inst.src1, inst.src2, "^", tmp);
                    currentBlock.add(new Sz(tmp, SzCategory.snez, regTrans(inst.dest), currentBlock));
                }
                break;
            default:
                throw new UnreachableCodeError();
        }
    }
    @Override
    public void visit(Branch inst) {
        if (inst.thenDest.equals(inst.elseDest)) { // should be deprecated after CFG Simplify
            System.err.println("simplify branch");
            currentBlock.add(new Jmp(blockMap.get(inst.thenDest), currentBlock));
            return;
        }
        else if (inst.cond instanceof ConstBool) { // should be deprecated after CFG Simplify
            System.err.println("simplify branch");
            RVBlock to = ((ConstBool) inst.cond).value ? blockMap.get(inst.thenDest) : blockMap.get(inst.elseDest);
            RVBlock notTo = !((ConstBool) inst.cond).value ? blockMap.get(inst.thenDest) : blockMap.get(inst.elseDest);
            currentBlock.add(new Jmp(to, currentBlock));
            currentBlock.successors.remove(notTo);
            notTo.precursors.remove(currentBlock);
            return;
        } else if (inst.cond instanceof Register
        &&  inst.cond.uses.size() == 1
        &&  inst.cond.uses.iterator().next().isTerminal()
        &&  ((Register) inst.cond).def instanceof Cmp) {
            Cmp cmp = (Cmp) ((Register) inst.cond).def;
            BCategory op;
            switch (cmp.op) {
                case "<":
                    op = BCategory.lt;
                    break;
                case ">":
                    op = BCategory.gt;
                    break;
                case "<=":
                    op = BCategory.le;
                    break;
                case ">=":
                    op = BCategory.ge;
                    break;
                case "==":
                    op = BCategory.eq;
                    break;
                case "!=":
                    op = BCategory.ne;
                    break;
                default:
                    throw new UnreachableCodeError();
            }
            currentBlock.add(new Br(regTrans(cmp.src1), regTrans(cmp.src2), op, blockMap.get(inst.thenDest), currentBlock));
        } else {
            currentBlock.add(new Br(regTrans(inst.cond), zero, BCategory.ne, blockMap.get(inst.thenDest), currentBlock));
        }
        currentBlock.add(new Jmp(blockMap.get(inst.elseDest), currentBlock));
    }
    @Override
    public void visit(Call inst) {
        if (inst.tailCallable) {
            // do tail call
            System.err.println("tail call : " + inst.callee.name);
            ArrayList<VReg> tmpParams = new ArrayList<>();
            for (int i = 0; i < inst.params.size(); i++) {
                tmpParams.add(new VReg(vRegCount++, 4));
            }
            for (int i = 0; i < inst.params.size(); i++) {
                Reg reg = regTrans(inst.params.get(i));
                if (reg instanceof GReg) {
                    currentBlock.add(new La((GReg) reg, tmpParams.get(i), currentBlock));
                } else {
                    currentBlock.add(new Mv(reg, tmpParams.get(i), currentBlock));
                }
            }
            for (int i = 0; i < inst.params.size(); i++) {
                currentBlock.add(new Mv(tmpParams.get(i), currentFunction.params.get(i), currentBlock));
            }
            currentBlock.add(new Jmp(currentFunction.tailCallEntryBlock, currentBlock));
            currentBlock.successors.add(currentFunction.tailCallEntryBlock);
            currentFunction.tailCallEntryBlock.precursors.add(currentBlock);
            return;
        }
        for (int i = 0; i < min(paramRegNum, inst.params.size()); i++) {
            Reg reg = regTrans(inst.params.get(i));
            if (reg instanceof GReg) {
                currentBlock.add(new La((GReg) reg, a.get(i), currentBlock));
            } else {
                currentBlock.add(new Mv(reg, a.get(i), currentBlock));
            }
        }
        int paramInStackOffset = 0;
        for (int i = paramRegNum; i < inst.params.size(); i++) {
            Operand param = inst.params.get(i);
            currentBlock.add(new St(sp, new Imm(paramInStackOffset), regTrans(param), param.type.size() / 8, currentBlock));
            paramInStackOffset += 4;
        }
        currentFunction.paramInStackOffset = max(currentFunction.paramInStackOffset, paramInStackOffset);
        currentBlock.add(new Cl(rvRoot, functionMap.get(inst.callee), currentBlock));
        currentBlock.back.comment = inst.toString();
        if (inst.dest != null) {
            currentBlock.add(new Mv(a.get(0), regTrans(inst.dest), currentBlock));
        }
    }
    @Override
    public void visit(Cast inst) {
        currentBlock.add(new Mv(regTrans(inst.src), regTrans(inst.dest), currentBlock));
    }
    @Override
    public void visit(Jump inst) {
        currentBlock.add(new Jmp(blockMap.get(inst.dest), currentBlock));
    }
    @Override
    public void visit(Load inst) {
        currentBlock.add(new Ld(regTrans(inst.addr), new Imm(0), regTrans(inst.dest), inst.dest.type.size() / 8, currentBlock));
    }
    @Override
    public void visit(Malloc inst) {
        currentBlock.add(new Mv(regTrans(inst.size), a.get(0), currentBlock));
        currentBlock.add(new Cl(rvRoot, functionMap.get(root.getFunction("malloc")),currentBlock));
        currentBlock.add(new Mv(a.get(0), regTrans(inst.dest), currentBlock));
    }
    @Override
    public void visit(Move inst) {
        if (isZero(inst.src)) {
            currentBlock.add(new Mv(zero, regTrans(inst.dest), currentBlock));
        } else if (inst.src instanceof ConstInt) {
            currentBlock.add(new Li(((ConstInt) inst.src).value, regTrans(inst.dest), currentBlock));
        } else if (inst.src instanceof ConstBool) {
            currentBlock.add(new Li(((ConstBool) inst.src).value ? 1 : 0, regTrans(inst.dest), currentBlock));
        } else {
            currentBlock.add(new Mv(regTrans(inst.src), regTrans(inst.dest), currentBlock));
        } // not possible to be const strings or globals
    }
    @Override
    public void visit(Return inst) {
        if (inst.value != null) {
            if (isZero(inst.value)) {
                currentBlock.add(new Mv(zero, a.get(0), currentBlock));
            } else if (inst.value instanceof ConstInt) {
                currentBlock.add(new Li(((ConstInt) inst.value).value, a.get(0), currentBlock));
            } else if (inst.value instanceof ConstBool) {
                currentBlock.add(new Li(((ConstBool) inst.value).value ? 1 : 0, a.get(0), currentBlock));
            } else {
                currentBlock.add(new Mv(regTrans(inst.value), a.get(0), currentBlock));
            }
        }
    }
    @Override
    public void visit(Store inst) {
        Reg addr = regTrans(inst.addr);
        if (addr instanceof GReg) {
            Reg reg = regTrans(inst.value);
            VReg ptr = new VReg(vRegCount++, 4);
            currentBlock.add(new Lui(new Reloc((GReg) addr, Reloc.RCategory.hi), ptr, currentBlock));
            currentBlock.add(new St(ptr, new Reloc((GReg) addr, Reloc.RCategory.lo), reg, inst.value.type.size() / 8, currentBlock));
        } else {
            currentBlock.add(new St(addr, new Imm(0), regTrans(inst.value), inst.value.type.size() / 8, currentBlock));
        }
    }

    void runForBlock(BasicBlock irBlock) {
        currentBlock = blockMap.get(irBlock);
        for (BasicBlock precursor : irBlock.precursors) {
            currentBlock.precursors.add(blockMap.get(precursor));
        }
        for (BasicBlock successor : irBlock.successors) {
            currentBlock.successors.add(blockMap.get(successor));
        }
        for (Inst inst = irBlock.front; inst != null; inst = inst.next) {
            inst.accept(this);
            if (currentBlock.back instanceof Jmp || currentBlock.back instanceof Ret) {
                break;
            }
        }
    }

    void runForFunction(Function irFunc) {
        RVFunction function = functionMap.get(irFunc);
        currentFunction = function;
        vRegCount = function.vRegCount;
        SImm stackFrame = new SImm(0, true);

        function.tailCallEntryBlock = function.entryBlock;

        function.entryBlock = new RVBlock("." + irFunc.name + "_entry");

        function.entryBlock.add(new IType(sp, stackFrame, SCategory.add, sp, function.entryBlock));

        ArrayList<VReg> calleeVRegs = new ArrayList<>();
        rvRoot.calleeSavedRegs.forEach(pReg -> {
            VReg reg = new VReg(vRegCount++, 4);
            function.entryBlock.add(new Mv(pReg, reg, function.entryBlock));
            calleeVRegs.add(reg);
        });

        VReg vRa = new VReg(vRegCount++, 4);
        function.entryBlock.add(new Mv(ra, vRa, function.entryBlock));
        for (int i = 0; i < min(paramRegNum, irFunc.params.size()); i++) {
            function.entryBlock.add(new Mv(a.get(i), function.params.get(i), function.entryBlock));
        }
        int paramInStackOffset = 0;
        for (int i = paramRegNum; i < irFunc.params.size(); i++) {
            function.entryBlock.add(new Ld(sp,
                    new SImm(paramInStackOffset, false),
                    function.params.get(i),
                    max(1, irFunc.params.get(i).type.size() / 8),
                    function.entryBlock));
            paramInStackOffset += 4;
        }

        function.entryBlock.add(new Jmp(function.tailCallEntryBlock, function.entryBlock));
        function.entryBlock.successors.add(function.tailCallEntryBlock);
        function.tailCallEntryBlock.precursors.add(function.entryBlock);
        function.blocks.add(function.entryBlock);

        for (BasicBlock block : irFunc.blocks) {
            runForBlock(block);
        }
/*
        for (RVBlock block : function.blocks) {
            block.successors.forEach(x -> System.err.println(block + " suc -> " + x));
            block.successors.forEach(x -> System.err.println(x + " pre -> " + block));
        }
*/
        for (int i = 0; i < calleeVRegs.size(); i++) {
            function.exitBlock.add(new Mv(calleeVRegs.get(i), rvRoot.calleeSavedRegs.get(i), function.exitBlock));
        }
        function.exitBlock.add(new Mv(vRa, ra, function.exitBlock));
        function.exitBlock.add(new IType(sp, new SImm(0, false), SCategory.add, sp, function.exitBlock));
        function.exitBlock.add(new Ret(rvRoot, function.exitBlock));
        function.vRegCount = vRegCount;
    }

    public RVRoot run() {
        root.builtinFunctions.forEach(((s, function) -> {
            RVFunction func = new RVFunction(s);
            rvRoot.builtinFunctions.add(func);
            functionMap.put(function, func);
        }));
        root.proxyStatics.forEach(global -> {
            GReg reg = new GReg(global.name, global.arraySize);
            reg.isArray = global.isArray;
            reg.initialization = global.initialization;
            rvRoot.global.add(reg);
            operandMap.put(global, reg);
        });
        root.functions.forEach((s, function) -> {
            RVFunction rvFunction = new RVFunction(s);
            functionMap.put(function, rvFunction);
            for (BasicBlock block : function.blocks) {
                RVBlock rvBlock = new RVBlock("." + function.name + "_" + block.name);
                blockMap.put(block, rvBlock);
                rvFunction.blocks.add(rvBlock);
            }
            rvFunction.entryBlock = blockMap.get(function.entryBlock);
            rvFunction.exitBlock = blockMap.get(function.exitBlock);
            vRegCount = 0;
            function.params.forEach(param -> rvFunction.params.add(regTrans(param)));
            rvFunction.vRegCount = vRegCount;
            rvRoot.functions.add(rvFunction);
        });
        root.functions.forEach(((s, function) -> runForFunction(function)));
        return rvRoot;
    }
}
