
# 试验对比以及结论

1. 不设置-Xms对比设置（-Xms512m -Xmx512m）

现象：不设置会造成过早触发Full GC ；并且整个过程触发更多次的Full GC
结论：一般尽量设置（-Xms等于-Xmx）这样在初始化之后就会有比较充足的堆内存，有效减少Full GC 甚至GC频率

2. 设置不同Xmx大小（128m/256m/512m/1024m）
现象1：过小时（128m）触发多次FullGC 后，出现 堆内存溢出，程序终止。而且每次Full GC后内堆内存几乎没有变化，越临近溢出越明显

```

2020-10-28T22:15:48.037+0800: [GC (Allocation Failure) [PSYoungGen: 38333K->5112K(19968K)] 103201K->80826K(107520K), 0.0040554 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T22:15:48.041+0800: [Full GC (Ergonomics) [PSYoungGen: 5112K->0K(19968K)] [ParOldGen: 75713K->74843K(87552K)] 80826K->74843K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0184894 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
2020-10-28T22:15:48.064+0800: [Full GC (Ergonomics) [PSYoungGen: 14721K->0K(19968K)] [ParOldGen: 74843K->79923K(87552K)] 89564K->79923K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0125033 secs] [Times: user=0.11 sys=0.00, real=0.01 secs]
2020-10-28T22:15:48.080+0800: [Full GC (Ergonomics) [PSYoungGen: 14713K->0K(19968K)] [ParOldGen: 79923K->83924K(87552K)] 94636K->83924K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0148997 secs] [Times: user=0.09 sys=0.00, real=0.01 secs]
2020-10-28T22:15:48.098+0800: [Full GC (Ergonomics) [PSYoungGen: 14847K->450K(19968K)] [ParOldGen: 83924K->87517K(87552K)] 98772K->87967K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0197069 secs] [Times: user=0.11 sys=0.00, real=0.02 secs]
...省略中间几次
2020-10-28T22:15:48.270+0800: [Full GC (Ergonomics) [PSYoungGen: 14842K->14596K(19968K)] [ParOldGen: 87024K->87024K(87552K)] 101867K->101621K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0018260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T22:15:48.272+0800: [Full GC (Ergonomics) [PSYoungGen: 14801K->14314K(19968K)] [ParOldGen: 87024K->87024K(87552K)] 101825K->101338K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0014913 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T22:15:48.274+0800: [Full GC (Ergonomics) [PSYoungGen: 14407K->14407K(19968K)] [ParOldGen: 87024K->87024K(87552K)] 101431K->101431K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0016414 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T22:15:48.275+0800: [Full GC (Allocation Failure) [PSYoungGen: 14407K->14407K(19968K)] [ParOldGen: 87024K->87005K(87552K)] 101431K->101412K(107520K), [Metaspace: 2625K->2625K(1056768K)], 0.0235373 secs] [Times: user=0.19 sys=0.00, real=0.02 secs]

```
现象2：增大Xmx时GC频率明显下降，到2g时没有出现Full GC

结论： GC只对堆内存生效，而且对Young区作用比较大，老年代的GC效果非常低下。老年代默认都是在使用的对象。
生产中可根据具体使用内存情况来调整Xmx的大小。尽量避免Full GC


