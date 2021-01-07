package com.example.demo.pubsub;

import com.example.demo.client.RedissonClientConfig;
import com.example.demo.client.RedissonClientConfig2;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author yansq
 * @date 2021/1/6
 */
public class PublishOrder {
    private final String topicName;
    private PublishOrder(String topicName){
        this.topicName = topicName;
    }
    public static PublishOrder of(String topicName) {
        return new PublishOrder(topicName);
    }

    public void publish() {
        RedissonClient client = RedissonClientConfig.getClient();
        RTopic topic = client.getTopic(topicName);
        for (int i = 0; i<10;i++) {
            topic.publish("========== pub msg: " + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String topic = "cui:talk";
        SubscribeOrder.run(topic);
        PublishOrder publishOrder = PublishOrder.of(topic);
        publishOrder.publish();
        TimeUnit.SECONDS.sleep(100L);
        RedissonClientConfig.shutdown();
        RedissonClientConfig2.shutdown();
    }
}
