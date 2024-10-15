package com.example.web;

import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.example.web.HttpUtils.*;

public class CocktailClientWithRxJava {

    public static void main(String[] args) {
        time(CocktailClientWithRxJava::run);
    }

    public static void run() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        try {
            // Cocktails abrufen
            Observable<String> cocktailIds = HttpUtilsRxJava.getResponseLinesRx("http://localhost:8080/cocktails")
                    .filter(line -> line.toLowerCase().contains("milk"))
                    .map(line -> line.split(" ")[0]);

            // Zutatensummen abrufen und addieren
            int totalMilk = cocktailIds
                    .flatMap(id -> {
                        System.out.println("id = " + id);
                        return Observable.fromFuture(
                                executor.submit(() -> getMilkAmount(id))
                        );
                    })
                    .reduce(Integer::sum)
                    .blockingGet();

            // Ergebnis ausgeben
            System.out.println("Total amount of milk in all cocktails: " + totalMilk + " CL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Executor herunterfahren
        executor.shutdown();
    }

    // Methode, um die Menge an Milch in einem Cocktail zu ermitteln
    private static int getMilkAmount(String cocktailId) throws Exception {
        return HttpUtilsRxJava.getResponseLinesRx("http://localhost:8080/cocktails/" + cocktailId)
                .map(line -> line.split(" "))
                .filter(parts -> parts[1].equalsIgnoreCase("milch"))
                .map(parts -> Integer.parseInt(parts[0]))
                .blockingFirst(); // Blockiert, bis der erste Eintrag gefunden wurde
    }

}