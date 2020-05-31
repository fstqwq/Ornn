package Ornn;

import static java.lang.Integer.min;

public class CompileParameter {
    public static int badSpillLimit = 10;
    public static int forcedInlineInstLimit = 200;
    public static int inlineInstLimit = 750;
    public static int outputInstLimit = 200000;
    public static int CSEInstLimit = 64;
    public static int crossCallPenalty = 0;
    public static int MIRPeepholeMaxGap = 9999;
    public static int[] unrollParams = {64, 8, 2, 1};
    public static boolean checkLoopUnroll(int loopDepth, int loopLength) {
        return loopLength <= unrollParams[min(loopDepth, unrollParams.length - 1)];
    }
    public static void setLowConfidence() {
        forcedInlineInstLimit = 120;
        inlineInstLimit = 600;
        CSEInstLimit = 32;
        MIRPeepholeMaxGap = 64;
        crossCallPenalty = 12;
        unrollParams[0] = 32;
    }
}
