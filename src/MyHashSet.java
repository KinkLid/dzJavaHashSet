import java.util.Objects;

public class MyHashSet<E> {

    private static final class Node<E> {
        final E key;
        Node<E> next;
        Node(E key, Node<E> next) { this.key = key; this.next = next; }
    }

    private Node<E>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity < 1) throw new IllegalArgumentException("capacity < 1");
        if (loadFactor <= 0 || Float.isNaN(loadFactor) || !Float.isFinite(loadFactor))
            throw new IllegalArgumentException("bad loadFactor");

        this.table = (Node<E>[]) new Node<?>[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = Math.max(1, (int) (initialCapacity * loadFactor));
    }

    public MyHashSet(int initialCapacity) { this(initialCapacity, 0.75f); }

    public MyHashSet() { this(16, 0.75f); }

    public boolean insert(E key) {
        Objects.requireNonNull(key, "null is not supported");
        int idx = indexFor(key.hashCode());
        for (Node<E> n = table[idx]; n != null; n = n.next) {
            if (n.key.equals(key)) return false;
        }
        table[idx] = new Node<>(key, table[idx]);
        if (++size > threshold) resize();
        return true;
    }

    public boolean delete(E key) {
        Objects.requireNonNull(key, "null is not supported");
        int idx = indexFor(key.hashCode());
        Node<E> prev = null, cur = table[idx];
        while (cur != null) {
            if (cur.key.equals(key)) {
                if (prev == null) table[idx] = cur.next; else prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    private int indexFor(int hash) {
        return Math.floorMod(hash, table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTab = table;
        int newCap = oldTab.length * 2 + 1;
        Node<E>[] newTab = (Node<E>[]) new Node<?>[newCap];

        for (Node<E> head : oldTab) {
            while (head != null) {
                Node<E> next = head.next;
                int idx = Math.floorMod(head.key.hashCode(), newCap);
                head.next = newTab[idx];
                newTab[idx] = head;
                head = next;
            }
        }
        table = newTab;
        threshold = Math.max(1, (int) (newCap * loadFactor));
    }
}
