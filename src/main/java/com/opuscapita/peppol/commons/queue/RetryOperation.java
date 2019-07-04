package com.opuscapita.peppol.commons.queue;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryOperation {

    public static void start(RetryableOperation op) throws Exception {
        RetryOperation.start(op, 5, 1000);
    }

    public static void start(RetryableOperation op, Integer tryCount) throws Exception {
        RetryOperation.start(op, tryCount, 1000);
    }

    public static void start(RetryableOperation op, Integer tryCount, Integer cooldown) throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        Exception exception;
        do {
            try {
                op.run();
                return;
            } catch (Exception e) {
                exception = e;
                try {
                    Thread.sleep(cooldown);
                } catch (InterruptedException ignored) {
                }
            }
        } while (i.incrementAndGet() < tryCount);

        throw exception;
    }

    @FunctionalInterface
    public interface RetryableOperation {
        public void run() throws Exception;
    }
}
