declare i8* @malloc(i32 %0)
declare i32 @puts(i8* %0)
declare void @print(i8* %0)
declare i8* @string_add(i8* %0, i8* %1)
@c = global i8**  zeroinitializer, align 4
@co = global i8*  zeroinitializer, align 4
@a2q = global i8*  zeroinitializer, align 4
@a2b = global i8*  zeroinitializer, align 4
@.str.0 = private unnamed_addr constant [2 x i8] c"0\00", align 1
@.str.1 = private unnamed_addr constant [2 x i8] c"1\00", align 1
@.str.2 = private unnamed_addr constant [2 x i8] c"2\00", align 1
@.str.3 = private unnamed_addr constant [2 x i8] c"3\00", align 1
@.str.4 = private unnamed_addr constant [2 x i8] c"4\00", align 1
@.str.5 = private unnamed_addr constant [2 x i8] c"5\00", align 1
@.str.6 = private unnamed_addr constant [2 x i8] c"6\00", align 1
@.str.7 = private unnamed_addr constant [2 x i8] c"7\00", align 1
@.str.8 = private unnamed_addr constant [2 x i8] c"8\00", align 1
@.str.9 = private unnamed_addr constant [2 x i8] c"9\00", align 1
@.str.10 = private unnamed_addr constant [3 x i8] c"c[\00", align 1
@.str.11 = private unnamed_addr constant [3 x i8] c"]=\00", align 1
@.str.12 = private unnamed_addr constant [2 x i8] c" \00", align 1
@.str.13 = private unnamed_addr constant [2 x i8] c"!\00", align 1
@.str.14 = private unnamed_addr constant [2 x i8] c"#\00", align 1
@.str.15 = private unnamed_addr constant [2 x i8] c"$\00", align 1
@.str.16 = private unnamed_addr constant [2 x i8] c"%\00", align 1
@.str.17 = private unnamed_addr constant [2 x i8] c"&\00", align 1
@.str.18 = private unnamed_addr constant [2 x i8] c"'\00", align 1
@.str.19 = private unnamed_addr constant [2 x i8] c"(\00", align 1
@.str.20 = private unnamed_addr constant [2 x i8] c")\00", align 1
@.str.21 = private unnamed_addr constant [2 x i8] c"*\00", align 1
@.str.22 = private unnamed_addr constant [2 x i8] c"+\00", align 1
@.str.23 = private unnamed_addr constant [2 x i8] c";\00", align 1
@.str.24 = private unnamed_addr constant [2 x i8] c"\22\00", align 1
@.str.25 = private unnamed_addr constant [2 x i8] c"\5C\00", align 1
define i8* @digt(i32 %0) {
1:
	%2 = alloca i32, align 4
	store i32 %0, i32* %2, align 4
	%3 = load i32, i32* %2, align 4
	%4 = icmp eq i32 %3, 0
	br i1 %4, label %5, label %7
5:
	%6 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.0, i32 0, i32 0
	br label %53
7:
	%8 = load i32, i32* %2, align 4
	%9 = icmp eq i32 %8, 1
	br i1 %9, label %10, label %12
10:
	%11 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.1, i32 0, i32 0
	br label %53
12:
	%13 = load i32, i32* %2, align 4
	%14 = icmp eq i32 %13, 2
	br i1 %14, label %15, label %17
15:
	%16 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.2, i32 0, i32 0
	br label %53
17:
	%18 = load i32, i32* %2, align 4
	%19 = icmp eq i32 %18, 3
	br i1 %19, label %20, label %22
20:
	%21 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.3, i32 0, i32 0
	br label %53
22:
	%23 = load i32, i32* %2, align 4
	%24 = icmp eq i32 %23, 4
	br i1 %24, label %25, label %27
25:
	%26 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.4, i32 0, i32 0
	br label %53
27:
	%28 = load i32, i32* %2, align 4
	%29 = icmp eq i32 %28, 5
	br i1 %29, label %30, label %32
30:
	%31 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.5, i32 0, i32 0
	br label %53
32:
	%33 = load i32, i32* %2, align 4
	%34 = icmp eq i32 %33, 6
	br i1 %34, label %35, label %37
35:
	%36 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.6, i32 0, i32 0
	br label %53
37:
	%38 = load i32, i32* %2, align 4
	%39 = icmp eq i32 %38, 7
	br i1 %39, label %40, label %42
40:
	%41 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.7, i32 0, i32 0
	br label %53
42:
	%43 = load i32, i32* %2, align 4
	%44 = icmp eq i32 %43, 8
	br i1 %44, label %45, label %47
45:
	%46 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.8, i32 0, i32 0
	br label %53
47:
	%48 = load i32, i32* %2, align 4
	%49 = icmp eq i32 %48, 9
	br i1 %49, label %50, label %52
50:
	%51 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.9, i32 0, i32 0
	br label %53
52:
	br label %53
53:
	%54 = phi i8* [ %6, %5 ], [ %11, %10 ], [ %16, %15 ], [ %21, %20 ], [ %26, %25 ], [ %31, %30 ], [ %36, %35 ], [ %41, %40 ], [ %46, %45 ], [ %51, %50 ], [ null, %52 ]
	ret i8* %54
}
define i8* @c2(i32 %0) {
1:
	%2 = alloca i32, align 4
	store i32 %0, i32* %2, align 4
	%3 = load i32, i32* %2, align 4
	%4 = icmp sle i32 %3, 9
	br i1 %4, label %5, label %12
5:
	%6 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.10, i32 0, i32 0
	%7 = load i32, i32* %2, align 4
	%8 = call i8* @digt(i32 %7)
	%9 = call i8* @string_add(i8* %6, i8* %8)
	%10 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.11, i32 0, i32 0
	%11 = call i8* @string_add(i8* %9, i8* %10)
	br label %24
12:
	%13 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.10, i32 0, i32 0
	%14 = load i32, i32* %2, align 4
	%15 = sdiv i32 %14, 10
	%16 = call i8* @digt(i32 %15)
	%17 = call i8* @string_add(i8* %13, i8* %16)
	%18 = load i32, i32* %2, align 4
	%19 = srem i32 %18, 10
	%20 = call i8* @digt(i32 %19)
	%21 = call i8* @string_add(i8* %17, i8* %20)
	%22 = getelementptr inbounds [3 x i8], [3 x i8]* @.str.11, i32 0, i32 0
	%23 = call i8* @string_add(i8* %21, i8* %22)
	br label %24
24:
	%25 = phi i8* [ %11, %5 ], [ %23, %12 ]
	ret i8* %25
}
define i32 @main() {
0:
	%1 = alloca i32, align 4
	call void @__init()
	store i32 0, i32* %1, align 4
	%2 = load i8**, i8*** @c, align 4
	%3 = getelementptr inbounds i8*, i8** %2, i32 0
	%4 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.12, i32 0, i32 0
	store i8* %4, i8** %3, align 4
	%5 = load i8**, i8*** @c, align 4
	%6 = getelementptr inbounds i8*, i8** %5, i32 1
	%7 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.13, i32 0, i32 0
	store i8* %7, i8** %6, align 4
	%8 = load i8**, i8*** @c, align 4
	%9 = getelementptr inbounds i8*, i8** %8, i32 2
	%10 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.14, i32 0, i32 0
	store i8* %10, i8** %9, align 4
	%11 = load i8**, i8*** @c, align 4
	%12 = getelementptr inbounds i8*, i8** %11, i32 3
	%13 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.15, i32 0, i32 0
	store i8* %13, i8** %12, align 4
	%14 = load i8**, i8*** @c, align 4
	%15 = getelementptr inbounds i8*, i8** %14, i32 4
	%16 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.16, i32 0, i32 0
	store i8* %16, i8** %15, align 4
	%17 = load i8**, i8*** @c, align 4
	%18 = getelementptr inbounds i8*, i8** %17, i32 5
	%19 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.17, i32 0, i32 0
	store i8* %19, i8** %18, align 4
	%20 = load i8**, i8*** @c, align 4
	%21 = getelementptr inbounds i8*, i8** %20, i32 6
	%22 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.18, i32 0, i32 0
	store i8* %22, i8** %21, align 4
	%23 = load i8**, i8*** @c, align 4
	%24 = getelementptr inbounds i8*, i8** %23, i32 7
	%25 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.19, i32 0, i32 0
	store i8* %25, i8** %24, align 4
	%26 = load i8**, i8*** @c, align 4
	%27 = getelementptr inbounds i8*, i8** %26, i32 8
	%28 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.20, i32 0, i32 0
	store i8* %28, i8** %27, align 4
	%29 = load i8**, i8*** @c, align 4
	%30 = getelementptr inbounds i8*, i8** %29, i32 9
	%31 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.21, i32 0, i32 0
	store i8* %31, i8** %30, align 4
	%32 = load i8**, i8*** @c, align 4
	%33 = getelementptr inbounds i8*, i8** %32, i32 10
	%34 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.22, i32 0, i32 0
	store i8* %34, i8** %33, align 4
	store i32 0, i32* %1, align 4
	br label %35
35:
	%36 = load i32, i32* %1, align 4
	%37 = icmp slt i32 %36, 10
	br i1 %37, label %38, label %52
38:
	%39 = load i32, i32* %1, align 4
	%40 = call i8* @c2(i32 %39)
	call void @print(i8* %40)
	%41 = load i8*, i8** @a2q, align 4
	call void @print(i8* %41)
	%42 = load i8**, i8*** @c, align 4
	%43 = load i32, i32* %1, align 4
	%44 = getelementptr inbounds i8*, i8** %42, i32 %43
	%45 = load i8*, i8** %44, align 4
	call void @print(i8* %45)
	%46 = load i8*, i8** @a2q, align 4
	call void @print(i8* %46)
	%47 = load i8*, i8** @co, align 4
	%48 = call i32 @puts(i8* %47)
	br label %49
49:
	%50 = load i32, i32* %1, align 4
	%51 = add i32 %50, 1
	store i32 %51, i32* %1, align 4
	br label %35
52:
	ret i32 0
}
define void @__init() {
0:
	%1 = mul i32 256, 4			; Binary *
	%2 = add i32 %1, 4			; Binary +
	%3 = call i8* @malloc(i32 %2)			; Malloc
	%4 = bitcast i8* %3 to i32*			; Cast
	store i32 256, i32* %4, align 4			; Store
	%5 = getelementptr inbounds i32, i32* %4, i32 1
	%6 = bitcast i32* %5 to i8**
	store i8** %6, i8*** @c, align 4
	%7 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.23, i32 0, i32 0
	store i8* %7, i8** @co, align 4
	%8 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.24, i32 0, i32 0
	store i8* %8, i8** @a2q, align 4
	%9 = getelementptr inbounds [2 x i8], [2 x i8]* @.str.25, i32 0, i32 0
	store i8* %9, i8** @a2b, align 4
	ret void
}
