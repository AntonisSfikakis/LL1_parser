import java.io.IOException;

class Main {

  public static void main(String[] args) {
    try {
      // something here
    } catch (IOException | ParseError e) {
      System.err.print(e.getMessage());
    }
  }
}
