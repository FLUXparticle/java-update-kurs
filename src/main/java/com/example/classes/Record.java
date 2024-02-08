record Complex(double real, double img) {
    Complex(double real) {
        this(real, 0);
    }
}

record Entity(
        int x,
        int y
) { }

void main() {
    var x = new Complex(5);
    var y = new Complex(5, 4);

    System.out.println(STR."x = \{x.real}");
    System.out.println(STR."y = \{y}");
}
