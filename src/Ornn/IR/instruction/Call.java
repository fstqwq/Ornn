package Ornn.IR.instruction;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;

import java.util.ArrayList;
import java.util.HashSet;

public class Call extends Inst {
    public Register dest;
    public Function callee;
    public ArrayList<Operand> params;

    public Call(Function callee, ArrayList<Operand> params, Register dest, BasicBlock block) {
        super(block);
        this.callee = callee;
        this.params = params;
        params.forEach(param -> param.uses.add(this));
        if (dest != null) {
            this.dest = dest;
            dest.def = this;
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (dest != null) {
            ret.append(dest.toString()).append(" = call ").append(dest.type.toString()).append(" ");
        } else {
            ret.append("call void ");
        }
        switch (callee.name) {
            case "printf": case "scanf": case "ssacanf":
                ret.append("(i8*, ...) ");
                break;
            default:
                break;
        }
        ret.append(callee.toString());
        if (params.size() != 0) {
            ret.append("(");
            for (int i = 0; i < params.size(); i++) {
                Operand param = params.get(i);
                ret.append(param.type).append(" ").append(param.toString());
                if (i == params.size() - 1) {
                    ret.append(")");
                }
                else {
                    ret.append(", ");
                }
            }
        } else {
            ret.append("()");
        }
        return ret.toString();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public HashSet<Operand> getUses() {
        return new HashSet<>(params);
    }

    @Override
    public Register getDest() {
        return dest;
    }
}