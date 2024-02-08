import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.*;
import java.util.concurrent.*;

void main() {
    try (HttpClient httpClient = HttpClient.newHttpClient()) {
        // Erstellen des HTTP-Requests
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/cocktails"))
                .GET()
                .build();

        // Erstellen eines BodyHandler für die Flow-API
        var handler = BodyHandlers.fromLineSubscriber(new CocktailSubscriber(httpClient), CocktailSubscriber::getCount, null);

        // Asynchronen Request senden und Flow API für die Verarbeitung nutzen
        CompletableFuture<HttpResponse<Integer>> futureResponse = httpClient.sendAsync(request, handler);
        HttpResponse<Integer> response = futureResponse.join();
        Integer count = response.body();
        System.out.println(STR."count = \{count}");
    }
}

static class CocktailSubscriber implements Flow.Subscriber<String> {

    private final HttpClient httpClient;

    private Flow.Subscription subscription;

    private int count;

    public CocktailSubscriber(HttpClient httpClient) {
        this.httpClient = httpClient;
        System.out.println("CocktailSubscriber started...");
    }

    public int getCount() {
        return count;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println(STR."Thread \{Thread.currentThread().getName()} received: \{item}");

        count++;

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
