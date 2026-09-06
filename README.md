# String Parser & Translator

Two compiler front-end exercises: a recursive-descent LL(1) parser for a string-expression language, and a JavaCUP/JFlex-based translator from a small string-manipulation language into executable Java. Built for a university compilers course.

**Antonis Sfikakis**

---

## Structure

### `string-expression-parser/` — LL(1) String Expression Parser

| File | Description |
|---|---|
| `Grammar.md` | Grammar transformation, FIRST/FOLLOW/FIRST+ sets, parse table |
| `README.md` | Build instructions and implementation details |
| `src/Main.java` | Entry point |
| `src/GrammarEvaluator.java` | Recursive descent LL(1) parser |
| `src/ParseError.java` | Parse error exception |

### `string-to-java-translator/` — String Language to Java Translator

| File | Description |
|---|---|
| `README.md` | Build instructions and implementation details |
| `src/Runner.java` | Entry point |
| `src/scanner.flex` | JFlex lexer specification |
| `src/parser.cup` | JavaCUP grammar and translator |
| `lib/` | JavaCUP and JFlex jars |
