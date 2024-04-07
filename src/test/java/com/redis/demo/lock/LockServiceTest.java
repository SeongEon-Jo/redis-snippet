package com.redis.demo.lock;

import com.redis.demo.domain.RaceResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class LockServiceTest extends TestContainerBasedTest {

    @Autowired
    private LockService lockService;

    private final static int threadCount = 32;

    private final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    @Test
    void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        RaceResource raceResource = new RaceResource();
        LockKey key = new LockKey("key");

        for (int i = 0; i < 1024; i++) {
            executorService.submit(() -> {
                lockService.lock(key, raceResource);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        assertThat(raceResource.getCount()).isEqualTo(1);
    }
}