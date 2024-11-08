package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
class Lec04BucketAsMapTest extends BaseTest {

    @Test
    void bucketsAsMap() {
        Mono<Void> mono = client.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:2:name", "user:3:name")
                .doOnNext(i -> log.info("Value: {}", i))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
