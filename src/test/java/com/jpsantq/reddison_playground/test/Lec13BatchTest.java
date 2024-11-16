package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

@Log4j2
class Lec13BatchTest extends BaseTest {

    @Test
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
}
