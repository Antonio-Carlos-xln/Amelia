# Amelia

Amelia is meant to be a language for describing data in a similar manner as json or yaml, with only diference being that you can do it in plain english-like format. It allows yo to describe your data (or actions...) in a structured and programmaticcaly parsable way, yet allows anyone to easily realises what's written, since its grammar aims to be as close to plain English as possible. Currently it's still under active development and in alpha mode.

## Grammar

The grammar of the language is already somewhat stable altough some other reserved keywords may be added in the near future and some implementations details may change, as well. Here is its grammar:

```
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

```

And here's a short example of a how a document looks like in this format:

```
let $_message of $messages 
                   with "client" as Emitter,
                        "company" as Receiver,
                        "high" as Priority
    let $_reply of $messages
             with "company" as Emitter,
                  "client" as Receiver,
                  "low" as Priority
    set "partner" as Emitter of $0_message of $messages
    set  "medium" as Priority of $1_message of $messages   
```

And the produced output:

```
{
  "messages": [
    {
      "Emitter": "partner",
      "Priority": "high",
      "Receiver": "company"
    },
    {
      "Emitter": "company",
      "Priority": "medium",
      "Receiver": "client"
    }
  ]
}
```

## App mode

The current library can be used in a app mode where you can have it to convert your data to json format through a command line application. Here's an example of such command:

```
   amelia "./test/testfile.txt" -e json -n "message_log" -p "./test/res/"
```
Supposing that you already has the jar invocation aliased at your session. you can use -h to see the full list of options.

## Library Mode

The application can also have its classes imported, as any other Java project, and be used to built your own applications that can consume input in the language's format. For this purpose, the project is well documented from the ground up (you can build the documentation with javadoc), but since it's still in alpha mode, the library and its docs are currently not hosted in any package manager.
