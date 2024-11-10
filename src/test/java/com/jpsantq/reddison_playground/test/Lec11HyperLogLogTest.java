package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

@Log4j2
class Lec11HyperLogLogTest extends BaseTest {

    @Test
    void count() {
        RHyperLogLogReactive<Long> counter = client.getHyperLogLog("user:visits", LongCodec.INSTANCE);

        List<Long> longList = LongStream.rangeClosed(1, 25)
                .boxed()
                .toList();

        StepVerifier.create(counter.addAll(longList).then())
                .verifyComplete();

        counter.count()
                .doOnNext(log::info)
                .subscribe();
    }
}
