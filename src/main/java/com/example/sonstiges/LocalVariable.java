import java.util.*;

void main() {
    // infers ArrayList<String>
    var list = new ArrayList<String>();

    // infers Stream<String>
    var stream = list.stream();
}
