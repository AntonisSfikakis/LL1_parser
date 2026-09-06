# String Language to Java Translator

A translator from a custom string-manipulation language to executable Java code, implemented using JFlex and JavaCUP.

## Table of Contents
- [Dependencies](#dependencies)
- [Project Structure](#project-structure)
- [How to Execute](#how-to-execute)
- [The Language](#the-language)
- [Example Programs](#example-programs)
- [Implementation Notes](#implementation-notes)

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
string-to-java-translator/
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

> **Note:** the `Makefile` refers to the jars as `../lib/...`. If you run `make` from `Q2/` as described below and it can't find the jars, change `LIB_DIR` in the `Makefile` to `lib` instead of `../lib`.

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
Check the generated Java code for each test in `tests/tests_java_code/` after running `make test`.
The captured output of each test run is in `tests/results/`.

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

### String literals
String literals support the escape sequences `\t`, `\n`, `\r`, `\"`, and `\\`.

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

---

## Implementation Notes

**Single-pass, syntax-directed translation.** There's no separate AST or code-generation pass — the JavaCUP grammar's own semantic actions build the output Java source directly while parsing, accumulating it into two `StringBuilder`s: one for function declarations (`decls`), one for the top-level `System.out.println` calls (`calls`). The two are stitched together into the final `Main` class once parsing completes.

**Declaration-before-statement ordering is enforced by the parser, not just documented.** A `seenStatement` flag is set the first time a top-level call or if-statement is parsed; if a function declaration production fires after that flag is set, the parser throws immediately. This directly enforces the language rule that all declarations must precede all statements, rather than leaving it as an unchecked convention.

**`prefix`/`suffix` map directly to Java's `String` methods**, with the operand order matching Java's method-receiver convention: `a prefix b` (is `a` a prefix of `b`) compiles to `b.startsWith(a)`, and `a suffix b` to `b.endsWith(a)`.

**`if`/`else` compiles to Java's ternary operator** (`cond ? e1 : e2`), using the exact same translation whether the conditional appears as a full top-level statement or nested inside another expression.

**The grammar has 3 known shift/reduce conflicts**, explicitly acknowledged via JavaCUP's `-expect 3` flag rather than silently ignored. This is consistent with the classic dangling-else style ambiguity, since `if`/`else` appears both as a top-level statement and as a nested expression form. *(Worth confirming the exact conflict report before citing this precisely — run javacup on `parser.cup` without `-nowarn` to see the details.)*
