package com.redis.demo.lock;

import com.redis.demo.domain.RaceResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


public class StatefulLockServiceTest extends TestContainerBasedTest {
    @Autowired
    private StatefulLockService statefulLockService;
    @Autowired
    private RedisTemplate<String, StatefulLockKey> redisTemplate;
    private final static int threadCount = 32;

    private final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    @Test
    void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        String key = "key";
        redisTemplate.opsForValue().set(key, StatefulLockKey.ofFailed(key));
        RaceResource raceResource = new RaceResource();
        StatefulLockKey statefulLockKey = StatefulLockKey.ofCounting(key);

        for (int i = 0; i < 1024; i++) {
            executorService.submit(() -> {
                statefulLockService.lock(statefulLockKey, raceResource);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        assertThat(raceResource.getCount()).isEqualTo(1);
    }
}