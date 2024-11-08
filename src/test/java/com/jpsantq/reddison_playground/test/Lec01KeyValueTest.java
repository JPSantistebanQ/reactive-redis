package com.jpsantq.reddison_playground.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec01KeyValueTest extends BaseTest {

    @Test
    public void keyValueAccessTest() {
        RBucketReactive<String> bucket = client.getBucket("user:1:name");
        Mono<Void> set = bucket.set("John");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
