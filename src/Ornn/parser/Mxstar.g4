grammar Mxstar;

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
    StringConstant  #intLiteral
|   IntConstant     #strLiteral
|   NullConstant    #nullLiteral
|   BoolConstant    #boolLiteral
;

IntConstant: [0-9]+;
StringConstant:'"' (~["\n\r\\] | '\\' ["nr\\])*? '"';
NullConstant:   Null;
BoolConstant:   True | False;

type:
    nonarrayType    #simpleType
|   type '[' ']'    #arrayType
;
nonarrayType:
    Bool
|   Int             #typeInt
|   Void            #typeBool
|   String          #typeString
|   Identifier      #typeIdentifier
;
returnType:   type|Void;
creator:
    nonarrayType ('[' expression ']')+ ('[' ']')+ ('[' expression ']')+     #rejectCreator
|   nonarrayType '(' ')'                                                    #classCreator
|   nonarrayType ('[' expression ']')+ ('[' ']')*                           #arrayCreator
|   nonarrayType                                                            #simpleCreator
;
expression:
    expression      op=('++'|'--')                                          #sufExpr
|   expression      '(' parameterList?   ')'                                #funcallExpr
|   name=expression '[' index=expression ']'                                #subscriptExpr
|   expression      '.' Identifier                                          #memaccessExpr
|   <assoc=right>   op=('++' | '--')     expression                         #unaryExpr
|   <assoc=right>   op=('+' | '-')       expression                         #unaryExpr
|   <assoc=right>   op=('!' | '~')       expression                         #unaryExpr
|   <assoc=right>   'new'                creator                            #newExpr
|   src1=expression op=('*' | '/' | '%') src2=expression                    #binaryExpr
|   src1=expression op=('+' | '-')       src2=expression                    #binaryExpr
|   src1=expression op=('<<' | '>>')     src2=expression                    #binaryExpr
|   src1=expression op=('<' | '<=' )     src2=expression                    #binaryExpr
|   src1=expression op=('>' | '>=' )     src2=expression                    #binaryExpr
|   src1=expression op=('!=' | '==')     src2=expression                    #binaryExpr
|   src1=expression op='&'               src2=expression                    #binaryExpr
|   src1=expression op='^'               src2=expression                    #binaryExpr
|   src1=expression op='|'               src2=expression                    #binaryExpr
|   src1=expression op='&&'              src2=expression                    #binaryExpr
|   src1=expression op='||'              src2=expression                    #binaryExpr
|   <assoc=right> src1=expression op='=' src2=expression                    #binaryExpr
|   constant                                                                #literal
|   This                                                                    #thisExpr
|   Identifier                                                              #identifier
|   '(' expression ')'                                                      #bracketExpr
;

parameterList:
    expression (',' expression)*
;

statement:
    block                   #blockStmt
|   variableDeclaration     #vDecStmt
|   expression ';'          #exprStmt
|   conditionStatement      #condStmt
|   loopStatement           #loopStmt
|   controlStatement        #ctrlStmt
|   ';'                     #emptyStmt
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
    Return expression? ';'  #returnStmt
|   Break ';'               #breakStmt
|   Continue ';'            #continueStmt
;

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