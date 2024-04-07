package com.redis.demo.lock;

import com.redis.demo.domain.RaceResource;
import com.redis.demo.domain.RaceResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.TRUE;

@Component
@RequiredArgsConstructor
public class LockService {
    private final RaceResourceService raceResourceService;
    private final RedisTemplate<String, LockKey> redisTemplate;

    public void lock(LockKey key, RaceResource raceResource) {
        try {
            if (TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key.getKey(), key))) {
                System.out.println("lock success");
                raceResourceService.increaseCount(raceResource);
            }
        } finally {
        }
    }
}
