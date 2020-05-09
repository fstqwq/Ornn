# Ornn
 > Mxstar compiler for MS208
>
![](https://universe-meeps.leagueoflegends.com/v1/assets/images/ornn-splash.jpg)


![](https://opgg-static.akamaized.net/images/lol/item/2420.png?image=q_auto,w_42&v=1586932751)
![](https://opgg-static.akamaized.net/images/lol/item/1054.png?image=q_auto,w_42&v=1583298869)
![](https://opgg-static.akamaized.net/images/lol/item/3802.png?image=q_auto,w_42&v=1588915771)
![](https://opgg-static.akamaized.net/images/lol/item/3373.png?image=q_auto,w_42&v=1583298869)
![](https://opgg-static.akamaized.net/images/lol/item/3111.png?image=q_auto,w_42&v=1583298869)
![](https://opgg-static.akamaized.net/images/lol/item/3379.png?image=q_auto,w_42&v=1583298869)

List of things I shall done:

![](https://opgg-static.akamaized.net/images/lol/item/2031.png?image=q_auto,w_42&v=1583298869)
 Init
 
![](https://opgg-static.akamaized.net/images/lol/item/1054.png?image=q_auto,w_42&v=1583298869)
 Parser
 * with help of ANTLR4

![](https://opgg-static.akamaized.net/images/lol/item/3373.png?image=q_auto,w_42&v=1583298869)
AST

![](https://opgg-static.akamaized.net/images/lol/item/3111.png?image=q_auto,w_42&v=1583298869)
Sematic Checker
* Structural design mainly refer to [Daedalus of Bohan Hou](https://github.com/spectrometerHBH/Daedalus), which is quite elegant.

![](https://opgg-static.akamaized.net/images/lol/item/3379.png?image=q_auto,w_42&v=1583298869)
IR Builder
* Build LLVM IR. Able to compile using llc-10.
    * >  If you are toying with some language ideas, using LLVM will be like hauling your backpack with a truck. --[QBE](http://c9x.me/compile/doc/llvm.html)
    
     *(But yes, if you choose to drive a truck, you don't have to learn how to maintain balance while riding a bicycle)*

* Not so good implementation of pointer loading.
    
   * Well, a better solution is to keep track the type of lvalue and rvalue to insert load. If time permit I will rewrite IRBuilder.

![](https://opgg-static.akamaized.net/images/lol/item/3386.png?image=q_auto,w_42&v=1583298869) Optim

* HIR Optimization
    * Constant Folding
        * Redundant with MIR, but it's quite easy to implement when I am not ready to write backend optimizations.

    * Print Reconstruct
        * Boring but highly effective optimization to the bad implementation of string

(TODO)
* MIR Optimization
    * Mem2Reg
        * Actually it's also SSA construction
    * (Aggressive) Dead Code Eliminate
    * CFG Simplify
    * Sparse Conditional Constant Propagation
    * Common subexpression elimination
* Consider to do
    * Function inlining (Where to do?)
    * constexpr
        
![](https://opgg-static.akamaized.net/images/lol/item/3390.png?image=q_auto,w_42&v=1583298869) Codegen

![](https://opgg-static.akamaized.net/images/lol/item/3374.png?image=q_auto,w_42&v=1583298869) Black magic