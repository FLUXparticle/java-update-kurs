package com.example.stream;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Streams {

    public static void main(String[] args) throws IOException {

        Path path = Paths.get("Geschichte.txt");

        Files.lines(path) // Stream<String>
                .flatMap(line -> {
                    if (line.isEmpty()) {
                        return Stream.empty();
                    }
                    String[] split = line.split(" ");
                    return Stream.of(split);
                }) // Stream<String>
                .forEach(System.out::println);

        var list = List.of(1, 2, 3, 4, 5);
        Optional.of(list)
                .flatMap(l -> l.stream().reduce(Integer::sum));


        var y = Optional.<Integer>ofNullable(42)
                .filter(value -> value % 2 == 0)
                .flatMap(x -> {
                    if (x > 40) {
                        return Optional.empty();
                    }
                    return Optional.of(x + 1);
                })
                .orElse(0);

        System.out.println("y = " + y);
    }

}
