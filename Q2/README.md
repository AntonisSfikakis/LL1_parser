# String Language to Java Translator

A translator from a custom string-manipulation language to executable Java code, implemented using JFlex and JavaCUP.

## Table of Contents
- [Dependencies](#dependencies)
- [Project Structure](#project-structure)
- [How to Execute](#how-to-execute)
- [The Language](#the-language)
- [Example Programs](#example-programs)

---

## Dependencies

The required jars are included in the `lib/` directory:

| Library | Version |
|---|---|
| JFlex | 1.9.1 |
| JavaCUP | 11b |

Java 11 or higher is required.

---

## Project Structure

```
Q2/
├── src/
│   ├── scanner.flex      ← JFlex lexer specification
│   ├── parser.cup        ← JavaCUP grammar and translator
│   └── Runner.java       ← Entry point, reads from stdin
├── tests/
│   └── input/            ← Test input files
├── lib/
│   ├── jflex-full-1.9.1.jar
│   ├── java-cup-11b.jar
│   └── java-cup-11b-runtime.jar
├── Makefile
└── README.md
```

Generated files go to `gen/`, translated output goes to `output/`, test results go to `tests/results/`.

---

## How to Execute

> **Note:** All warning and informational messages from the parser are suppressed.  
> Only the translated Java program is written to stdout. Parse errors are written to stderr.  

### Compile
```bash
make
```

### Run with input from stdin
```bash
make run < input.txt
```
Generated files go to `gen/`.  
The translated `Main.java` is written to `output/`, compiled, and executed automatically.

### Run tests
```bash
make test
```

Runs all test files from `tests/input/`, translates each one, compiles and executes the result, and prints the output.  
Check the Parsed generated java code in `test/test_java_code` after running `make test`.  
Also all the results from my tests are in the `test/results`  

### Clean generated files
```bash
make clean
```

---

## The Language

All values in the language are strings. A program consists of **function declarations** followed by **top-level function calls**. Declarations must come before all statements.

### Function declaration
```
functionName(param1, param2) {
    expr
}
```

### String operations
| Operation | Meaning |
|---|---|
| `a + b` | String concatenation |
| `a prefix b` | Whether `a` is a prefix of `b` |
| `a suffix b` | Whether `a` is a suffix of `b` |

### Conditionals
Every `if` must be followed by an `else`. Conditions use `prefix` or `suffix`:
```
if (condition)
    expr
else
    expr
```

Operator precedence: `if` < `+` (concatenation has higher precedence).

---

## Example Programs

### Simple function calls

Input:
```
name() {
    "John"
}

surname() {
    "Doe"
}

fullname(first, sep, last) {
    first + sep + last
}

name()
surname()
fullname(name(), " ", surname())
```

Output:
```java
public class Main {
    public static void main(String[] args) {
        System.out.println(name());
        System.out.println(surname());
        System.out.println(fullname(name(), " ", surname()));
    }

    public static String name() {
        return "John";
    }

    public static String surname() {
        return "Doe";
    }

    public static String fullname(String first, String sep, String last) {
        return first + sep + last;
    }
}
```

### Nested conditionals

Input:
```
findLangType(langName) {
    if ("Java" prefix langName)
        if (langName prefix "Java")
            "Static"
        else
            if ("script" suffix langName)
                "Dynamic"
            else
                "Unknown"
    else
        if ("script" suffix langName)
            "Probably Dynamic"
        else
            "Unknown"
}

findLangType("Java")
findLangType("Javascript")
findLangType("Typescript")
```

Output:
```java
public class Main {
    public static void main(String[] args) {
        System.out.println(findLangType("Java"));
        System.out.println(findLangType("Javascript"));
        System.out.println(findLangType("Typescript"));
    }

    public static String findLangType(String langName) {
        return "Java".startsWith(langName) ? (langName.startsWith("Java") ? ("Static") : "script".endsWith(langName) ? ("Dynamic") : "Unknown") : "script".endsWith(langName) ? ("Probably Dynamic") : "Unknown";
    }
}
```
