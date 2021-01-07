package com.example.demo.lock;

import com.example.demo.client.RedissonClientConfig;
import org.redisson.api.RBucket;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yansq
 * @date 2021/1/5
 */

public class RedisLock {


    public static boolean lock(String lockName, long timeOut, TimeUnit timeUnit) {
        RedissonClient client = RedissonClientConfig.getClient();
        RBucket<String> bucket = client.getBucket(lockName);
        return bucket.trySet("1", timeOut, timeUnit);
    }

    /**
     *  使用lua封装 exists && del
     * @param lockName 锁名
     * @return 是否成功释放锁
     */
    public static boolean unlock(String lockName) {
        String script = "if redis.call('exists',KEYS[1]) == 1 then " +
                "return redis.call('del',KEYS[1]) else return 0 end";
        RedissonClient client = RedissonClientConfig.getClient();
        Long eval = client.getScript().eval(RScript.Mode.READ_WRITE,
                script,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(lockName));
        System.out.println("eval--=====================" +eval);
        return Objects.equals(eval,1L);
    }

    public static int scriptTest(String key) {
        String s = "return redis.call('get',KEYS[1])";
        RedissonClient client = RedissonClientConfig.getClient();
        Object eval = client.getScript().eval(RScript.Mode.READ_ONLY,
                s,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(key));
//        boolean Integ = eval instanceof Integer;
//        System.out.println(Integ);
        return 1;
    }

    public static void main(String[] args) throws InterruptedException {
        String lockName = "cui:lock1";
        boolean cuiLock = lock(lockName, 300L, TimeUnit.SECONDS);
        System.out.println("=================== cuiLock" + cuiLock);
        TimeUnit.SECONDS.sleep(1L);
        boolean release = unlock(lockName);
        System.out.println("================= release" + release);
        boolean exists = RedissonClientConfig.getClient().getBucket(lockName).isExists();
        System.out.println(exists);
        int i = scriptTest(lockName);
        System.out.println(i);
        RedissonClientConfig.shutdown();
    }


}
