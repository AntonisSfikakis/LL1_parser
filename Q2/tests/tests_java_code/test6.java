public class Main {
    public static void main(String[] args) {
        System.out.println(deep("he", "hello", "worldhe"));
        System.out.println(deep("he", "world", "test"));
        System.out.println(deep("he", "hello", "test"));
    }

    public static String deep(String a, String b, String c) {
      return b.startsWith(a) ? (c.startsWith(b) ? (a.endsWith(c) ? ("all match") : "two match") : "one match") : "no match";
    }
}

