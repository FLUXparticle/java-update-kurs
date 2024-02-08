package com.example.mini;

public class HelloWorld {

    private static String name = "World";

    private static String greeting() {
        return "Hello " + name + "!";
    }

    public static void main(String[] args) {
        System.out.println(greeting());
    }

}
