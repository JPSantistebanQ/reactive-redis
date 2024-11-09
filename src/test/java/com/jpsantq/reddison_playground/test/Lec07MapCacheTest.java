package com.jpsantq.reddison_playground.test;

import com.jpsantq.reddison_playground.test.dto.Student;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
class Lec07MapCacheTest extends BaseTest {

    @Test
    void mapCacheTest() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> bucket = client.getMapCache("users:cache", codec);
        Student student = new Student("John", 25, "New York", List.of(90, 95, 100));
        Student student2 = new Student("John", 25, "New York", List.of(90, 95, 100));

        Mono<Student> name = bucket.put(1, student, 10, TimeUnit.SECONDS);
        Mono<Student> name2 = bucket.put(2, student2, 15, TimeUnit.SECONDS);

        StepVerifier.create(name.then(name2).then())
                .verifyComplete();

        sleep(3);

        // * Access Student 1 and 2
        bucket.get(1)
                .doOnNext(log::info)
                .subscribe();
        bucket.get(2)
                .doOnNext(log::info)
                .subscribe();

        sleep(10);
        // * Access Student 1 and 2
        bucket.get(1)
                .doOnNext(log::info)
                .subscribe();
        bucket.get(2)
                .doOnNext(log::info)
                .subscribe();
    }
}
