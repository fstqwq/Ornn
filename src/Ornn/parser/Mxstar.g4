grammar Mxstar;

@header {
package Ornn.Parser;
}

program: programSection* EOF;

programSection:
    functionDeclaration
|   classDeclaration
|   variableDeclaration
;

functionDeclaration:
    returnType Identifier '(' parameterDeclarationList? ')' block
;

classDeclaration:
    Class Identifier '{' (variableDeclaration | functionDeclaration | constructiveFunctionDeclaration)* '}' ';'
;

variableDeclaration:
    type variableDeclarationList ';'
;

parameterDeclarationList:
   parameterDeclaration (',' parameterDeclaration)*
;

parameterDeclaration:
    type Identifier
;

variableDeclarationList:
    singleVariableDeclaration (',' singleVariableDeclaration)*
;

singleVariableDeclaration:
    Identifier ('=' expression)?
;

constructiveFunctionDeclaration:
    Identifier '(' ')' block
;

constant:
    StringConstant
|   IntConstant
|   NullConstant
|   BoolConstant
;
IntConstant:
    [0-9]+
;
StringConstant:
    '"' (~["\n\r\\] | '\\' ["nr\\])*? '"'
;
NullConstant:   Null;
BoolConstant:   True | False;

type:
    nonarrayType
|   type '[' ']'
;
nonarrayType:
    Bool
|   Int
|   Void
|   String
|   Identifier
;
returnType:   type|Void;
newType:
    nonarrayType ('[' expression ']')+ ('[' ']')+ ('[' expression ']')+
|   nonarrayType
|   nonarrayType '(' ')'
|   nonarrayType ('[' expression ']')+ ('[' ']')*
;
expression:
    expression      op=('++'|'--')
|   expression      '(' parameterList?   ')'
|   name=expression '[' index=expression ']'
|   expression      '.' Identifier
|   <assoc=right>   op=('++' | '--')     expression
|   <assoc=right>   op=('+' | '-')       expression
|   <assoc=right>   op=('!' | '~')       expression
|   <assoc=right>   'new'                newType
|   src1=expression op=('*' | '/' | '%') src2=expression
|   src1=expression op=('+' | '-')       src2=expression
|   src1=expression op=('<<' | '>>')     src2=expression
|   src1=expression op=('<' | '<=' )     src2=expression
|   src1=expression op=('>' | '>=' )     src2=expression
|   src1=expression op=('!=' | '==')     src2=expression
|   src1=expression op='&'               src2=expression
|   src1=expression op='^'               src2=expression
|   src1=expression op='|'               src2=expression
|   src1=expression op='&&'              src2=expression
|   src1=expression op='||'              src2=expression
|   <assoc=right> src1=expression op='=' src2=expression
|   constant
|   This
|   Identifier
|   '(' expression ')'
;

parameterList:
    expression (',' expression)*
;

statement:
    block
|   variableDeclaration
|   expression ';'
|   conditionStatement
|   loopStatement
|   controlStatement
|   ';'
;
block:
    '{' statement* '}'
;
conditionStatement:
    If '(' expression ')'
        thenStmt = statement
    (Else
        elseStmt = statement)?
;
loopStatement:
    For '('
            init = expression ? ';'
            cond = expression ? ';'
            step = expression ?
        ')'
        statement
|   While '(' expression ')' statement
;
controlStatement:
    Return expression? ';'
|   Break ';'
|   Continue ';'
;

Int:        'int';
Bool:       'bool';
String:     'string';
Void:       'void';
If:         'if';
Else:       'else';
For:        'for';
While:      'while';
Return:     'return';
Break:      'break';
Continue:   'continue';
New:        'new';
Class:      'class';
This:       'this';
fragment Null:  'null';
fragment True:  'true';
fragment False: 'false';

Identifier:
    [a-zA-Z][0-9a-zA-Z_]*
;

LineComment:
    '//' .*? (EOF | '\n')   ->skip
;
BlockComment:
    '/*' .*? '*/'           ->skip
;
WhiteSpace:
    [ \t\n\r]+              ->skip
;