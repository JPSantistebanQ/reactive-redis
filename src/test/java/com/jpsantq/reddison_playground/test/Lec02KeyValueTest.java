package com.jpsantq.reddison_playground.test;

import com.jpsantq.reddison_playground.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

class Lec02KeyValueTest extends BaseTest {

    @Test
    void keyValueObjectTest() {
        Student student = new Student("John", 25, "New York", Arrays.asList(90, 95, 100));

        //RBucketReactive<Student> bucket = client.getBucket("student:1", JsonJacksonCodec.INSTANCE);
        RBucketReactive<Student> bucket = client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
