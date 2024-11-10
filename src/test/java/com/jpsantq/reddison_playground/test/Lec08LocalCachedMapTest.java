package com.jpsantq.reddison_playground.test;

import com.jpsantq.reddison_playground.test.config.RedissonConfig;
import com.jpsantq.reddison_playground.test.dto.Student;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Log4j2
class Lec08LocalCachedMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> students;

    @BeforeAll
    void setupClient() {
        RedissonConfig redissonConfig = new RedissonConfig();
        RedissonClient client = redissonConfig.getClient();

        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer, Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE);
        students = client.getLocalCachedMap("students", new TypedJsonJacksonCodec(Integer.class, Student.class), mapOptions);
    }

    @Test
    void appServer1() {
        Student student = new Student("John", 25, "New York", List.of(90, 95, 100));
        Student student2 = new Student("John", 25, "New York", List.of(90, 95, 100));

        students.put(1, student);
        students.put(2, student2);

        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(i -> log.info("Student: {}", i))
                .subscribe();

        sleep(6);
    }


    @Test
    void appServer2() {
        Student student = new Student("John_Updated", 25, "New York", List.of(90, 95, 100));

        students.put(1, student);

    }
}
