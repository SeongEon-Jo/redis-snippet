package com.redis.demo.lock;

import com.redis.demo.domain.RaceResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class LockServiceTest extends TestContainerBasedTest {

    @Autowired
    private LockService lockService;

    @Test
    void test() {
        // given
        RaceResource raceResource = new RaceResource();
        LockKey key = new LockKey("key");

        // when
        ConcurrentExecutor.execute(() -> lockService.lock(key, raceResource));

        // then
        assertThat(raceResource.getCount()).isEqualTo(1);
    }
}