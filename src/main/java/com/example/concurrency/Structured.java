import java.util.concurrent.*;
import java.util.function.*;

import static java.util.FormatProcessor.*;

long start = System.currentTimeMillis();

long last = 0;

void sleep(long millis, String name) {
    try {
        Thread.sleep(millis);
    } catch (InterruptedException e) {
        log(STR."\{name} Interrupted!");
        throw new RuntimeException(e);
    }
}

synchronized void log(String line) {
    long now = System.currentTimeMillis();
    if (now - last > 200) {
        System.out.println();
    }
    System.out.println(FMT."%.3f\{(now - start) / 1000.0}s: \{line}");
    last = now;
}

record Response(String user, Integer order) {}

String findUser() {
    sleep(500, "findUser");
    log("User not found");
    throw new RuntimeException("User");
//    return "Sven";
}

Integer fetchOrder() {
    sleep(1000, "fetchOrder");
    log("Order fetched");
    return 33;
}

ExecutorService esvc = ForkJoinPool.commonPool();

Response handleUnstructured() throws ExecutionException, InterruptedException {
    Future<String>  user  = esvc.submit(() -> findUser());
    Future<Integer> order = esvc.submit(() -> fetchOrder());
    log("Eins");
    String theUser  = user.get();   // Join findUser
    int    theOrder = order.get();  // Join fetchOrder
    log("Zwei");
    return new Response(theUser, theOrder);
}

Response handleStructured() throws InterruptedException, ExecutionException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        Supplier<String> user  = scope.fork(() -> findUser());
        Supplier<Integer> order = scope.fork(() -> fetchOrder());

        log("Eins");

        scope.join().throwIfFailed();

        log("Zwei");

        return new Response(user.get(), order.get());
    }
}

void main() {
    try {
        Response response = handleStructured();
        log(STR."response = \{response}");
    } catch (InterruptedException | ExecutionException e) {
        log(STR."error = \{e}");
    }
    sleep(1000, "main");
}
