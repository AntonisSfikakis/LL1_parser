import java.io.IOException;
import java.io.InputStream;

/*

- == Error

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
*/

class GrammarEvaluator {
  private final InputStream in;

  private int lookahead;

  // private StringBuilder sb = new StringBuilder();

  public GrammarEvaluator(InputStream in) throws IOException {
    this.in = in;
    lookahead = in.read();
  }

  public void run() throws IOException {
    while (lookahead != -1) {

      if (lookahead == '\n') {
        lookahead = in.read();
        continue;
      }

      try {
        System.out.println(eval());
      } catch (ParseError e) {
        System.err.print(e.getMessage());
        while (lookahead != '\n' && lookahead != -1)
          lookahead = in.read();
      }
    }
  }

  private void consume(int symbol) throws IOException, ParseError {
    if (lookahead == symbol)
      lookahead = in.read();
    else
      throw new ParseError();
  }

  private boolean isCharUpper(int c) {
    return c >= 'A' && c <= 'Z';
  }

  private boolean isCharDown(int c) {
    return c >= 'a' && c <= 'z';
  }

  public StringBuilder eval() throws IOException, ParseError {
    StringBuilder sb = new StringBuilder();
    sb = exp();

    if (lookahead != -1 && lookahead != '\n') {
      // System.err.println(lookahead);
      throw new ParseError();
    }

    return sb;
  }

  /* expr -> temp exp2 */
  private StringBuilder exp() throws IOException, ParseError {

    if (isCharUpper(lookahead) || isCharDown(lookahead) || lookahead == '(') {
      return exp2(temp());
    }
    // System.err.println(lookahead);
    throw new ParseError();
  }

  /* temp -> str temp2 */
  private StringBuilder temp() throws IOException, ParseError {
    if (isCharUpper(lookahead) || isCharDown(lookahead) || lookahead == '(') {
      return temp2(str());
    }
    // System.err.println(lookahead);
    throw new ParseError();
  }

  /* str -> char str2 */
  private StringBuilder str() throws IOException, ParseError {
    if (isCharUpper(lookahead) || isCharDown(lookahead) || lookahead == '(') {
      return str2(chr());
    }
    // System.err.println(lookahead);
    throw new ParseError();
  }

  /* char -> A-Z | a-z | (exp) */
  private StringBuilder chr() throws IOException, ParseError {
    StringBuilder sb = new StringBuilder();

    if (lookahead == '(') {
      consume('(');
      sb = exp();
      if (lookahead != ')')
        throw new ParseError();
      consume(')');
      return sb;
    }

    if (isCharDown(lookahead) || isCharUpper(lookahead)) {
      sb.append((char) lookahead);
      consume(lookahead);
      return sb;
    }
    // System.err.println(lookahead);
    throw new ParseError();
  }

  /* exp2 -> / exp | e */
  private StringBuilder exp2(StringBuilder left)
      throws IOException, ParseError {
    StringBuilder right = new StringBuilder();

    if (lookahead == ')' || lookahead == -1 || lookahead == '\n')
      return left;

    if (lookahead == '/') {
      consume(lookahead);
      right = exp();
      // aa/a -> a -> for
      // a if b != a
      // a/b if b is suffix
      return compute(left, right);
    }
    // System.err.println(lookahead);
    throw new ParseError();
  }

  /* temp2 -> ** str temp2 | e */
  private StringBuilder temp2(StringBuilder left)
      throws IOException, ParseError {
    StringBuilder right = new StringBuilder();

    if (lookahead == ')' || lookahead == -1 || lookahead == '\n' ||
        lookahead == '/')
      return left;

    if (lookahead == '*') {

      consume(lookahead);
      if (lookahead != '*')
        throw new ParseError();
      consume(lookahead);

      right = str();
      left.append(right);
      left.append(right);
      return temp2(left);
    }
    // System.err.println(lookahead);
    throw new ParseError();
  }

  /* str2 -> char str2 */
  private StringBuilder str2(StringBuilder left)
      throws IOException, ParseError {

    if (lookahead == -1 || lookahead == '\n' || lookahead == '/' ||
        lookahead == ')')
      return left;

    if (lookahead == '*') {
      return left;
    }

    if (isCharDown(lookahead) || isCharUpper(lookahead) || lookahead == '(') {
      left.append(chr());
      return str2(left);
    }

    throw new ParseError();
  }

  /* this function takes two strings and computes the suffix */
  /* left = before / , right = suffix */

  private StringBuilder compute(StringBuilder left, StringBuilder right) {

    /* it devides the main string in two string builders and compares them */
    // temp1 -> aaaa
    // temp2 -> a
    // suffix starts at = 4 - 1 = 3

    int left_suffix = left.length() - right.length();
    if (left_suffix < 0)
      return left;

    for (int j = 0; j < right.length(); j++) {
      if (left.charAt(left_suffix + j) != right.charAt(j)) {
        return left;
      }
    }

    return new StringBuilder(left.substring(0, left_suffix));
  }
}
