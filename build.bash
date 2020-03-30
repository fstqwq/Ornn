set -e
cd "$(dirname "$0")"
mkdir -p bin
find ./src -name *.java | javac -d bin @/dev/stdin