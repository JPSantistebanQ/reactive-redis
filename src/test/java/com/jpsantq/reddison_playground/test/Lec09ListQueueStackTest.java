package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

@Log4j2
class Lec09ListQueueStackTest extends BaseTest {

    @Test
    void listTest() {
        RListReactive<Long> list = client.getList("number-input", LongCodec.INSTANCE);

        List<Long> longStream = LongStream.rangeClosed(1, 10)
                .boxed()
                .toList();

        StepVerifier.create(list.addAll(longStream).then())
                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    void queueTest() {
        RQueueReactive<Long> queue = client.getQueue("number-input", LongCodec.INSTANCE);

        Mono<Void> queuePool = queue.poll()
                .repeat(3)
                .doOnNext(log::info)
                .then();

        StepVerifier.create(queuePool)
                .verifyComplete();

        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();
    }

    @Test
    void stackTest() {
        RDequeReactive<Long> deque = client.getDeque("number-input", LongCodec.INSTANCE);

        Mono<Void> queuePool = deque.pollLast()
                .repeat(3)
                .doOnNext(log::info)
                .then();

        StepVerifier.create(queuePool)
                .verifyComplete();

        StepVerifier.create(deque.size())
                .expectNext(6)
                .verifyComplete();
    }
}
