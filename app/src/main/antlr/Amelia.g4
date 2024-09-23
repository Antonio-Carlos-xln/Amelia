grammar Amelia;

@header{
package com.antoniocarlos.amelia;
}

INT: [0-9]+;
FLOAT: [0-9]+'.'[0-9]+;
STRING: '"' (~["\r\n])* '"';
ID:[a-zA-Z$][a-zA-Z0-9_]*;

WS: [ \t\n]+ -> skip;

val
:
INT # Int
|FLOAT # Float
|STRING # String
|ID ('of' ID)* # GID
;

assign
:
val 'as' ID ','?
;

specifier
:
'with' assign+
;

decl
:
'let' val specifier? # Let
|'set' val 'as' val # Set
|'delete' val # Delete
;

root: decl+;