3. 使用不同的GC回收器比较（其他参数：  -Xms2g -Xmx2g -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis）
- UseG1GC
```
java -XX:+UseG1GC -Xms2g -Xmx2g -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis
2020-10-28T22:54:33.889+0800: [GC pause (G1 Evacuation Pause) (young) 133M->50M(2048M), 0.0094759 secs]
2020-10-28T22:54:33.928+0800: [GC pause (G1 Evacuation Pause) (young) 160M->85M(2048M), 0.0099180 secs]
2020-10-28T22:54:33.962+0800: [GC pause (G1 Evacuation Pause) (young) 195M->113M(2048M), 0.0094260 secs]
2020-10-28T22:54:33.992+0800: [GC pause (G1 Evacuation Pause) (young) 216M->149M(2048M), 0.0114455 secs]
2020-10-28T22:54:34.030+0800: [GC pause (G1 Evacuation Pause) (young) 266M->189M(2048M), 0.0098287 secs]
2020-10-28T22:54:34.088+0800: [GC pause (G1 Evacuation Pause) (young) 361M->244M(2048M), 0.0139423 secs]
2020-10-28T22:54:34.138+0800: [GC pause (G1 Evacuation Pause) (young) 413M->286M(2048M), 0.0114299 secs]
2020-10-28T22:54:34.199+0800: [GC pause (G1 Evacuation Pause) (young) 506M->343M(2048M), 0.0149471 secs]
2020-10-28T22:54:34.274+0800: [GC pause (G1 Evacuation Pause) (young) 606M->411M(2048M), 0.0184905 secs]
2020-10-28T22:54:34.365+0800: [GC pause (G1 Evacuation Pause) (young) 743M->492M(2048M), 0.0205852 secs]
2020-10-28T22:54:34.458+0800: [GC pause (G1 Evacuation Pause) (young) 862M->572M(2048M), 0.0219096 secs]
2020-10-28T22:54:34.804+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 1612M->696M(2048M), 0.0532998 secs]
2020-10-28T22:54:34.857+0800: [GC concurrent-root-region-scan-start]
执行结束!共生成对象次数:11349
2020-10-28T22:54:34.858+0800: [GC concurrent-root-region-scan-end, 0.0008470 secs]
2020-10-28T22:54:34.858+0800: [GC concurrent-mark-start]
2020-10-28T22:54:34.860+0800: [GC concurrent-mark-end, 0.0020425 secs]
2020-10-28T22:54:34.860+0800: [GC remark, 0.0019705 secs]
2020-10-28T22:54:34.863+0800: [GC cleanup 698M->536M(2048M), 0.0012507 secs]
2020-10-28T22:54:34.864+0800: [GC concurrent-cleanup-start]
2020-10-28T22:54:34.864+0800: [GC concurrent-cleanup-end, 0.0000916 secs]
```
- UseParallelGC
```
$ java -XX:+UseParallelGC -Xms2g -Xmx2g -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis
正在执行...
2020-10-28T22:56:34.751+0800: [GC (Allocation Failure)  524800K->144167K(2010112K), 0.0294741 secs]
2020-10-28T22:56:34.869+0800: [GC (Allocation Failure)  668967K->258702K(2010112K), 0.0413457 secs]
2020-10-28T22:56:34.991+0800: [GC (Allocation Failure)  783502K->371953K(2010112K), 0.0427269 secs]
2020-10-28T22:56:35.123+0800: [GC (Allocation Failure)  896753K->483592K(2010112K), 0.0399845 secs]
2020-10-28T22:56:35.243+0800: [GC (Allocation Failure)  1008392K->599449K(2010112K), 0.0411640 secs]
2020-10-28T22:56:35.366+0800: [GC (Allocation Failure)  1124249K->711412K(1720320K), 0.0401323 secs]
2020-10-28T22:56:35.440+0800: [GC (Allocation Failure)  946420K->774956K(1776128K), 0.0323852 secs]
2020-10-28T22:56:35.516+0800: [GC (Allocation Failure)  1009964K->819207K(1864192K), 0.0376295 secs]
执行结束!共生成对象次数:13584
```
- UseSerialGC
```
$ java -XX:+UseSerialGC -Xms2g -Xmx2G  -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis
正在执行...
2020-10-28T22:58:10.761+0800: [GC (Allocation Failure)  559232K->154827K(2027264K), 0.0577437 secs]
2020-10-28T22:58:10.915+0800: [GC (Allocation Failure)  714059K->292806K(2027264K), 0.0768668 secs]
2020-10-28T22:58:11.085+0800: [GC (Allocation Failure)  852038K->418420K(2027264K), 0.0582386 secs]
2020-10-28T22:58:11.229+0800: [GC (Allocation Failure)  977652K->529075K(2027264K), 0.0535979 secs]
2020-10-28T22:58:11.369+0800: [GC (Allocation Failure)  1088307K->663490K(2027264K), 0.0623098 secs]
2020-10-28T22:58:11.514+0800: [GC (Allocation Failure)  1222722K->793532K(2027264K), 0.0600224 secs]
执行结束!共生成对象次数:12819

```
结论：堆内存够用时，G1收集器GC时间最短。PS：线上最近也升级了G1收集器，YGC时间也设置了

