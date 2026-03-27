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

  private StringBuilder sb = new StringBuilder();

  public GrammarEvaluator(InputStream in) throws IOException {
    this.in = in;
    lookahead = in.read();
  }

  private void consume(int symbol) throws IOException, ParseError {
    if (lookahead == symbol)
      lookahead = in.read();
    else
      throw new ParseError();
  }

  private boolean isCharUpper(int c) { return c >= 'A' && c <= 'Z'; }

  private boolean isCharDown(int c) { return c >= 'a' && c <= 'z'; }

  public StringBuilder eval() throws IOException, ParseError {

    exp();

    if (lookahead != -1 && lookahead != '\n') {
      //System.err.println(lookahead);
      throw new ParseError();
    }

    return sb;
  }

  /* expr -> temp exp2 */
  private void exp() throws IOException, ParseError {

    if (isCharUpper(lookahead) || isCharDown(lookahead) || lookahead == '(') {
      temp();
      exp2();
      return;
    }
    //System.err.println(lookahead);
    throw new ParseError();
  }

  /* temp -> str temp2 */
  private void temp() throws IOException, ParseError {
    if (isCharUpper(lookahead) || isCharDown(lookahead) || lookahead == '(') {
      str();
      temp2();
      return;
    }
    //System.err.println(lookahead);
    throw new ParseError();
  }

  /* str -> char str2 */
  private void str() throws IOException, ParseError {
    if (isCharUpper(lookahead) || isCharDown(lookahead) || lookahead == '(') {
      chr();
      str2();
      return;
    }
    //System.err.println(lookahead);
    throw new ParseError();
  }

  /* char -> A-Z | a-z | (exp) */
  private void chr() throws IOException, ParseError {
    if (lookahead == '(') {
      consume('(');
      exp();
      if (lookahead != ')')
        throw new ParseError();
      consume(')');
      return;
    }

    if (isCharDown(lookahead) || isCharUpper(lookahead)) {
      sb.append((char)lookahead);
      consume(lookahead);
      return;
    }
    //System.err.println(lookahead);
    throw new ParseError();
  }

  /* exp2 -> / exp | e */
  private void exp2() throws IOException, ParseError {
    if (lookahead == ')' || lookahead == -1 || lookahead == '\n')
      return;

aaaaa/aa/a
    if (lookahead == '/') {
      sb.append((char)lookahead);
      consume(lookahead);
      exp();
      sb = compute();
      // aa/a -> a -> for
      // a if b != a
      // a/b if b is suffix
      return;
    }
    //System.err.println(lookahead);
    throw new ParseError();
  }

  /* temp2 -> ** str temp2 | e */
  private void temp2() throws IOException, ParseError {
    if (lookahead == ')' || lookahead == -1 || lookahead == '\n' || lookahead == '/')
      return;

    if (lookahead == '*') {
      sb.append(lookahead);
      consume(lookahead);

      if (lookahead != '*')
        throw new ParseError();

      sb.append(lookahead);
      consume(lookahead);

      str();
      temp2();
      return;
      // sb = concat();
    }
    //System.err.println(lookahead);
    throw new ParseError();
  }

  /* str2 -> char str2 */
  private void str2() throws IOException, ParseError {
    if (lookahead == -1 || lookahead == '\n' || lookahead == '/' ||
        lookahead == ')')
      return;
    if (lookahead == '*') {
      consume(lookahead);
      if (lookahead == '*')
        return;
      throw new ParseError();
    }

    if (isCharDown(lookahead) || isCharUpper(lookahead) || lookahead == '(') {
      chr();
      str2();
      return;
    }
    throw new ParseError();
  }

  /* this function takes two strings and computes the suffix */
  /* left = before / , right = suffix */

  private StringBuilder compute() {

    /* it devides the main string in two string builders and compares them */
    StringBuilder left = new StringBuilder();
    StringBuilder right = new StringBuilder();
    boolean inBackSlash = false;

    for (int i = 0; i < sb.length(); i++) {
      if (sb.charAt(i) == '/') {
        inBackSlash = true;
        continue;
      }

      if (!inBackSlash)
        left.append(sb.charAt(i));
      else
        right.append(sb.charAt(i));
    }

    // temp1 -> aaaa
    // temp2 -> a
    // suffix starts at = 4 - 1 = 3

    int left_suffix = left.length() - right.length();

    for (int j = 0; j < right.length(); j++) {
      if (left.charAt(left_suffix + j) != right.charAt(j)) {
        return left;
      }
    }

    return new StringBuilder(left.substring(0, left_suffix));
  }

  private void concat() { return; }
}
