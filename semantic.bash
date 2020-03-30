set -e
cd "$(dirname "$0")"
export CCHK="java -classpath ./bin Ornn.Main"
# cat > program.txt   # save everything in stdin to program.txt
$CCHK