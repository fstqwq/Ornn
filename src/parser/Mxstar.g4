grammar Mxstar;

@header {
package Compiler.Parser;
}
program: programSection* EOF;

programSection:
    functionDeclaration
|   classDeclaration
|   variableDeclaration
;

functionDeclaration:
    returnType Identifier '(' parameterList? ')' block
;

classDeclaration:
    Class Identifier '{' (variableDeclaration | functionDeclaration)* '}'
;

variableDeclaration:
    type variableList ';'
;

parameterList:
    parameter (',' parameter)*
;

parameter:
    type Identifier
;

variableList:
    singleVariableDeclaration (',' singleVariableDeclaration)*
;

singleVariableDeclaration:
    Identifier ('=' expression)?
;

keyword: Int|Bool|String|Null|Void|True|False|If|Else|For|While|Break|Continue|Return|New|Class|This;
Int:        'int';
Bool:       'bool';
String:     'string';
Null:       'null';
Void:       'void';
True:       'true';
False:      'false';
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

Identifier:
    [a-zA-Z][0-9a-zA-Z]*
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
    '"' (~["\n\r\\] | '\\' ["nr\\]) '"'
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

expression:
    constant
|   Identifier
|   This
|   '(' expression ')'
; // TODO

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

LineComment:
    '//' .*? '\n' ->skip
;
BlockComment:
    '/*' .*? '*/' ->skip
;
WhiteSpace:
    [ \t\n\r]+    ->skip
;