package com.example.sonstiges;

public class HelpfulNPE {

    private static Integer foo;

    public static void main(String[] args) {
        int bar = foo;
        System.out.println(STR."bar = \{bar}");
    }

}