```
-server -Xms4G -Xmx4G -XX:MaxMetaspaceSize=1G -XX:+UseG1GC -XX:MaxGCPauseMillis=80 -XX:+PrintGCDetails -XX:+PrintGCDateStamps
```





# GC日志详细说明

## java version
```
$ java -version
java version "1.8.0_251"
Java(TM) SE Runtime Environment (build 1.8.0_251-b08)
Java HotSpot(TM) 64-Bit Server VM (build 25.251-b08, mixed mode)

```

## GC相关参数
```
-XX:+PrintGC 输出简要GC日志 
-XX:+PrintGCDetails 输出详细GC日志 
-Xloggc:gc.log  输出GC日志到文件
-XX:+PrintGCTimeStamps 输出GC的时间戳（以JVM启动到当期的总时长的时间戳形式） 
-XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2020-10-28T21:34:09.558+0800: 0.147） 
-XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
-verbose:gc
-XX:+PrintReferenceGC 打印年轻代各个引用的数量以及时长
-XX:+UseSerialGC 使用串行化GC
-Xms512m  初始化堆大小为512MB
-Xmx512m  最大堆为512MB
-XX:MaxGCPauseMillis=80 YGC最大时间80ms

```


```
java -XX:+UseSerialGC -Xms512m -Xmx512m -Xloggc:gc.demo.UseSerialGC.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
-- 默认使用 UseParallelGC
java  -Xms512m -Xmx512m -Xloggc:gc.demo.default.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis 
java -XX:+UseParallelGC -Xms512m -Xmx512m -Xloggc:gc.demo.UseParallelGC.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -XX:+UseG1GC -Xms512m -Xmx512m -Xloggc:gc.demo.UseG1GC.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

```


# 演示使用类
```java
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
/*
演示GC日志生成与解读
*/
public class GCLogAnalysis {
    private static Random random = new Random();
    public static void main(String[] args) {
        // 当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        // 持续运行毫秒数; 可根据需要进行修改
        long timeoutMillis = TimeUnit.SECONDS.toMillis(1);
        // 结束时间戳
        long endMillis = startMillis + timeoutMillis;
        LongAdder counter = new LongAdder();
        System.out.println("正在执行...");
        // 缓存一部分对象; 进入老年代
        int cacheSize = 2000;
        Object[] cachedGarbage = new Object[cacheSize];
        // 在此时间范围内,持续循环
        while (System.currentTimeMillis() < endMillis) {
            // 生成垃圾对象
            Object garbage = generateGarbage(100*1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            if (randomIndex < cacheSize) {
                cachedGarbage[randomIndex] = garbage;
            }
        }
        System.out.println("执行结束!共生成对象次数:" + counter.longValue());
    }

    // 生成对象
    private static Object generateGarbage(int max) {
        int randomSize = random.nextInt(max);
        int type = randomSize % 4;
        Object result;
        switch (type) {
            case 0:
                result = new int[randomSize];
                break;
            case 1:
                result = new byte[randomSize];
                break;
            case 2:
                result = new double[randomSize];
                break;
            default:
                StringBuilder builder = new StringBuilder();
                String randomString = "randomString-Anything";
                while (builder.length() < randomSize) {
                    builder.append(randomString);
                    builder.append(max);
                    builder.append(randomSize);
                }
                result = builder.toString();
                break;
        }
        return result;
    }
}

```


