extern int scanf(const char *restrict, ...);
extern int sscanf(const char *restrict, const char *restrict, ...);
extern int printf(const char *restrict, ...);
extern int sprintf(char *, const char *, ...);
extern unsigned int strlen(const char *);
extern int strcmp(const char*, const char *);
extern char* strcat(char*, const char *);
extern void* memcpy(void*, const void*, unsigned int);
extern void* malloc(unsigned int);
extern char* _mOff_;

void print(char* str) {
	printf("%s", str);
}

void printInt(int n) {
	printf("%d", n);
}

void printlnInt(int n) {
	printf("%d\n", n);
}

char* getString() {
	char *ret = _mOff_;
	_mOff_ = ret + 512;
	scanf("%s", ret);
	return ret;
}

int getInt() {
	int d;
	scanf("%d", &d);
	return d;
}

char* toString(int i) {
	/*
	char *ret, *cur, *L, *R; char t;
	if (i == 0) {
		ret = "0";
	}
	else if (i == 0x80000000) {
		ret = "-2147483648";
	}
	else {
		ret = (char*)malloc(12);
		cur = ret;
		if (i < 0) {
			i = -i;
			t = '-';
		} else {
			t = '\0';
		}
		while (i != 0) {
			*cur++ = (char)('0' + i % 10);
			i /= 10;	
		}
		if (t) {
			*cur++ = t;
		}
		*cur = '\0';
		L = ret; R = cur - 1;
		while (L < R) {
			t = *L;
			*L = *R;
			*R = t;
			L++; R--;
		}
	}
	return ret;*/
	char *ret = _mOff_;
	_mOff_ = ret + 12;
	sprintf(ret, "%d", i);
	return ret;
}

int string_ord(char *s, int i) {
	return *(s + i);
}
char* string_substring(char *s, int l, int r) {
	char *ret = _mOff_;
	_mOff_ = ret + (r - l + 1);
	memcpy(ret, s + l, r - l);
	return ret;
}
int string_parseInt(char *s) {
	int d;
	sscanf(s, "%d", &d);
	return d;
}
int string_length(char *s) {
	return strlen(s);
}
char* string_add(char *lhs, char *rhs) {
	int l = strlen(lhs), r = strlen(rhs);
	char *ret = _mOff_;
	_mOff_ = ret + l + r + 1;
	memcpy(ret, lhs, l);
	strcat(ret, rhs);
	return ret;
}
_Bool string_eq(char *lhs, char *rhs) {
	return strcmp(lhs, rhs) == 0;
}
_Bool string_ne(char *lhs, char *rhs) {
	return strcmp(lhs, rhs) != 0;
}
_Bool string_lt(char *lhs, char *rhs) {
	return strcmp(lhs, rhs) < 0;
}
_Bool string_le(char *lhs, char *rhs) {
	return strcmp(lhs, rhs) <= 0;
}
_Bool string_gt(char *lhs, char *rhs) {
	return strcmp(lhs, rhs) > 0;
}
_Bool string_ge(char *lhs, char *rhs) {
	return strcmp(lhs, rhs) >= 0;
}
