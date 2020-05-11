package Ornn.backend;

import Ornn.IR.*;
import Ornn.IR.instruction.*;
import Ornn.IR.operand.*;
import Ornn.IR.type.Pointer;
import Ornn.RISCV.*;
import Ornn.RISCV.instrution.*;
import Ornn.RISCV.operand.*;
import Ornn.util.UnreachableError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static Ornn.RISCV.instrution.RVInst.BCategory;
import static Ornn.RISCV.instrution.RVInst.SCategory;
import static Ornn.RISCV.instrution.RVInst.SzCategory;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

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

    HashMap<Register, Reg> tmpAlias = new HashMap<>();

    public Reg regTrans(Operand operand) {
        if (operand instanceof Register) {
            Reg ret;
            if (operandMap.containsKey(operand)) {
                ret = operandMap.get(operand);
            } else if (tmpAlias.containsKey(operand)) {
                ret = tmpAlias.get(operand);
            } else {
                ret = new VReg(vRegCount++, operand.type.size() / 8);
                operandMap.put(operand, ret);
            }
            return ret;
        }
        if (operand instanceof ConstStr || operand instanceof Global) {
            Reg ret;
            if (!operandMap.containsKey(operand)) {
                GReg reg = new GReg("." + operand.name, ((Pointer) operand.type).typePointedTo.size() / 8);
                if (operand instanceof Global) {
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
                VReg reg = new VReg(vRegCount++, operand.type.size() / 8);
                currentBlock.add(new Li(1, reg, currentBlock));
                return reg;
            } else {
                return zero;
            }
        }
        if (operand instanceof ConstInt) {
            if (((ConstInt) operand).value != 0) {
                VReg reg = new VReg(vRegCount++, operand.type.size() / 8);
                currentBlock.add(new Li(((ConstInt) operand).value, reg, currentBlock));
                return reg;
            } else {
                return zero;
            }
        }
        if (operand instanceof Null) {
            return zero;
        }
        throw new UnreachableError();
    }

    boolean checkImm(int x) {
        return minImm <= x && x <= maxImm;
    }

    void makeBinary(Operand src1, Operand src2, SCategory op, Reg rd) {

    }
    void makeCmp(Operand src1, Operand src2, SCategory op, Reg rd) {
        if (src2 instanceof ConstInt && checkImm(((ConstInt) src2).value)) {
            currentBlock.add(new IType(regTrans(src1), new Imm(((ConstInt) src2).value), op, rd, currentBlock));
        } else {
            currentBlock.add(new RType(regTrans(src1), regTrans(src2), op, rd, currentBlock));
        }
    }
    void makeCmpz(Operand src, SzCategory op, Reg rd) {
        currentBlock.add(new Sz(regTrans(src), op, rd, currentBlock));
    }
    @Override
    public void visit(Binary inst) {

    }
    @Override
    public void visit(GEP inst) {

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
                if (inst.src1 instanceof ConstInt) {
                    makeCmp(inst.src2, inst.src1, SCategory.sgt, regTrans(inst.dest));
                } else {
                    makeCmp(inst.src1, inst.src2, SCategory.slt, regTrans(inst.dest));
                }
                break;
            case ">":
                if (inst.src1 instanceof ConstInt) {
                    makeCmp(inst.src2, inst.src1, SCategory.slt, regTrans(inst.dest));
                } else {
                    makeCmp(inst.src1, inst.src2, SCategory.sgt, regTrans(inst.dest));
                }
                break;
            case "<=":
                tmp = new VReg(vRegCount++, 4);
                if (inst.src1 instanceof ConstInt) {
                    makeCmp(inst.src2, inst.src1, SCategory.slt, tmp);
                } else {
                    makeCmp(inst.src1, inst.src2, SCategory.sgt, tmp);
                }
                currentBlock.add(new IType(tmp, new Imm(1), SCategory.xor, regTrans(inst.dest), currentBlock));
                break;
            case ">=":
                tmp = new VReg(vRegCount++, 4);
                if (inst.src1 instanceof ConstInt) {
                    makeCmp(inst.src2, inst.src1, SCategory.sgt, tmp);
                } else {
                    makeCmp(inst.src1, inst.src2, SCategory.slt, tmp);
                }
                currentBlock.add(new IType(tmp, new Imm(1), SCategory.xor, regTrans(inst.dest), currentBlock));
                break;
            case "==":
                if ((inst.src1 instanceof ConstInt && ((ConstInt) inst.src1).value == 0)
                ||  (inst.src1 instanceof ConstBool && !((ConstBool) inst.src1).value)
                ||  (inst.src1 instanceof Null)) {
                    makeCmpz(inst.src2, SzCategory.seqz, regTrans(inst.dest));
                } else
                if ((inst.src2 instanceof ConstInt && ((ConstInt) inst.src2).value == 0)
                        ||  (inst.src2 instanceof ConstBool && !((ConstBool) inst.src2).value)
                        ||  (inst.src2 instanceof Null)) {
                    makeCmpz(inst.src1, SzCategory.seqz, regTrans(inst.dest));
                } else {
                    tmp = new VReg(vRegCount++, 4);
                    makeBinary(inst.src1, inst.src2, SCategory.xor, tmp);
                    currentBlock.add(new Sz(tmp, SzCategory.seqz, regTrans(inst.dest), currentBlock));
                }
                break;
            case "!=":
                if ((inst.src1 instanceof ConstInt && ((ConstInt) inst.src1).value == 0)
                        ||  (inst.src1 instanceof ConstBool && !((ConstBool) inst.src1).value)
                        ||  (inst.src1 instanceof Null)) {
                    makeCmpz(inst.src2, SzCategory.snez, regTrans(inst.dest));
                } else
                if ((inst.src2 instanceof ConstInt && ((ConstInt) inst.src2).value == 0)
                        ||  (inst.src2 instanceof ConstBool && !((ConstBool) inst.src2).value)
                        ||  (inst.src2 instanceof Null)) {
                    makeCmpz(inst.src1, SzCategory.snez, regTrans(inst.dest));
                } else {
                    tmp = new VReg(vRegCount++, 4);
                    makeBinary(inst.src1, inst.src2, SCategory.xor, tmp);
                    currentBlock.add(new Sz(tmp, SzCategory.snez, regTrans(inst.dest), currentBlock));
                }
                break;
            default:
                throw new UnreachableError();
        }
    }
    @Override
    public void visit(Branch inst) {
        if (inst.cond instanceof Register
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
                    throw new UnreachableError();
            }
            currentBlock.add(new Br(regTrans(cmp.src1), regTrans(cmp.src2), op, blockMap.get(inst.thenDest), currentBlock));
        } else {
            currentBlock.add(new Br(regTrans(inst.cond), zero, BCategory.ne, blockMap.get(inst.thenDest), currentBlock));
        }
        currentBlock.add(new Jmp(blockMap.get(inst.elseDest), currentBlock));
    }
    @Override
    public void visit(Call inst) {
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
        if (inst.dest != null) {
            currentBlock.add(new Mv(ra, regTrans(inst.dest), currentBlock));
        }
    }
    @Override
    public void visit(Cast inst) {
        /*  notice that: inst.dest must be a fucking useless tmp reg, so just fuck it  */
        tmpAlias.put(inst.dest, regTrans(inst.src));
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
        if (inst.src instanceof ConstInt) {
            currentBlock.add(new Li(((ConstInt) inst.src).value, regTrans(inst.dest), currentBlock));
        } else if (inst.src instanceof ConstBool) {
            currentBlock.add(new Li(((ConstBool) inst.src).value ? 1 : 0, regTrans(inst.dest), currentBlock));
        } else {
            currentBlock.add(new Mv(regTrans(inst.src), regTrans(inst.dest), currentBlock));
        } // not possible to be const strings or globals
    }
    @Override
    public void visit(Return inst) {
        if (inst.value instanceof ConstInt) {
            currentBlock.add(new Li(((ConstInt) inst.value).value, a.get(0), currentBlock));
        } else if (inst.value instanceof ConstBool) {
            currentBlock.add(new Li(((ConstBool) inst.value).value ? 1 : 0, a.get(0), currentBlock));
        } else if (inst.value != null) {
            currentBlock.add(new Mv(regTrans(inst.value), a.get(0), currentBlock));
        } // not possible to be const strings or globals
    }
    @Override
    public void visit(Store inst) {
        Reg addr = regTrans(inst.addr);
        if (addr instanceof GReg) {
            Reg reg = regTrans(inst.value);
            VReg ptr = new VReg(vRegCount++, 4);
            currentBlock.add(new Lui(new Relocation((GReg) addr, Relocation.RCategory.hi), ptr, currentBlock));
            currentBlock.add(new St(reg, new Relocation((GReg) addr, Relocation.RCategory.lo), ptr, inst.value.type.size() / 8, currentBlock));
        }
    }

    void runForBlock(BasicBlock irBlock) {
        currentBlock = blockMap.get(irBlock);
        for (BasicBlock successor : irBlock.successors) {
            currentBlock.precursors.add(blockMap.get(successor));
        }
        for (BasicBlock successor : irBlock.successors) {
            currentBlock.successors.add(blockMap.get(successor));
        }
        for (Inst inst = irBlock.front; inst != null; inst = inst.next) {
            inst.accept(this);
        }
    }

    void runForFunction(Function irFunc) {
        RVFunction function = functionMap.get(irFunc);
        currentFunction = function;
        vRegCount = 0;
        tmpAlias.clear();
        SImm stackFrame = new SImm(0, true);
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
                    irFunc.params.get(i).type.size() / 8,
                    function.entryBlock));
            paramInStackOffset += 4;
        }
        for (BasicBlock block : irFunc.blocks) {
            runForBlock(block);
        }
        for (int i = 0; i < calleeVRegs.size(); i++) {
            function.exitBlock.add(new Mv(calleeVRegs.get(i), rvRoot.calleeSavedRegs.get(i), function.exitBlock));
        }
        function.exitBlock.add(new Mv(vRa, ra, function.exitBlock));
        function.exitBlock.add(new IType(sp, new SImm(0, false), SCategory.add, sp, function.exitBlock));
        function.exitBlock.add(new Ret(function.exitBlock));
        function.vRegCount = vRegCount;
    }

    public RVRoot run() {
        root.builtinFunctions.forEach(((s, function) -> {
            RVFunction func = new RVFunction(s);
            rvRoot.builtinFunctions.add(func);
            functionMap.put(function, func);
        }));
        root.functions.forEach((s, function) -> {
            for (BasicBlock block : function.blocks) {
                RVBlock rvBlock = new RVBlock("." + function.name + "_" + block.name);
                blockMap.put(block, rvBlock);
            }
            RVFunction rvFunction = new RVFunction(s);
            rvFunction.entryBlock = blockMap.get(function.entryBlock);
            rvFunction.exitBlock = blockMap.get(function.exitBlock);
            function.params.forEach(param -> rvFunction.params.add(regTrans(param)));
            rvRoot.functions.add(rvFunction);
        });
        root.functions.forEach(((s, function) -> runForFunction(function)));
        return rvRoot;
    }
}
