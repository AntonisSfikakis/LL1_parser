import java.io.*;
import java.io.IOException;

public class Runner { 
  public static void main(String[] args) throws Exception {
        Parser p = new Parser(new Scanner(new InputStreamReader(System.in)));
        try {
            p.parse();
        } catch (Exception e) {
            System.err.println("parse error");
        }

  }


}
