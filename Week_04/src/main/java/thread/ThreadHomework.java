package thread;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好
 * @author yansq
 * @date 2020/11/10
 */
public class ThreadHomework {
    private static int res;

    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        // 1.submit a callable
        int i = useFuture();
        System.out.println(i);
        // 2.use runnable , countDownLatch, static var
        CountDownLatch latch = new CountDownLatch(1);
        int i1 = useRunnable(latch);
        System.out.println(i1);

        // 3.use callable
        CountDownLatch latch2 = new CountDownLatch(1);
        int i2 = useCallable(latch2);
        System.out.println(i2);

        // 4.use forkJoinPool
        int i3 = useForkJoinPool();
        System.out.println(i3);
        int i4 = useCyclicBarrier();
        System.out.println(i4);

        System.out.println("method count: " + count.get());
    }



    private static int useFuture() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future =  executorService.submit(()-> ThreadHomework.getResult(1));
        executorService.shutdown();
        return future.get();
    }

    private static int useRunnable(CountDownLatch latch) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                res = ThreadHomework.getResult(2);
            } finally {
                latch.countDown();
            }
        };
        runnable.run();
        latch.await();
        return res;
    }

    private static int useCallable(CountDownLatch latch) throws Exception {
        Callable<Integer> callable = () -> ThreadHomework.getResult(3);
        return callable.call();
    }

    private static int useForkJoinPool() throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(1);
        ForkJoinTask<Integer> forkJoinTask = pool.submit(() -> ThreadHomework.getResult(4));
        return forkJoinTask.get();
    }

    private static int useCyclicBarrier() throws BrokenBarrierException, InterruptedException {
        // 赋值给外面的res
        Runnable runnable = () -> res = getResult(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, runnable);
        cyclicBarrier.await();
        return res;
    }


    private static int getResult(int y) {
        count.incrementAndGet();
        return fibo(y);
    }
    private static int fibo(int y) {
        if ( y < 2) {
            return 1;
        }
        return fibo(y-1) + fibo(y-2);
    }
}
