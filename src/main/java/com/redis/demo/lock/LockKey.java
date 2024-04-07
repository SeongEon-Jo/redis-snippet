package com.redis.demo.lock;

public class LockKey {

    private final String key;

    public LockKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
