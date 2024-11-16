package com.jpsantq.reddison_playground.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

@Log4j2
class Lec12PubSubTest extends BaseTest {

    @Test
    void subscriber1() {
        RTopicReactive topic = client.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnNext(log::info)
                .doOnError(log::error)
                .subscribe();
        sleep(60);
    }

    @Test
    void subscriber2() {
        RTopicReactive topic = client.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnNext(log::info)
                .doOnError(log::error)
                .subscribe();
        sleep(60);
    }
}
