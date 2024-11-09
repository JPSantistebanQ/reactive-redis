package com.jpsantq.reddison_playground.test;

import com.jpsantq.reddison_playground.test.dto.Student;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

@Log4j2
class Lec06MapTest extends BaseTest {

    @Test
    void ampTest() {
        RMapReactive<String, String> bucket = client.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = bucket.put("name", "Sam");
        Mono<String> age = bucket.put("age", "10");
        Mono<String> city = bucket.put("city", "Lima");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    void ampTest2() {
        RMapReactive<String, String> bucket = client.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> map = Map.of(
                "name", "Sam",
                "age", "10",
                "city", "Lima"
        );

        Mono<Void> name = bucket.putAll(map);

        StepVerifier.create(name.then())
                .verifyComplete();
    }

    @Test
    void ampTest3() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> bucket = client.getMap("users", codec);
        Student student = new Student("John", 25, "New York", List.of(90, 95, 100));

        Mono<Student> name = bucket.put(1, student);

        StepVerifier.create(name.then())
                .verifyComplete();
    }
}
