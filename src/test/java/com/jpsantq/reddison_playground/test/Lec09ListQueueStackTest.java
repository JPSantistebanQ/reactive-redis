package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.client.codec.LongCodec;
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
}
