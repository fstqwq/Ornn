package Ornn.RISCV;

import java.util.Set;

public class TargetInfo {
    public static final String[] regNames = {"zero","ra","sp","gp","tp","t0","t1","t2","s0","s1","a0","a1","a2","a3","a4","a5","a6","a7","s2","s3","s4","s5","s6","s7","s8","s9","s10","s11","t3","t4","t5","t6" };
    public static final String[] calleeSavedRegNames = {"s0","s1","s2","s3","s4","s5","s6","s7","s8","s9","s10","s11"};
    public static final String[] argumentRegNames = {"a0","a1","a2","a3","a4","a5","a6","a7"};
    public static final Set<String> callerSavedRegNames = Set.of("ra","t0","t1","t2","a0","a1","a2","a3","a4","a5","a6","a7","t3","t4","t5","t6" );
    public static final String[] allocatableRegNames = {"a0","a1","a2","a3","a4","a5","a6","a7","t0","t1","t2","s0","s1","s2","s3","s4","s5","s6","s7","s8","s9","s10","s11","t3","t4","t5","t6", "ra"};
}
