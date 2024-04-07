package com.redis.demo.lock;

import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentExecutor {
    private static final int threadCount = 32;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    public static void execute(Runnable runnable) {
        try {
            int executeCount = 1024;
            CountDownLatch countDownLatch = new CountDownLatch(executeCount);

            for (int i = 0; i < executeCount; i++) {
                executorService.submit(() -> {
                    runnable.run();
                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();
        } catch (Exception ignored) {

        }
    }
}
