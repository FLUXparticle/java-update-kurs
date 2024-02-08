import java.time.*;
import java.util.concurrent.*;

import static java.util.FormatProcessor.*;

import java.util.concurrent.atomic.AtomicInteger;

// Atomic integer containing the next thread ID to be assigned
private static final AtomicInteger nextId = new AtomicInteger(0);

// Thread local variable containing each thread's ID
private static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial(nextId::getAndIncrement);

// Returns the current thread's unique ID, assigning it if necessary
public static int getThreadId() {
    return threadId.get();
}

long start = System.currentTimeMillis();

long last = 0;

void sleep(long millis) {
    try {
        Thread.sleep(millis);
    } catch (InterruptedException e) {
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

void main() {
//    try (ExecutorService service = Executors.newFixedThreadPool(10)) {
    try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < 25; i++) {
            String task = FMT."Task %02d\{i}";
            service.submit(() -> {
                log(STR."\{task} --> \{Thread.currentThread()} (\{getThreadId()})");
                sleep(1000);
                log(STR."\{task} <-- \{Thread.currentThread()} (\{getThreadId()})");
            });
            sleep(20);
        }
    }

    log("Ende");
}
