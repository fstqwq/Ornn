package Ornn.RISCV;

import Ornn.RISCV.operand.*;

import java.util.*;

import static Ornn.RISCV.TargetInfo.*;

public class RVRoot {
    public HashSet<RVFunction> functions = new HashSet<>();
    public HashSet<RVFunction> builtinFunctions = new HashSet<>();
    public ArrayList<PReg> pRegs = new ArrayList<>();
    public LinkedHashMap<String, PReg> regMap = new LinkedHashMap<>();
    public ArrayList<PReg> calleeSavedRegs = new ArrayList<>();
    public ArrayList<PReg> callerSavedRegs = new ArrayList<>();
    public ArrayList<PReg> argumentRegs = new ArrayList<>();
    public ArrayList<PReg> allocatableRegs = new ArrayList<>();
    public HashMap<GReg, String> constStr = new HashMap<>();
    public HashSet<GReg> global = new HashSet<>();
    public int spilledCount;
    public RVRoot() {
        for (String reg : regNames) {
            PReg pReg = new PReg(reg);
            pRegs.add(pReg);
            regMap.put(reg, pReg);
        }
        for (String reg : calleeSavedRegNames) {
            calleeSavedRegs.add(regMap.get(reg));
        }
        for (String reg : callerSavedRegNames) {
            callerSavedRegs.add(regMap.get(reg));
        }
        for (String reg : argumentRegNames) {
            argumentRegs.add(regMap.get(reg));
        }
        for (String reg : allocatableRegNames) {
            allocatableRegs.add(regMap.get(reg));
        }
    }
}
