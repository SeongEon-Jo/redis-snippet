package com.redis.demo.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RaceResourceService {

    public void increaseCount(RaceResource raceResource) {
        raceResource.increaseCount();
    }
}
