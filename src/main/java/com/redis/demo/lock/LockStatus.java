package com.redis.demo.lock;

public enum LockStatus {
    COUNTING, FAILED, COMPLETED;

    public boolean isFailed() {
        return this.equals(LockStatus.FAILED);
    }
}
