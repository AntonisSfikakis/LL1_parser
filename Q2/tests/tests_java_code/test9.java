public class Main {
    public static void main(String[] args) {
        System.out.println(isJava("Java"));
        System.out.println(isJava("Javascript"));
        System.out.println(isJava("Python"));
        System.out.println(isFunctional("Haskell"));
        System.out.println(isFunctional("Python"));
        System.out.println(getFamily("Kotlin"));
        System.out.println(getFamily("Python"));
        System.out.println(describe("Java"));
        System.out.println(describe("Kotlin"));
        System.out.println(describe("Haskell"));
        System.out.println(describe("Python"));
    }

    public static String isJava(String name) {
      return name.startsWith("Java") ? ("Java".endsWith(name) ? ("yes") : "starts with Java") : "no";
    }

    public static String isFunctional(String name) {
      return name.startsWith("Haskell") ? ("functional") : name.startsWith("Erlang") ? ("functional") : name.startsWith("Clojure") ? ("functional") : "not functional";
    }

    public static String getFamily(String name) {
      return name.startsWith("Java") ? ("JVM") : name.startsWith("Kotlin") ? ("JVM") : name.startsWith("Scala") ? ("JVM") : name.startsWith("Clojure") ? ("JVM") : "other";
    }

    public static String describe(String name) {
      return name.startsWith("Java") ? ("Language: " + name + " Family: " + getFamily(name) + " Functional: " + isFunctional(name)) : name.startsWith("Kotlin") ? ("Language: " + name + " Family: " + getFamily(name) + " Functional: " + isFunctional(name)) : "Language: " + name + " Family: " + getFamily(name) + " Functional: " + isFunctional(name);
    }
}

