#!/bin/bash

rm -rf Scanner*;
jflex scanner.flex
javac Scanner.java
java Scanner test.txt

