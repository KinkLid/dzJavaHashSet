
public class Main {
    public static void main(String[] args) {
        MyHashSet<String> set = new MyHashSet<>(10, 0.75f);
        System.out.println(set.insert("a")); // true
        System.out.println(set.insert("b")); // true
        System.out.println(set.insert("a")); // false
        System.out.println(set.delete("b")); // true
        System.out.println(set.delete("b")); // false
    }
}