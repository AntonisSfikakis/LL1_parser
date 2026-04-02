public class Main {
    public static void main(String[] args) {
        System.out.println(both("hello"));
        System.out.println(both("world"));
    }

    public static String upper(String x) {
      return x + "!";
    }

    public static String wrap(String x) {
      return "[" + x + "]";
    }

    public static String both(String x) {
      return wrap(upper(x));
    }
}

