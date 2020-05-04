clang-10 -S -emit-llvm -Xclang -disable-O0-optnone builtin.c -O3
llc-10 --march=riscv32 --mattr=+m builtin.ll -O3
