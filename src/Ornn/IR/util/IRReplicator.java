package Ornn.IR.util;

import Ornn.IR.BasicBlock;
import Ornn.IR.operand.Operand;
import Ornn.IR.operand.Register;
import Ornn.util.UnreachableCodeError;

import java.util.HashMap;

public class IRReplicator {
    public HashMap <Operand, Operand> operandMap = new HashMap<>();
    public HashMap <BasicBlock, BasicBlock> blockMap = new HashMap<>();
    public Operand get(Operand from) {
        if (!operandMap.containsKey(from)) {
            operandMap.put(from, from.getCopy());
        }
        return operandMap.get(from);
    }
    public Register get(Register from) {
        return (Register) get((Operand) from);
    }
    public BasicBlock get(BasicBlock from) {
        if (blockMap.containsKey(from)) {
            return blockMap.get(from);
        }
        throw new UnreachableCodeError();
    }
    public void put(Operand from, Operand to) {
        operandMap.put(from, to);
    }
    public void put(BasicBlock from, BasicBlock to) {
        blockMap.put(from, to);
    }
    public void clear() {
        operandMap = new HashMap<>();
        blockMap = new HashMap<>();
    }
}
