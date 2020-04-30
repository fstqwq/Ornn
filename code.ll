declare i8* @string.le(i8* %0, i8* %1)declare i8* @string.ne(i8* %0, i8* %1)declare i8* @string.substring(i8* %0, i8* %1, i8* %2)declare i8* @getString()declare i8* @string.ge(i8* %0, i8* %1)declare i8* @string.add(i8* %0, i8* %1)declare i32 @string.ord(i8* %0, i32 %1)declare i32 @getInt()declare i8* @string.lt(i8* %0, i8* %1)declare void @print(i8* %0)declare void @println(i32 %0)declare i32 @string.parseInt(i8* %0)declare void @printInt(i32 %0)declare void @printlnInt(i32 %0)declare i8* @toString(i32 %0)declare i32 @string.length(i8* %0)declare i8* @string.gt(i8* %0, i8* %1)declare i8* @string.eq(i8* %0, i8* %1)%struct.ARRAY = type {}
%struct.array = type {}
%struct.Array = type {i32}
define i32 @ARRAY.size() {
0:
	ret 1919
}define i32 @size() {
0:
	ret 114
}define i32 @__init() {
0:
	ret 0
}define i32 @main() {
0:
	%1 = mul i32 514i32 4
	%2 = add i32 %1i32 4
	%3 = call i8* @malloc(i32 %2)
	%4 = bitcast i8* %3 to i32
	store i32, 514, i32 %4, align 4
	%5 = getelementptr inbounds i32, i32 %4, i32 1
	%6 = mul i32 514i32 4
	%7 = add i32 %6i32 4
	%8 = call i8* @malloc(i32 %7)
	%9 = bitcast i8* %8 to i32
	store i32, 514, i32 %9, align 4
	%10 = getelementptr inbounds i32, i32 %9, i32 1
	store i32*, %10, i32** %_addr_a, align 4
	%11 = call i8* @malloc(i32 0)
	%12 = bitcast i8* %11 to %struct.ARRAY*
	%13 = call i8* @malloc(i32 0)
	%14 = bitcast i8* %13 to %struct.ARRAY*
	store %struct.ARRAY*, %14, %struct.ARRAY** %_addr_b, align 4
	%15 = call i8* @malloc(i32 0)
	%16 = bitcast i8* %15 to %struct.array*
	%17 = call i8* @malloc(i32 0)
	%18 = bitcast i8* %17 to %struct.array*
	store %struct.array*, %18, %struct.array** %_addr_c, align 4
	%19 = call i8* @malloc(i32 4)
	%20 = bitcast i8* %19 to %struct.Array*
	@Array.Array(%struct.Array* %20)
	%21 = call i8* @malloc(i32 4)
	%22 = bitcast i8* %21 to %struct.Array*
	@Array.Array(%struct.Array* %22)
	store %struct.Array*, %22, %struct.Array** %_addr_d, align 4
	@size()
	@toString(i32 %ret_val)
	%23 = load i8*, i8** %ret_val, align 4
	@print(i8* %23)
	%24 = load i32*, i32** %_addr_a, align 4
	%25 = bitcast i32* %24 to i32
	%26 = getelementptr inbounds i32, i32 %25, i32 -1
	%27 = load i32, i32* %26, align 4
	@toString(i32 %27)
	%28 = load i8*, i8** %ret_val, align 4
	@println(i8* %28)
	%29 = load %struct.ARRAY*, %struct.ARRAY** %_addr_b, align 4
	@ARRAY.size(%struct.ARRAY* %29)
	@toString(i32 %ret_val)
	%30 = load i8*, i8** %ret_val, align 4
	@print(i8* %30)
	%31 = load %struct.array*, %struct.array** %_addr_c, align 4
	@array.size(%struct.array* %31)
	@toString(i32 %ret_val)
	%32 = load i8*, i8** %ret_val, align 4
	@print(i8* %32)
	%33 = load %struct.Array*, %struct.Array** %_addr_d, align 4
	@Array.size(%struct.Array* %33)
	@toString(i32 %ret_val)
	%34 = load i8*, i8** %ret_val, align 4
	@println(i8* %34)
	ret 0
}define i32 @Array.size() {
0:
	%1 = getelementptr inbounds %struct.Array*, %struct.Array** %size.this, i32 0, i32 0
	%2 = load i32, i32* %1, align 4
	ret %2
}define i32 @array.size() {
0:
	ret 8
}define void @Array.Array() {
0:
	%1 = getelementptr inbounds %struct.Array*, %struct.Array** %Array.this, i32 0, i32 0
	store i32, 10, i32* %1, align 4
	ret void
	%2 = getelementptr inbounds %struct.Array*, %struct.Array** %Array.this, i32 0, i32 0
	store i32, 10, i32* %2, align 4
}