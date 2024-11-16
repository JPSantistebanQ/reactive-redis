package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
class Lec14TransactionTest extends BaseTest {

    private RBucketReactive<Long> bucket;
    private RBucketReactive<Long> bucket2;

    @BeforeAll
    void accountSetup() {
        bucket = client.getBucket("user:1:balance", LongCodec.INSTANCE);
        bucket2 = client.getBucket("user:2:balance", LongCodec.INSTANCE);

        Mono<Void> mono = bucket.set(100L)
                .then(bucket2.set(0L))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @AfterAll
    void accountBalanceStatus() {
        Mono<Void> mono = Flux.zip(bucket.get(), bucket2.get())
                .doOnNext(tuple -> log.info("User 1: {}, User 2: {}", tuple.getT1(), tuple.getT2()))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    void nonTransactionTest() {
        transfer(bucket, bucket2, 50)
                .thenReturn(0)
                .map(i -> 5 / i)
                .subscribe(); // * Forcing an error

        sleep(5);
    }

    private Mono<Void> transfer(RBucketReactive<Long> from, RBucketReactive<Long> to, int amount) {
        return Flux.zip(from.get(), to.get())
                .filter(tuple -> tuple.getT1() >= amount)
                .flatMap(tuple -> from.set(tuple.getT1() - amount).thenReturn(tuple))
                .flatMap(tuple -> to.set(tuple.getT2() + amount))
                .then();
    }

    @Test
    void transactionTest() {
        RTransactionReactive transaction = client.createTransaction(TransactionOptions.defaults());
        RBucketReactive<Object> bucketT1 = transaction.getBucket("user:1:balance", LongCodec.INSTANCE);
        RBucketReactive<Object> bucketT2 = transaction.getBucket("user:1:balance", LongCodec.INSTANCE);

        transfer(bucketT1, bucketT2, 50)
                .thenReturn(0)
                .map(i -> 5 / i)
                .then(transaction.commit())
                .doOnError(ex -> transaction.rollback())
                .subscribe(); // * Forcing an error

        sleep(5);
    }
}
