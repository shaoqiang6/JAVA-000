

> 课程内容涵盖了主流Java相关各个技术栈，秦老师也近最大努力为我们呈现了这幅清明上河图。虽然课程就要结束了，老师教授的知识将会一直陪伴着我打怪升级之路.
> 总结概览仅涉及关键知识点，具体每个小单元的深入知识后续会持续更新

## jvm

1. JVM内存区域划分;Java虚拟机在运行时，内存分为若干区域；Java虚拟机管理的内存区域有：方法区、堆内存、虚拟机栈、本地方法栈、程序计数器。
2. JVM内存溢出；堆内存溢出，栈内存溢出；方法区溢出
3. 堆栈日志分析；


## NIO
1. IO模型 （阻塞IO、非阻塞、多路复用、信号驱动、异步）同步异步、阻塞非阻塞
2. Netty简单使用，基本原理、 与netty的架构模型 主要的类（BECH）
3. Netty的使用场景 （常见的业务网关 soul）
4. Netty对比Java NIO通过压测对比对应的性能指标



## 并发编程
1. 线程基础 状态转换（RR —WW—B）
2. JUC并发包下的原子类、工具类、锁、线程池
3. ThreadLocal


## Spring
1. 主流开发框架Spring (core, web, mvc, messaging)
2. 理解类加载过程Springboot (自动装配原理，各种starter ,熟练自定义starter)
3. ORM技术体系 （JPA，Hibernate，Mybatis）
4. 常用的提效框架（Java8 lambda，Guava等）
5. 设计原则：LOSID设计模式（创建型、结构型、行为型）


## mysql
1. mysql基础 （事务、索引、锁、针对innodb引擎 的mvcc 、gap锁等）
2. 事务（ACID）
3. 查询优化（索引失效、不精确分页、减少锁的范围）
4. 高可用高性能（读写分离、主从复制、主从切换）分库分表、分布式主键、分表算法
5. 数据迁移等常用的数据库中间件（ss、mycat、TDDL）XA、柔性事务（TCC、TAC）


## 分布式服务
1. RPC 目前常用到的框架有有DUBBO、gRPC、springCloud
2. 通信协议 JSON、ProtocolBuf、Thrift


## 分布式缓存
1. 主要用到内存K-V数据Redis；
2. 在Java中的使用场景：高性能缓存、分布式锁等


## 分布式消息
1. 主流MQ (activemq, rabbitmq, kafka, rocketmq, Pulsar)
2. 消息原理
2. 高可用（集群、分区、副本）
3. 压测


---
fake it, make it 