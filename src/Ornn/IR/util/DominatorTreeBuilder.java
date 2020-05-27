package Ornn.IR.util;

import Ornn.IR.BasicBlock;
import Ornn.IR.Function;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.min;

// from SJTU Team 1 template
// Refer to our team template : https://github.com/fstqwq/DarkSeal/blob/master/DarkSeal-25/src/TreeandGraph/%E6%94%AF%E9%85%8D%E6%A0%91.cpp
// It's double plus ungood that Java has no reload of [].

public class DominatorTreeBuilder {
    static ArrayList<ArrayList<Integer>> dom;
    static int cnt;
    static ArrayList<BasicBlock> id;
    static HashMap<BasicBlock, Integer> dfn;
    static ArrayList<Integer> sDom, p, mn;
    static HashMap<Integer, Integer> pa;

    static void dfs(BasicBlock x) {
        dfn.put(x, cnt);
        id.add(x);
        p.add(cnt); mn.add(cnt); sDom.add(cnt);
        cnt++;
        for (BasicBlock i : x.successors) {
            if (!dfn.containsKey(i)) {
                dfs(i);
                pa.put(dfn.get(i), dfn.get(x));
            }
        }
    }
    static int getf(int x) {
        if (!p.get(x).equals(p.get(p.get(x)))) {
            if (sDom.get(mn.get(x)) > sDom.get(getf(p.get(x)))) {
                mn.set(x, getf(p.get(x)));
            }
            p.set(x, p.get(p.get(x)));
        }
        return mn.get(x);
    }
    static void LengauerTarjan() {
        for (int i = cnt - 1; i > 0; i--) {
            BasicBlock cur = id.get(i);
            for (BasicBlock j : cur.precursors) {
                sDom.set(i, min(sDom.get(i), sDom.get(getf(dfn.get(j)))));
            }
            dom.get(sDom.get(i)).add(i);
            int x = pa.get(i);
            p.set(i, x);
            BasicBlock xBlock = id.get(x);
            for (Integer j : dom.get(x)) {
                id.get(j).iDom = sDom.get(getf(j)) < x ? id.get(getf(j)) : xBlock;
            }
            dom.get(x).clear();
        }
        for (int i = 1; i < cnt; i++) {
            BasicBlock iBlock = id.get(i);
            if (!iBlock.iDom.equals(id.get(sDom.get(i)))) {
                iBlock.iDom = iBlock.iDom.iDom;
            }
        }
        for (int i = 1; i < cnt; i++) {
            BasicBlock iBlock = id.get(i);
            for (BasicBlock x : iBlock.precursors) {
                while (!x.equals(iBlock.iDom)) {
                    x.domFrontier.add(iBlock);
                    x = x.iDom;
                }
            }
        }
    }
    static public void runForFunction(Function function) {
        BlockGraphUpdater.runForFunction(function);
        function.blocks.forEach(BasicBlock::resetDomInfo);
        BasicBlock entry = function.entryBlock;
        dom = new ArrayList<>();
        dfn = new HashMap<>();
        id = new ArrayList<>();
        sDom = new ArrayList<>();
        p = new ArrayList<>();
        mn = new ArrayList<>();
        pa = new HashMap<>();
        cnt = 0;
        dfs(entry);
        for (int i = 0; i < cnt; i++) dom.add(new ArrayList<>());
/*        for (int i = 0; i < cnt; i++) {
            int finalI = i;
            id.get(i).successors.forEach(x -> System.err.println(finalI + " -> " + dfn.get(x)));
        }*/
        LengauerTarjan();
/*        for (int i = 1; i < cnt; i++) {
            System.err.println(i + " idom " + dfn.get(id.get(i).iDom));
        }*/
    }
}
