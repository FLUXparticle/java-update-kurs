package com.example.web;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static com.example.web.HttpUtils.*;

public class CocktailClientWithCompletable {

    public static void main(String[] args) throws Exception {
        time(() -> {
            // 1. Cocktails mit "Milk" im Namen abrufen
            Stream<String> cocktailIds = getCocktailsWithMilk();

            // 2. Die Menge an Milch in diesen Cocktails addieren
            List<CompletableFuture<Integer>> futures = cocktailIds
                    .map(id -> CompletableFuture.supplyAsync(() -> getMilkAmount(id)))
                    .toList();

            int totalMilk = 0;
            for (CompletableFuture<Integer> future : futures) {
                try {
                    totalMilk += future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // 3. Ergebnis ausgeben
            System.out.println("Total amount of milk in all cocktails: " + totalMilk + " CL");
        });
    }

    // Methode, um alle Cocktails mit "Milk" im Namen zu finden
    private static Stream<String> getCocktailsWithMilk() {
        // Abruf der Cocktails als Stream von Zeilen
        try {
            return HttpUtils.getResponseLines("http://localhost:8080/cocktails")
                    .filter(line -> line.contains("Milk")) // Filtert die Cocktails mit "Milk" im Namen
                    .map(line -> line.split(" ")[0]);  // Extrahiert die ID
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Methode, um die Menge an Milch in einem Cocktail zu ermitteln
    private static int getMilkAmount(String cocktailId) {
        try (Stream<String> lines = HttpUtils.getResponseLines("http://localhost:8080/cocktails/" + cocktailId)) {
            // Summiert die Menge an Milch für diesen Cocktail
            return lines
                    .filter(line -> line.contains("Milch"))
                    .map(line -> line.split(" "))
                    .map(parts -> Integer.valueOf(parts[0]))
                    .findFirst()
                    .orElse(0);
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Milchmenge für Cocktail ID: " + cocktailId);
            return 0;  // Im Fehlerfall 0 zurückgeben
        }
    }

}