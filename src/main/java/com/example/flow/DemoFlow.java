import java.util.concurrent.*;
import java.util.concurrent.Flow.*;

void main() {
    // Erstellen eines Processors, der die empfangenen Zahlen verdoppelt
    DoubleProcessor doubleProcessor = new DoubleProcessor();

    // Erstellen eines Subscribers, der die verdoppelten Zahlen ausgibt
    PrintSubscriber printSubscriber = new PrintSubscriber();

    // Erstellen eines SubmissionPublisher mit einem Standard-Puffer
    try (SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()) {
        // Verbinden der Publisher, des Processors und des Subscribers
        publisher.subscribe(doubleProcessor);
        doubleProcessor.subscribe(printSubscriber);

        // Produktion der Zahlen 1 bis 5
        for (int i = 1; i <= 5; i++) {
            publisher.submit(i);
            System.out.println(STR."Thread \{Thread.currentThread().getName()} submitted: \{i}");
        }

    }

    printSubscriber.join();
}

static class DoubleProcessor extends SubmissionPublisher<Integer> implements Processor<Integer, Integer> {

    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        submit(item * 2); // Verdoppeln und weiterleiten
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        close(); // Schließen des Processors nach Abschluss
    }
}

// Implementierung eines Subscribers, der die empfangenen Zahlen ausgibt
static class PrintSubscriber implements Flow.Subscriber<Integer> {

    private Subscription subscription;

    private final CompletableFuture<Void> completableFuture = new CompletableFuture<>();

    public void join() {
        completableFuture.join();
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        System.out.println(STR."Thread \{Thread.currentThread().getName()} received: \{item}");
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        completableFuture.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("Processing completed.");
        completableFuture.complete(null);
    }
}