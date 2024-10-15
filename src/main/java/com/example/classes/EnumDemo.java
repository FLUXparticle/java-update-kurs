package com.example.classes;

public class EnumDemo {

    public static void main(String[] args) {

        Enum e = Enum.valueOf("ONE");

        switch (e) {
            case ONE -> System.out.println("ONE");
            case TWO -> {
                System.out.println("TWO");
            }
            case THREE -> {
                System.out.println("THREE");
            }
            default -> System.out.println("default");
        }

        String s = switch (e) {
            case ONE -> "ONE";
            case TWO -> "TWO";
            case THREE -> "THREE";
        };

        int x;
        if (e == Enum.ONE) {
            x = 1;
        } else {
            x = 2;
        }

        int y = (e == Enum.ONE) ? 1 : 2;

        int j = switch (e) {
            case ONE  -> 0;
            case TWO -> 1;
            default      -> {
                int k = e.ordinal();
                yield k;
            }
        };

    }

}
