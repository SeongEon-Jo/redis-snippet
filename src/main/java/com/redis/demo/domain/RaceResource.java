package com.redis.demo.domain;

public class RaceResource {
    private int count;

    public RaceResource() {
        this.count = 0;
    }

    public int getCount() {
        return count;
    }

    public void increaseCount() {
        this.count += 1;
    }

}
