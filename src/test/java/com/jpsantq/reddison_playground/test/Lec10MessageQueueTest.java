package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@Log4j2
class Lec10MessageQueueTest extends BaseTest {

    private RBlockingDequeReactive<Long> blockingDeque;

    @BeforeAll
    void setupQueue() {
        blockingDeque = client.getBlockingDeque("message-queue", LongCodec.INSTANCE);
    }

    @Test
    void consumer1() {
        blockingDeque.takeLastElements()
                .doOnNext(log::info)
                .doOnError(log::error)
                .subscribe();

        sleep(60);
    }

    @Test
    void consumer2() {
        blockingDeque.takeLastElements()
                .doOnNext(i -> log.info("Consuming 2: {}", i))
                .doOnError(log::error)
                .subscribe();

        sleep(60);
    }

    @Test
    void producer() {
        Mono<Void> mono = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(100))
                .doOnNext(i -> log.info("Pushing: {}", i))
                .flatMap(i -> blockingDeque.add(Long.valueOf(i)))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }
}
