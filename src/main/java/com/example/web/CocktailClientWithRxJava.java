package com.example.web;

import io.reactivex.rxjava3.annotations.*;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.web.HttpUtils.*;

public class CocktailClientWithRxJava {

    public static void main(String[] args) {
        time(CocktailClientWithRxJava::run);
    }

    public static void run() {
        try {
            // Cocktails abrufen
            Observable<String> cocktailIds = HttpUtilsRxJava.getResponseLinesRx("http://localhost:8080/cocktails")
                    .filter(line -> line.toLowerCase().contains("milk"))
                    .map(line -> line.split(" ")[0]);

            // Zutatensummen abrufen und addieren
            int totalMilk = cocktailIds
                    .flatMap(id -> {
                        System.out.println("id = " + id);
                        return getMilkAmount(id);
                    })
                    .reduce(Integer::sum)
                    .blockingGet();

            // Ergebnis ausgeben
            System.out.println("Total amount of milk in all cocktails: " + totalMilk + " CL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Methode, um die Menge an Milch in einem Cocktail zu ermitteln
    private static Observable<Integer> getMilkAmount(String cocktailId) throws Exception {
        return HttpUtilsRxJava.getResponseLinesRx("http://localhost:8080/cocktails/" + cocktailId)
                .map(line -> line.split(" "))
                .filter(parts -> parts[1].equalsIgnoreCase("milch"))
                .map(parts -> Integer.parseInt(parts[0]))
                .subscribeOn(Schedulers.io())
                .firstElement()
                .toObservable();
    }

}