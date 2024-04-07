package com.redis.demo.lock;

import com.redis.demo.domain.RaceResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;


public class StatefulLockServiceTest extends TestContainerBasedTest {

    @Autowired
    private StatefulLockService statefulLockService;
    @Autowired
    private RedisTemplate<String, StatefulLockKey> redisTemplate;

    @Test
    void test() {
        // given
        String key = "key";
        redisTemplate.opsForValue().set(key, StatefulLockKey.ofFailed(key));
        RaceResource raceResource = new RaceResource();
        StatefulLockKey statefulLockKey = StatefulLockKey.ofCounting(key);

        // when
        ConcurrentExecutor.execute(() -> statefulLockService.lock(statefulLockKey, raceResource));

        // then
        assertThat(raceResource.getCount()).isEqualTo(1);
    }
}