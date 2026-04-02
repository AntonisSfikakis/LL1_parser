public class Main {
    public static void main(String[] args) {
        System.out.println(MystartsWith("pre", "preview"));
        System.out.println(MyendsWith("playing", "ing"));
        System.out.println(classify("preview"));
        System.out.println(classify("posting"));
        System.out.println(classify("predetermined"));
        System.out.println(classify("random"));
        System.out.println(buildWord("pre", "view", "ing"));
        System.out.println(analyze("unbelievable"));
        System.out.println(analyze("undefined"));
        System.out.println(analyze("running"));
        System.out.println(analyze("education"));
        System.out.println(analyze("hello"));
    }

    public static String MystartsWith(String a, String b) {
      return b.startsWith(a) ? ("yes") : "no";
    }

    public static String MyendsWith(String a, String b) {
      return b.endsWith(a) ? ("yes") : "no";
    }

    public static String classify(String word) {
      return word.startsWith("pre") ? ("ing".endsWith(word) ? ("pre and ing") : "ed".endsWith(word) ? ("pre and ed") : "pre only") : word.startsWith("post") ? ("ing".endsWith(word) ? ("post and ing") : "post only") : "no prefix";
    }

    public static String buildWord(String pre, String root, String suf) {
      return pre + root + suf;
    }

    public static String analyze(String word) {
      return word.startsWith("un") ? ("able".endsWith(word) ? ("negated ability: " + word) : "ed".endsWith(word) ? ("negated past: " + word) : "negated: " + word) : "tion".endsWith(word) ? ("noun: " + word) : "ing".endsWith(word) ? ("verb: " + word) : "unknown: " + word;
    }
}

