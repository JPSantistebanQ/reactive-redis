package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
class Lec13BatchTest extends BaseTest {

    @Test
        // * 7.6 seg
    void batchTest() {
        RBatchReactive batch = client.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);

        for (long i = 0; i < 20_000; i++) {
            list.add(i);
            set.add(i);
        }

        StepVerifier.create(batch.execute().then())
                .verifyComplete();
    }

    @Test
        // * 26seg
    void regularTest() {

        RListReactive<Long> list = client.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = client.getSet("numbers-set", LongCodec.INSTANCE);

        Mono<Void> mono = Flux.range(1, 20_000)
                .map(Long::valueOf)
                .flatMap(i -> list.add(i).then(set.add(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
