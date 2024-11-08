package com.jpsantq.reddison_playground.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

@Slf4j
class Lec01KeyValueTest extends BaseTest {

    @Test
    void keyValueAccessTest() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("John");
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    void keyValueExpireTest() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Sam", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }


    @Test
    void keyValueExtendExpiryTest() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("Sam", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(log::info)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        // * Extend
        sleep(5);
        Mono<Boolean> mono = bucket.expire(60, TimeUnit.SECONDS);
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();

        // * Acces expiration time
        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(time -> log.info("Time to live: {}", time))
                .then();
        StepVerifier.create(ttl)
                .verifyComplete();
    }
}
