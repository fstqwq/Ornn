	.text
	.file	"builtin.c"
	.globl	print                   # -- Begin function print
	.p2align	2
	.type	print,@function
print:                                  # @print
# %bb.0:
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
                                        # -- End function
	.globl	printInt                # -- Begin function printInt
	.p2align	2
	.type	printInt,@function
printInt:                               # @printInt
# %bb.0:
	lui	a1, %hi(.L.str.1)
	addi	a1, a1, %lo(.L.str.1)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end1:
	.size	printInt, .Lfunc_end1-printInt
                                        # -- End function
	.globl	printlnInt              # -- Begin function printlnInt
	.p2align	2
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
# %bb.0:
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end2:
	.size	printlnInt, .Lfunc_end2-printlnInt
                                        # -- End function
	.globl	getString               # -- Begin function getString
	.p2align	2
	.type	getString,@function
getString:                              # @getString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	addi	a0, zero, 1024
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	mv	a1, s0
	call	scanf
	mv	a0, s0
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end3:
	.size	getString, .Lfunc_end3-getString
                                        # -- End function
	.globl	getInt                  # -- Begin function getInt
	.p2align	2
	.type	getInt,@function
getInt:                                 # @getInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	lui	a0, %hi(.L.str.1)
	addi	a0, a0, %lo(.L.str.1)
	addi	a1, sp, 8
	call	scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end4:
	.size	getInt, .Lfunc_end4-getInt
                                        # -- End function
	.globl	toString                # -- Begin function toString
	.p2align	2
	.type	toString,@function
toString:                               # @toString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	sw	s1, 4(sp)
	mv	s0, a0
	addi	a0, zero, 12
	call	malloc
	mv	s1, a0
	lui	a0, %hi(.L.str.1)
	addi	a1, a0, %lo(.L.str.1)
	mv	a0, s1
	mv	a2, s0
	call	sprintf
	mv	a0, s1
	lw	s1, 4(sp)
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end5:
	.size	toString, .Lfunc_end5-toString
                                        # -- End function
	.globl	string_ord              # -- Begin function string_ord
	.p2align	2
	.type	string_ord,@function
string_ord:                             # @string_ord
# %bb.0:
	add	a0, a0, a1
	lbu	a0, 0(a0)
	ret
.Lfunc_end6:
	.size	string_ord, .Lfunc_end6-string_ord
                                        # -- End function
	.globl	string_substring        # -- Begin function string_substring
	.p2align	2
	.type	string_substring,@function
string_substring:                       # @string_substring
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	sw	s1, 4(sp)
	sw	s2, 0(sp)
	mv	s0, a1
	mv	s2, a0
	sub	s1, a2, a1
	addi	a0, s1, 1
	call	malloc
	add	a1, s2, s0
	mv	a2, s1
	lw	s2, 0(sp)
	lw	s1, 4(sp)
	lw	s0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	tail	memcpy
.Lfunc_end7:
	.size	string_substring, .Lfunc_end7-string_substring
                                        # -- End function
	.globl	string_parseInt         # -- Begin function string_parseInt
	.p2align	2
	.type	string_parseInt,@function
string_parseInt:                        # @string_parseInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	lui	a1, %hi(.L.str.1)
	addi	a1, a1, %lo(.L.str.1)
	addi	a2, sp, 8
	call	sscanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end8:
	.size	string_parseInt, .Lfunc_end8-string_parseInt
                                        # -- End function
	.globl	string_length           # -- Begin function string_length
	.p2align	2
	.type	string_length,@function
string_length:                          # @string_length
# %bb.0:
	tail	strlen
.Lfunc_end9:
	.size	string_length, .Lfunc_end9-string_length
                                        # -- End function
	.globl	string_add              # -- Begin function string_add
	.p2align	2
	.type	string_add,@function
string_add:                             # @string_add
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	sw	s1, 20(sp)
	sw	s2, 16(sp)
	sw	s3, 12(sp)
	mv	s2, a1
	mv	s3, a0
	call	strlen
	mv	s0, a0
	mv	a0, s2
	call	strlen
	add	a0, s0, a0
	addi	a0, a0, 1
	call	malloc
	mv	s1, a0
	mv	a1, s3
	mv	a2, s0
	call	memcpy
	mv	a0, s1
	mv	a1, s2
	lw	s3, 12(sp)
	lw	s2, 16(sp)
	lw	s1, 20(sp)
	lw	s0, 24(sp)
	lw	ra, 28(sp)
	addi	sp, sp, 32
	tail	strcat
.Lfunc_end10:
	.size	string_add, .Lfunc_end10-string_add
                                        # -- End function
	.globl	string_eq               # -- Begin function string_eq
	.p2align	2
	.type	string_eq,@function
string_eq:                              # @string_eq
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	seqz	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end11:
	.size	string_eq, .Lfunc_end11-string_eq
                                        # -- End function
	.globl	string_ne               # -- Begin function string_ne
	.p2align	2
	.type	string_ne,@function
string_ne:                              # @string_ne
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	snez	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end12:
	.size	string_ne, .Lfunc_end12-string_ne
                                        # -- End function
	.globl	string_lt               # -- Begin function string_lt
	.p2align	2
	.type	string_lt,@function
string_lt:                              # @string_lt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	srli	a0, a0, 31
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end13:
	.size	string_lt, .Lfunc_end13-string_lt
                                        # -- End function
	.globl	string_le               # -- Begin function string_le
	.p2align	2
	.type	string_le,@function
string_le:                              # @string_le
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	slti	a0, a0, 1
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end14:
	.size	string_le, .Lfunc_end14-string_le
                                        # -- End function
	.globl	string_gt               # -- Begin function string_gt
	.p2align	2
	.type	string_gt,@function
string_gt:                              # @string_gt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	sgtz	a0, a0
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end15:
	.size	string_gt, .Lfunc_end15-string_gt
                                        # -- End function
	.globl	string_ge               # -- Begin function string_ge
	.p2align	2
	.type	string_ge,@function
string_ge:                              # @string_ge
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)
	call	strcmp
	not	a0, a0
	srli	a0, a0, 31
	lw	ra, 12(sp)
	addi	sp, sp, 16
	ret
.Lfunc_end16:
	.size	string_ge, .Lfunc_end16-string_ge
                                        # -- End function
	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.1,@object        # @.str.1
.L.str.1:
	.asciz	"%d"
	.size	.L.str.1, 3

	.type	.L.str.2,@object        # @.str.2
.L.str.2:
	.asciz	"%d\n"
	.size	.L.str.2, 4

	.ident	"clang version 10.0.0-++20200412073436+50d7e5d5e7d-1~exp1~20200412054917.132 "
	.section	".note.GNU-stack","",@progbits
