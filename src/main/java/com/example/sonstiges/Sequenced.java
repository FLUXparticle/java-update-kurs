import java.util.*;

void printFirst(SequencedCollection<?> collection) {
    System.out.println(collection.getFirst());
}

void main() {
    printFirst(new TreeSet<>());
    printFirst(new ArrayList<>());
    printFirst(new LinkedList<>());
    printFirst(new LinkedHashSet<>());
    // printFirst(new HashSet<>());
}