public class Main {
    public static void main(String[] args) {
        System.out.println(cond("yes"));
        System.out.println(cond("no"));
    }

    public static String cond(String x) {
      return "yes".startsWith(x) ? ("positive") : "negative";
    }
}

