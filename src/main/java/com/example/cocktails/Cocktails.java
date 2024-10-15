package com.example.cocktails;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.util.Map.Entry.*;
import static java.util.stream.Collectors.*;

public class Cocktails {

    public static void main(String[] args) throws IOException {

        Map<Integer, String> zutaten = Files.lines(Path.of("zutaten.txt"))
                .map(line -> line.split(","))
                .collect(toMap(
                        parts -> Integer.valueOf(parts[0]),
                        parts -> parts[1]
                ));

        Map<String, List<Integer>> rezepte = Files.lines(Path.of("rezepte.txt"))
                .map(line -> line.split(":"))
                .collect(toMap(
                        parts -> parts[0],
                        parts -> Arrays.stream(parts[1].split(","))
                                .map(Integer::valueOf)
                                .toList()
                ));

        zutaten.entrySet().stream()
                .sorted(comparingByKey())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        int eingabe = 70; // Himbeersirup

        // Rezepte mit dieser Zutat
        System.out.println();
        System.out.println("Rezepte mit dieser Zutat");
        rezepte.entrySet().stream().parallel()
                .filter(entry -> entry.getValue().contains(eingabe))
                .forEach(entry -> {
                    System.out.println(entry.getKey() + ":");
                    entry.getValue().forEach(zutatId -> System.out.println("  " + zutaten.get(zutatId)));
                });

        // Einkaufsliste
        System.out.println();
        System.out.println("Einkaufsliste:");
        rezepte.entrySet().stream()
                .filter(entry -> entry.getValue().contains(eingabe))
                .flatMap(entry -> entry.getValue().stream())
                .distinct()
                .map(zutaten::get)
                .sorted()
                .forEach(System.out::println);
    }

}
