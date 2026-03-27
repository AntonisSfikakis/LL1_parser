import java.io.IOException;

class Main {

  public static void main(String[] args) {
    try {
      System.out.println(new GrammarEvaluator(System.in).eval().toString());
    } catch (IOException | ParseError e) {
      System.err.print(e.getMessage());
    }
  }
}
