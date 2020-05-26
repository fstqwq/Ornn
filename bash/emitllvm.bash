set -e
cd "$(dirname "$0")"
export CCHK="java -classpath /ulib/java/antlr-4.8-complete.jar:./bin Ornn.Main -emit-llvm"
cat > code.mx
$CCHK
cat code.ll

