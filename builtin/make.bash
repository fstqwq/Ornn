clang-10 -S -emit-llvm --target=riscv32 builtin.c -Ofast
llc-10 --march=riscv32 --mattr=+m builtin.ll -O3
