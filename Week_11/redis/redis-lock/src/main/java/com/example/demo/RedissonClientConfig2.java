package com.example.demo;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

/**
 * @author yansq
 * @date 2021/1/5
 */
public class RedissonClientConfig2 {

    private RedissonClientConfig2() {
    }

    public static RedissonClient getClient() {
        return RedissonConfigHolder.REDISSON_CLIENT;
    }

    public static void shutdown() {
        RedissonConfigHolder.REDISSON_CLIENT.shutdown();
    }

    private static class RedissonConfigHolder {
        private static final RedissonClient REDISSON_CLIENT = getClient();

        private static RedissonClient getClient() {
            Config config = new Config();
            config.setCodec(new StringCodec());
            config.useSentinelServers().addSentinelAddress(
                    "redis://192.168.252.40:26379",
                    "redis://192.168.252.40:26380",
                    "redis://192.168.252.40:26381"
            ).setMasterName("promotion_center");
            return Redisson.create(config);
        }
    }
}
