# Ornn

> Dark smoke exhales, from the mountain half\
  And wind blows through hollow dells\
  Skies clear as day, shall soon turn gray\
  And you'll know, you're nearing Ornn
> 
> Barring your path is a chasm wide\
  Howls rise from fathomless pits\
  But close stands a bridge, frozen by time\
  And across, you'll soon find Ornn
> 
> River of fire, that scorches the earth\
  Belies his kingdom of stone\
  And steel sings its tone, as a god stands alone\
  The shaping hand, we know as Ornn
>
> Sparks leap and fly from the star-fallen ore\
  Forging his works, divine\
  Bellows erupt, with unbridled force\
  No longer lost - the Mountainsmith, Ornn 

![](https://universe-meeps.leagueoflegends.com/v1/assets/images/ornn-splash.jpg)

![](http://fstqwq.pw/wp-content/uploads/2020/05/darkseal10-e1590666353790.png)
![](https://opgg-static.akamaized.net/images/lol/item/3386.png?image=q_auto,w_40&v=1583298869)
![](https://opgg-static.akamaized.net/images/lol/item/3390.png?image=q_auto,w_40&v=1583298869)
![](https://opgg-static.akamaized.net/images/lol/item/3373.png?image=q_auto,w_40&v=1583298869)
![](https://opgg-static.akamaized.net/images/lol/item/3111.png?image=q_auto,w_40&v=1583298869)
![](https://opgg-static.akamaized.net/images/lol/item/3379.png?image=q_auto,w_40&v=1583298869)

### Mxstar compiler for MS208

**Warning:** 
There should be so many bugs and unreasonable designs that at last I have to add redundant code to ensure correctness.


![](https://opgg-static.akamaized.net/images/lol/item/1054.png?image=q_auto,w_42&v=1583298869)
 Parser
 * with help of ANTLR4

![](https://opgg-static.akamaized.net/images/lol/item/3373.png?image=q_auto,w_42&v=1583298869) ![](https://opgg-static.akamaized.net/images/lol/item/3111.png?image=q_auto,w_42&v=1583298869)
AST & Semantic Checker
* Structural design mainly refer to [Daedalus of Bohan Hou](https://github.com/spectrometerHBH/Daedalus), which is quite elegant.

![](https://opgg-static.akamaized.net/images/lol/item/3379.png?image=q_auto,w_42&v=1583298869)
IR Builder
* Build LLVM IR. Able to compile using llc-10.
    * >  If you are toying with some language ideas, using LLVM will be like hauling your backpack with a truck. --[QBE](http://c9x.me/compile/doc/llvm.html)
    
     *(But yes, if you choose to drive a truck, you don't have to learn how to maintain balance while riding a bicycle)*

* Not so good implementation of pointer loading.
    
   * Well, a better solution is to keep track the type of lvalue and rvalue to insert load. ~~If time permit I will rewrite IRBuilder.~~ Let it be.

![](https://opgg-static.akamaized.net/images/lol/item/3386.png?image=q_auto,w_42&v=1583298869) Optim

* HIR Optimization
    * Constant Folding
        * Redundant with MIR, but it's quite easy to implement when I am not ready to write backend optimizations.
        * Also, it will help calculate "constant" usage of globals, and improve the performance of other two HIR optimizations.

    * Print Reconstruction
        * Boring but highly effective optimization to the bad implementation of string.

    * Static Arrays Detection
        * It's not standard implementation, but a trick to those global arrays whose pointers are only modified when initializing.
        Switch to C-style array for efficiency.

* MIR Optimization
    * Mem2Reg
        * Actually it's SSA construction, naming followed LLVM IR.
    * Function inlining
    * Tail call implementation
        * Mark all tail-callable calls and do tail calls at LIR stage. Highly effective for recursions!
    * Constant (Small) Loop Unrolling
    * Dead Code Elimination
    * CFG Simplification
    * Constant Propagation
    * Common subexpression elimination
    * MIR Peephole
        > Is alias analysis THAT important? In fact if you use alias analysis, in many times the only answer is may alias.
        * Kill redundant load stores. Mainly locally, but able to access idom block if it is the only entry (often conditions)
            * For globals, they must not have alias, so delete all loads except the first and all stores except the last.
            * For pointers, take all pointers of same type as may alias, and delete useless L/S.
    * Global variable localization in dead end functions
        * It was inspired by my participation in Huawei Software *~~(Constant Optimization)~~* Elite Challenge
        * Brainless inlining globals shows negative sometimes, like you've got lot to call in current function. So I designed a threshold condition to do it heuristically.
    
* LIR Optimization
    * Register Allocation
        * Graph coloring, refer to Tiger Book.
        * It's so unstable that, when I write some optimization that must be positive in a general sense, reg alloc drew it back. 
    * Reschedule
        * Simply put blocks in a dfs-order.
        * Unconditional jump only cost 1 in our scoring, so maybe not that useful to do some predictions.
    * Peephole
        * Remove redundant moves.
        * Remove redundant load immediate numbers, which is invisible in MIR.
        
![](https://opgg-static.akamaized.net/images/lol/item/3390.png?image=q_auto,w_42&v=1583298869) Codegen

![](http://fstqwq.pw/wp-content/uploads/2020/05/darkseal10-e1590666353790.png) Surpass O2 on test data
* "Over-fitting" and some constant tricks

![](https://opgg-static.akamaized.net/images/lol/item/3374.png?image=q_auto,w_42&v=1583298869) Black magic
* Time's up, baby.