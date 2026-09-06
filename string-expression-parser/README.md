# String Expression Parser

A recursive descent LL(1) parser and evaluator for a string expression language supporting concatenation (`**`) and suffix removal (`/`).

## Table of Contents
- [Dependencies](#dependencies)
- [Project Structure](#project-structure)
- [How to Execute](#how-to-execute)
- [The Language](#the-language)
- [Grammar](#grammar)
- [Implementation Details](#implementation-details)
- [Example Programs](#example-programs)

---

## Dependencies

Java 11 or higher. No external libraries required.

---

## Project Structure

```
string-expression-parser/
├── src/
│   ├── Main.java               ← Entry point
│   ├── GrammarEvaluator.java   ← Recursive descent parser and evaluator
│   └── ParseError.java         ← Custom parse error exception
├── tests/
│   └── input/                  ← Test input files
├── Makefile
└── README.md
```

Generated `.class` files go to `gen/`, test results go to `tests/results/`.

---

## How to Execute

### Compile
```bash
make
```

### Run with input from stdin
```bash
make run
```

Type one expression per line and press Enter. Press `Ctrl+D` to exit.

```bash
make run < input.txt
```

### Run tests
```bash
make test
```

### Clean generated files
```bash
make clean
```

---

## The Language

> Note: Read Grammar.md for further explanation about the logic of grammar implementation.

The language evaluates string expressions. All values are strings. One expression per line.

| Operation | Syntax | Meaning |
|---|---|---|
| Concatenation | `a ** b` | Appends `b` to `a` twice: `a ∘ b ∘ b` |
| Suffix removal | `a / b` | Removes `b` from the end of `a` if it is a suffix, otherwise returns `a` unchanged |
| Grouping | `(expr)` | Parenthesized sub-expression |
| Characters | `a-z`, `A-Z` | Single letter — strings are sequences of letters |

### Operator precedence and associativity

| Operator | Precedence | Associativity |
|---|---|---|
| `**` | Higher | Left |
| `/` | Lower | Right |

### Error handling

- Syntax errors print `parse error` to **stderr**
- The parser skips to the next line and continues
- Valid output is printed to **stdout**

---

## Grammar

Final LL(1) grammar after eliminating left recursion and left factoring:

```
exp   -> temp exp2
exp2  -> / exp | ε
temp  -> str temp2
temp2 -> ** str temp2 | ε
str   -> char str2
str2  -> char str2 | ε
char  -> A-Z | a-z | ( exp )
```

### Parse table

```
+-------+---+-------+--------------+-----------+-----------+-----------+---+
|       | $ |   /   |      **      |    A-Z    |    a-z    |     (     | ) |
+-------+---+-------+--------------+-----------+-----------+-----------+---+
|  exp  | - |   -   |      -       | temp exp2 | temp exp2 | temp exp2 | - |
|  exp2 | e | / exp |      -       |     -     |     -     |     -     | e |
|  temp | - |   -   |      -       | str temp2 | str temp2 | str temp2 | - |
| temp2 | e |   e   | ** str temp2 |     -     |     -     |     -     | e |
|  str  | - |   -   |      -       | char str2 | char str2 | char str2 | - |
|  str2 | e |   e   |      e       | char str2 | char str2 | char str2 | e |
|  char | - |   -   |      -       |    A-Z    |    a-z    |  ( exp )  | - |
+-------+---+-------+--------------+-----------+-----------+-----------+---+
```

---

## Implementation Details

Each grammar rule maps directly to a method in `GrammarEvaluator.java`:

| Method | Grammar Rule | Description |
|---|---|---|
| `run()` | — | Main loop. Reads one expression per line until EOF. On parse errors prints `parse error` to stderr and skips to the next line. |
| `eval()` | — | Parses a single expression and verifies the line ends with newline or EOF. |
| `exp()` | `exp -> temp exp2` | Entry point for expression parsing. |
| `exp2(left)` | `exp2 -> / exp \| ε` | Takes `left` as the left operand of `/`. Right recursion gives `/` its right-associativity. Calls `compute()` for suffix removal. |
| `temp()` | `temp -> str temp2` | Handles `**` expressions. |
| `temp2(left)` | `temp2 -> ** str temp2 \| ε` | Uses `left` as an accumulator. On each `**`, appends the right operand twice (`a ** b = a ∘ b ∘ b`), then recurses. Gives `**` its left-associativity without left recursion. |
| `str()` | `str -> char str2` | Handles character sequences. |
| `str2(left)` | `str2 -> char str2 \| ε` | Accumulates characters until a non-string token is encountered (`/`, `**`, `)`, newline, EOF). |
| `chr()` | `char -> a-z \| A-Z \| ( exp )` | Terminal level — characters are consumed here. |
| `compute(left, right)` | — | Checks if `right` is a suffix of `left`. If yes, removes it. Otherwise returns `left` unchanged. |

---

## Example Programs

```
a**b**c
```
Output: `abbcc` — left-associative: `(a**b)**c` = `abb**c` = `abbcc`

```
aaaaa/aa/a
```
Output: `aaaa` — right-associative: `aaaaa/(aa/a)` = `aaaaa/a` = `aaaa`

```
(aaaa/aa)/a
```
Output: `a` — forced left-associativity with parentheses: `(aaaa/aa)/a` = `aa/a` = `a`

```
abb**cc/c
```
Output: `abbccc` — `**` binds tighter: `(abb**cc)/c` = `abbcccc/c` = `abbccc`

```
abc/d
```
Output: `abc` — `d` is not a suffix of `abc`, returns unchanged
