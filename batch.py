#!python3

import os


test_cases_dir = './Compiler-2020/testcase/codegen/'
compile_cmd = "bash ./build.bash"
execute_cmd = "bash ./codegen.bash"
excluded_test_cases = ["foo.mx"]
ravel_path = "ravel"
builtin_path = "./builtin/builtin.s"

# When use_llvm is true, the output should be a .ll file, and we will use llc to
# compile it into asm. You can test the correctness of your IR-gen with this.
use_llvm = False
llc_cmd = 'llc-10'


color_red = "\033[0;31m"
color_green = "\033[0;32m"
color_none = "\033[0m"


def collect_test_cases():
    test_cases = []
    for f in os.listdir(test_cases_dir):
        if os.path.splitext(f)[1] == '.mx':
            test_cases.append(f)
    for s in excluded_test_cases:
        if s in test_cases: test_cases.remove(s)
    test_cases.sort()
    return test_cases


def parse_test_case(test_case_path):
    with open(test_case_path, 'r') as f:
        lines = f.read().split('\n')
    src_start_idx = lines.index('*/', lines.index('/*')) + 1
    src_text = '\n'.join(lines[src_start_idx:])

    input_start_idx = lines.index('=== input ===') + 1
    input_end_idx = lines.index('=== end ===', input_start_idx)
    input_text = '\n'.join(lines[input_start_idx:input_end_idx])

    output_start_idx = lines.index('=== output ===') + 1
    output_end_idx = lines.index('=== end ===', output_start_idx)
    output_text = '\n'.join(lines[output_start_idx:output_end_idx])

    return src_text, input_text, output_text


def main():
    if os.system(compile_cmd):
        print(color_red + "Fail when building your compiler...")
        return
    test_cases = collect_test_cases()
    os.system('cp %s ./builtin.s' % builtin_path)
    total = 0
    passed = 0
    continue_fail = 0
    for t in test_cases:
        if continue_fail > 2:
            exit(1)
        total += 1
        src_text, input_text, output_text = parse_test_case(test_cases_dir + t)
        with open('test.mx', 'w') as f:
            f.write(src_text)
        with open('test.in', 'w') as f:
            f.write(input_text)
        with open('test.ans', 'w') as f:
            f.write(output_text)

        print(t + ':', end=' ')
        if os.system('%s < ./test.mx > test.s' % execute_cmd):
            print(color_red + "Compilation failed" + color_none)
            continue_fail += 1
            continue
        if use_llvm:
            os.system('mv ./test.s ./test.ll')
            os.system(llc_cmd + ' --march=riscv32 -mattr=+m -o test.s test.ll')

        if os.system('%s --oj-mode < test.in 1>ravel.out 2>/dev/null'
                     % ravel_path):
            print(color_red + "Runtime error" + color_none)
            continue
        if os.system('diff -B -b test.out test.ans > diff.out'):
            print(color_red + "Wrong answer" + color_none)
            continue
        passed += 1
        continue_fail = 0

        ravel_out = open("ravel.out")
        time_field = ""
        while True:
            s = ravel_out.readline()
            if s.count("time") > 0:
                time_field = s[5:-1]
                break
        print(color_green + "Accepted" + color_none + time_field)
    print("total {}, passed {}, ratio {}".format(total, passed, passed / total))

if __name__ == '__main__':
    main()
