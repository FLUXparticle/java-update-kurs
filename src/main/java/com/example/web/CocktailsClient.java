package com.example.web;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.util.concurrent.*;

public class CocktailsClient {

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();

        // Erstellen des HTTP-Requests
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/cocktails"))
                .GET()
                .build();

        // Erstellen eines BodyHandler für die Flow-API
        var handler = BodyHandlers.fromLineSubscriber(new CocktailSubscriber(httpClient), CocktailSubscriber::getMilch, null);

        // Asynchronen Request senden und Flow API für die Verarbeitung nutzen
        CompletableFuture<HttpResponse<Integer>> futureResponse = httpClient.sendAsync(request, handler);
        HttpResponse<Integer> response = futureResponse.join();
        Integer milch = response.body();
        System.out.println("milch = " + milch);
    }

}

class CocktailSubscriber implements Flow.Subscriber<String> {

    private final HttpClient httpClient;

    private Flow.Subscription subscription;

    private CompletableFuture<Integer> futureMilch = CompletableFuture.completedFuture(0);

    public CocktailSubscriber(HttpClient httpClient) {
        this.httpClient = httpClient;
        System.out.println("CocktailSubscriber started...");
    }

    public int getMilch() {
        return futureMilch.join();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println(Thread.currentThread().getName() + " received: " + item);
        if (item.contains("Milk")) {
            String[] split = item.split(" ", 2);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/cocktails/" + split[0]))
                    .GET()
                    .build();

            var handler = BodyHandlers.fromLineSubscriber(new DetailsSubscriber(), DetailsSubscriber::getMilch, null);

            CompletableFuture<HttpResponse<Integer>> futureResponse = httpClient.sendAsync(request, handler);

            futureMilch = futureResponse.thenApply(HttpResponse::body).thenCombine(futureMilch, Integer::sum);
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("CocktailSubscriber completed.");
    }

}

class DetailsSubscriber implements Flow.Subscriber<String> {

    private Flow.Subscription subscription;

    private int milch;

    public DetailsSubscriber() {
        System.out.println("DetailsSubscriber started...");
    }

    public int getMilch() {
        return milch;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        if (item.contains("Milch")) {
            System.out.println(Thread.currentThread().getName() + " received: " + item);
            String[] split = item.split(" ", 2);
            milch += Integer.parseInt(split[0]);
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("DetailsSubscriber completed.");
    }

}
