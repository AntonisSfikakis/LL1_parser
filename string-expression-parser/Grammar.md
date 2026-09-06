# Grammar Analysis

This document presents the full grammar transformation process for the LL(1) parser, including step-by-step derivations, FIRST/FOLLOW/FIRST+ computations, and the final parse table.

---

## Final Grammar

```
[1]  exp   -> temp exp2
[2]  exp2  -> / exp
[3]  exp2  -> ε
[4]  temp  -> str temp2
[5]  temp2 -> ** str temp2
[6]  temp2 -> ε
[7]  str   -> char str2
[8]  str2  -> char str2
[9]  str2  -> ε
[10] char  -> A-Z
[11] char  -> a-z
[12] char  -> ( exp )
```

---

## Parse Table

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

`e` = ε (empty), `-` = error

---

## Step-by-Step Transformation

### Step 1 — Encode priorities and associativity

Higher priority operators go deeper in the parse tree. `**` has higher priority than `/`, so it binds tighter. `/` is right-associative so `exp` recurses on the right. `**` is left-associative so `temp` recurses on the left.

```
Original                           After encoding priorities
────────────────────────────────   ────────────────────────────────
exp  -> str | exp op exp | (exp)   exp  -> temp / exp | temp
op   -> / | **                     temp -> temp ** str | str
str  -> char | char str     ──►    str  -> char str | char
char -> a-z | A-Z                  char -> a-z | A-Z | (exp)
```

### Step 2 — Remove left recursion

LL(1) parsers cannot handle left recursion. Replace left-recursive rules with right-recursive equivalents using a helper non-terminal.

```
Before                             After
────────────────────────────────   ────────────────────────────────
exp  -> exp / temp | temp          exp   -> temp / exp | temp
temp -> temp ** str | str   ──►    temp  -> str temp2
str  -> char str | char            temp2 -> ** str temp2 | ε
char -> a-z | A-Z | (exp)          str   -> char str | char
                                   char  -> A-Z | a-z | (exp)
```

### Step 3 — Left factoring

Remove common prefixes so FIRST+ sets are disjoint, making the grammar LL(1).

```
Before                             After
────────────────────────────────   ────────────────────────────────
exp   -> temp / exp | temp         exp   -> temp exp2
temp  -> str temp2                 exp2  -> / exp | ε
temp2 -> ** str temp2 | ε   ──►    temp  -> str temp2
str   -> char str | char           temp2 -> ** str temp2 | ε
char  -> A-Z | a-z | (exp)         str   -> char str2
                                   str2  -> char str2 | ε
                                   char  -> a-z | A-Z | (exp)
```

---

## FIRST Sets

| Non-terminal | FIRST |
|---|---|
| `char` | `A-Z`, `a-z`, `(` |
| `str2` | `A-Z`, `a-z`, `(` |
| `str` | `A-Z`, `a-z`, `(` |
| `temp2` | `**` |
| `temp` | `A-Z`, `a-z`, `(` |
| `exp2` | `/` |
| `exp` | `A-Z`, `a-z`, `(` |

---

## FOLLOW Sets

| Non-terminal | FOLLOW |
|---|---|
| `exp` | `$`, `)` |
| `exp2` | `$`, `)` |
| `temp` | `/`, `$`, `)` |
| `temp2` | `/`, `$`, `)` |
| `str` | `**`, `/`, `$`, `)` |
| `str2` | `**`, `/`, `$`, `)` |
| `char` | `A-Z`, `a-z`, `(`, `**`, `/`, `$`, `)` |

---

## FIRST+ Sets

| Rule | FIRST+ |
|---|---|
| `[1]  exp -> temp exp2` | `A-Z`, `a-z`, `(` |
| `[2]  exp2 -> / exp` | `/` |
| `[3]  exp2 -> ε` | `$`, `)` |
| `[4]  temp -> str temp2` | `A-Z`, `a-z`, `(` |
| `[5]  temp2 -> ** str temp2` | `**` |
| `[6]  temp2 -> ε` | `/`, `$`, `)` |
| `[7]  str -> char str2` | `A-Z`, `a-z`, `(` |
| `[8]  str2 -> char str2` | `A-Z`, `a-z`, `(` |
| `[9]  str2 -> ε` | `/`, `$`, `**`, `)` |
| `[10] char -> A-Z` | `A-Z` |
| `[11] char -> a-z` | `a-z` |
| `[12] char -> ( exp )` | `(` |

All FIRST+ sets for rules of the same non-terminal are disjoint — confirming the grammar is LL(1).
