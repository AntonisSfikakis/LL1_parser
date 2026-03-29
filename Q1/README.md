## Implementation Details 

Compile and run the parser:
```
javac *.java
java Main
```

- `Main.java` — Entry point. Creates a `GrammarEvaluator` on `System.in` and calls `run()`.
- `ParseError.java` — Custom exception thrown on syntax errors. Caught in `run()` which prints "parse error" to stderr.
- `GrammarEvaluator.java` — Recursive descent parser and evaluator. Each grammar rule maps directly to a method:
  - `run()` — Main loop. Reads one expression per line until EOF. On parse errors, skips to the next line.
  - `eval()` — Parses a single expression and checks that the line ends properly (newline or EOF).
  - `exp()` — `exp -> temp exp2`
  - `exp2(left)` — `exp2 -> / exp | ε`. Takes `left` as the already-evaluated left operand of `/`. Calls `exp()` recursively to get the right operand — this right recursion gives `/` its right-associativity. Passes both sides to `compute()` for suffix removal.
  - `temp()` — `temp -> str temp2`
  - `temp2(left)` — `temp2 -> ** str temp2 | ε`. Takes `left` as an accumulator — the result built so far. On each `**`, calls `str()` for the right operand and appends it twice to `left` (`a ** b = a ∘ b ∘ b`), then passes the updated `left` to the next `temp2()`. This accumulator pattern gives `**` its left-associativity without left recursion.
  - `str()` — `str -> char str2`
  - `str2(left)` — `str2 -> char str2 | ε`. Takes `left` as the string built so far. Keeps appending characters from `chr()` until it hits something that doesn't belong to a string (`/`, `**`, `)`, newline, EOF). Returns the fully assembled string.
  - `chr()` — `char -> a-z | A-Z | ( exp )`. Terminal level — this is where characters are actually consumed.
  - `compute(left, right)` — Checks if `right` is a suffix of `left`. If yes, removes it. Otherwise returns `left` unchanged.
