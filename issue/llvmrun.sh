set -e
llvm-link-10 code.ll builtin/builtin.ll -o a.ll
llc-10 a.ll -o a.s --march=riscv32 --mattr=+m
ravel a.s --keep-debug-info
