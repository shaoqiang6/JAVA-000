package com.example.demo.pubsub;

import com.example.demo.RedissonClientConfig2;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @author yansq
 * @date 2021/1/6
 */
public class SubscribeOrder {

    public static void main(String[] args) {
        String topicName = "cui:channel";
        run(topicName);
    }
    public static void run(final String topicName) {
        Runnable runner = () -> {

            RedissonClient client = RedissonClientConfig2.getClient();
            RTopic topic = client.getTopic(topicName);
            topic.addListener(String.class, (channel, msg) -> System.out.println("========receive msg " + msg));
        };
        runner.run();
    }
}
