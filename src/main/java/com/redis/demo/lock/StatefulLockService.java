package com.redis.demo.lock;

import com.redis.demo.domain.RaceResource;
import com.redis.demo.domain.RaceResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatefulLockService {
    private final RedisTemplate<String, StatefulLockKey> redisTemplate;
    private final RaceResourceService raceResourceService;

    public void lock(StatefulLockKey key, RaceResource raceResource) {
        try {
            boolean isLocked = lockOnlyStatusIsFailed(key);
            if (isLocked) {
                System.out.println("lock success");
                raceResourceService.increaseCount(raceResource);
            }
        } catch (Exception ignored) {
        }
    }

    private boolean lockOnlyStatusIsFailed(StatefulLockKey key) {
        SessionCallback<Object> lockIfFailed = new SessionCallback<>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key.getKey());

                StatefulLockKey statefulLockKey = (StatefulLockKey) operations.opsForValue().get(key.getKey());

                if (statefulLockKey != null && statefulLockKey.getLockStatus().isFailed()) {
                    operations.multi();
                    redisTemplate.opsForValue().set(key.getKey(), StatefulLockKey.ofCounting(key.getKey()));
                    return !operations.exec().isEmpty();
                }

                return false;
            }
        };

        return Boolean.TRUE.equals(redisTemplate.execute(lockIfFailed));
    }

}
