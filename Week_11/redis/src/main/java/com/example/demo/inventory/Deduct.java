package com.example.demo.inventory;

import com.example.demo.client.RedissonClientConfig2;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

/**
 * @author yansq
 * @date 2021/1/6
 */
public class Deduct {

    private int count;
    private boolean isSuccess;

    private final long productId;
    private long inventoryCount;
    private static String cacheKey = "in:%d";

    private Deduct(int count, long productId) {
        this.count = count;
        this.productId = productId;
    }
    private Deduct(long inventoryCount, long productId) {
        this.inventoryCount = inventoryCount;
        this.productId = productId;
    }

    public static Deduct of(int count, long productId) {
        return new Deduct(count, productId);
    }
    public static Deduct of(long inventoryCount, long productId) {
        return new Deduct(inventoryCount, productId);
    }

    public boolean deductInventory(){
        RedissonClient client = RedissonClientConfig2.getClient();
        RAtomicLong atomicLong = client.getAtomicLong(String.format(cacheKey, productId));
        long andAdd = atomicLong.getAndAdd(-count);
        if (andAdd < count) {
            atomicLong.addAndGet(count);
            return false;
        }
        return true;
    }

    public void initInventory() {
        RedissonClient client = RedissonClientConfig2.getClient();
        RAtomicLong atomicLong = client.getAtomicLong(String.format(cacheKey, productId));
        atomicLong.set(inventoryCount);
    }


    public static void main(String[] args) {
        long productId = 10000001L;
        // 初始化商品库存
        Deduct.of(100L, productId).initInventory();

        Deduct deduct = Deduct.of(10, productId);
        for (int i = 1; i<12; i++) {
            boolean b = deduct.deductInventory();
            System.out.println("=============第"+ i+"次扣减, 扣减结果：" + b);
        }
    }

}
