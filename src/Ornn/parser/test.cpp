#include <bits/stdc++.h>
using namespace std;


int main() {
	for (int i = 1; i <= 14; i++) {
		string test = "java -Xmx500M -cp \"/usr/local/lib/antlr-4.8-complete.jar:$CLASSPATH\" org.antlr.v4.gui.TestRig Mxstar program < /mnt/c/code/Ornn/Compiler-2020/testcase/sema/class-package/class-" + to_string(i) + ".mx";
		cout << test << endl;
		if (system(test.c_str())) return 1;
	}

}
