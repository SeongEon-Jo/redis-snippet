package com.redis.demo.lock;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StatefulLockKey {
    private String key;
    private LocalDateTime updatedAt;
    private LockStatus lockStatus;

    public static StatefulLockKey ofCounting(String key) {
        return new StatefulLockKey(
                key,
                LocalDateTime.now(),
                LockStatus.COUNTING
        );
    }

    public static StatefulLockKey ofFailed(String key) {
        return new StatefulLockKey(
                key,
                LocalDateTime.now(),
                LockStatus.FAILED
        );
    }

}
