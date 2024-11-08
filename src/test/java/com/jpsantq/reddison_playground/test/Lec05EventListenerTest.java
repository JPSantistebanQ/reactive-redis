package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

@Log4j2
class Lec05EventListenerTest extends BaseTest {

    @Test
    void expiredEventTest() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Sam", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();

        Mono<Void> event = bucket.addListener((ExpiredObjectListener) s -> log.info("Key: {} expired", s))
                .then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        // * Extend
        sleep(11);
    }
}
