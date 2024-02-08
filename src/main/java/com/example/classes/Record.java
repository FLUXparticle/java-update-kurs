package com.example.classes;

public class Record {

    public static void main(String[] args) {
        var x = new Complex(5);
        var y = new Complex(5, 4);

        System.out.println("x = " + x);
        System.out.println("y = " + y);
    }

}

record Complex(double real, double img) {
    Complex(double real) {
        this(real, 0);
    }
}

record Entity(
        int x,
        int y
) { }
