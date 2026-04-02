#!/bin/bash

rm -rf Scanner* output.java Output.java Output.class
java -jar /usr/share/java-cup-11b.jar -parser Parser -symbols sym -expect 3 parser.cup
jflex scanner.flex
javac -cp .:/usr/share/java-cup-11b-runtime.jar *.java
java -cp .:/usr/share/java-cup-11b-runtime.jar Main < test.txt > Output.java
javac Output.java
java Output
