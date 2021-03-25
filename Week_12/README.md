# redis
基于 Redis 5.0.10 (00000000/0) 64 bit

## 编译安装
```shell
root@promotion-test:/usr/local# tar -zxvf redis-5.0.10.tar.gz 
root@promotion-test:/usr/local# cd redis-5.0.10/
# 编译
root@promotion-test:/usr/local/redis-5.0.10# make
root@promotion-test:/usr/local/redis-5.0.10/src# pwd
/usr/local/redis-5.0.10/src
# 进入src目录安装
root@promotion-test:/usr/local/redis-5.0.10/src# make install


```
### 配置文件说明
```shell
# 其他host连接时指定这个ip即可连接
bind 192.168.12.2 
protected-mode no
```

## 主从

### 配置文件修改
[conf 文件地址](https://github.com/shaoqiang6/JAVA-000/tree/main/Week_12/master-slave)
### 命令演示
```shell
root@redis-center-1:/usr/local/redis# src/redis-cli 
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6380
master_link_status:up
master_last_io_seconds_ago:3
master_sync_in_progress:0
slave_repl_offset:630124
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:f1c4122ddb0cf29db8920072ff831b0b21a2e664
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:630124
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:426654
repl_backlog_histlen:203471
127.0.0.1:6379> get hello
"redis-sentinel"
127.0.0.1:6379> set hello cuicui 
(error) READONLY You can't write against a read only replica.

# ### 
root@redis-center-1:~# cd /usr/local/redis/
root@redis-center-1:/usr/local/redis# src/redis-cli -p 6380
127.0.0.1:6380> get hello
"redis-sentinel"
127.0.0.1:6380> info replication
# Replication
role:master
connected_slaves:2
slave0:ip=127.0.0.1,port=6381,state=online,offset=630362,lag=0
slave1:ip=127.0.0.1,port=6379,state=online,offset=630362,lag=0
master_replid:f1c4122ddb0cf29db8920072ff831b0b21a2e664
master_replid2:971bb991e1ad96ddcdcf3f728c02f3a5135f4071
master_repl_offset:630376
second_repl_offset:283401
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:630376
127.0.0.1:6380> set hello
(error) ERR wrong number of arguments for 'set' command
127.0.0.1:6380> set hello cuicui
OK

```

## 哨兵 + 主从

### 配置文件
[conf 文件地址](https://github.com/shaoqiang6/JAVA-000/tree/main/Week_12/sentinel)

### 命令演示

#### 启动sentinel
```shell
# #### 26379
root@redis-center-1:/usr/local/redis# src/redis-sentinel sentinel.1.conf
21235:X 09 Jan 2021 16:19:02.676 # Sentinel ID is 85ae9196d9e54615c44c21050e269855c62cc680
21235:X 09 Jan 2021 16:19:02.676 # +monitor master mymaster 127.0.0.1 6380 quorum 1
21235:X 09 Jan 2021 16:19:32.719 # +sdown sentinel 85ae9196d9e54615c44c21050e269855c62cc681 127.0.0.1 26380 @ mymaster 127.0.0.1 6380
21235:X 09 Jan 2021 16:19:32.719 # +sdown sentinel 85ae9196d9e54615c44c21050e269855c62cc682 127.0.0.1 26381 @ mymaster 127.0.0.1 6380
21235:X 09 Jan 2021 16:20:18.319 # -sdown sentinel 85ae9196d9e54615c44c21050e269855c62cc681 127.0.0.1 26380 @ mymaster 127.0.0.1 6380

# #### 26380
21490:X 09 Jan 2021 16:20:17.377 # Sentinel ID is 85ae9196d9e54615c44c21050e269855c62cc681
21490:X 09 Jan 2021 16:20:17.377 # +monitor master mymaster 127.0.0.1 6380 quorum 2
21490:X 09 Jan 2021 16:20:47.444 # +sdown sentinel 85ae9196d9e54615c44c21050e269855c62cc682 127.0.0.1 26381 @ mymaster 127.0.0.1 6380

# #### 26381 
21697:X 09 Jan 2021 16:22:13.236 # Sentinel ID is 85ae9196d9e54615c44c21050e269855c62cc682
21697:X 09 Jan 2021 16:22:13.236 # +monitor master mymaster 127.0.0.1 6380 quorum 2

```
#### 模拟master 宕机，主从自动切换
1. 哨兵情况
```shell
root@redis-center-1:/usr/local/redis# ps -aux|grep redis
root      1751  0.1  0.1  65776  6860 ?        Ssl  11:34   0:19 src/redis-server *:6380
root@redis-center-1:/usr/local/redis# kill -9 1751
# #### 26380 日志输出 master发生变化，6381升级为master
21490:X 09 Jan 2021 16:25:01.140 # +sdown master mymaster 127.0.0.1 6380
21490:X 09 Jan 2021 16:25:01.221 # +new-epoch 2
21490:X 09 Jan 2021 16:25:01.224 # +vote-for-leader 85ae9196d9e54615c44c21050e269855c62cc680 2
21490:X 09 Jan 2021 16:25:02.157 # +config-update-from sentinel 85ae9196d9e54615c44c21050e269855c62cc680 127.0.0.1 26379 @ mymaster 127.0.0.1 6380
21490:X 09 Jan 2021 16:25:02.157 # +switch-master mymaster 127.0.0.1 6380 127.0.0.1 6381
21490:X 09 Jan 2021 16:25:02.157 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6381
21490:X 09 Jan 2021 16:25:02.158 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6381
21490:X 09 Jan 2021 16:25:32.217 # +sdown slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6381

```
2. server情况
6381升级为master
```shell
root@redis-center-1:/usr/local/redis# src/redis-cli -p 6381
127.0.0.1:6381> info replication
# Replication
role:master
connected_slaves:1
slave0:ip=127.0.0.1,port=6379,state=online,offset=713126,lag=1
master_replid:1a785e9b2aa59425bebe283110852a43fef037f6
master_replid2:f1c4122ddb0cf29db8920072ff831b0b21a2e664
master_repl_offset:713392
second_repl_offset:677873
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:14032
repl_backlog_histlen:699361
127.0.0.1:6381> set hello cuicui
OK
127.0.0.1:6381> get hello
"cuicui"
```
3. 重启6380
6380自动加入到slave中，6381仍然为master
```shell
root@redis-center-1:/usr/local/redis# src/redis-cli -p 6380
127.0.0.1:6380> info replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6381
master_link_status:up
master_last_io_seconds_ago:1
master_sync_in_progress:0
slave_repl_offset:749347
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:1a785e9b2aa59425bebe283110852a43fef037f6
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:749347
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:746784
repl_backlog_histlen:2564
127.0.0.1:6380> get hello
"cuicui"
127.0.0.1:6380> set hello sentinel---
(error) READONLY You can't write against a read only replica.
```

## 集群模式

### 配置文件

### 命令演示
```shell
# 使用官方提供工具来启动一个cluster
root@promotion-test:/usr/local/redis-5.0.10/utils/create-cluster# pwd
/usr/local/redis-5.0.10/utils/create-cluster
root@promotion-test:/usr/local/redis-5.0.10/utils/create-cluster# ./create-cluster 
```




## 参考

[redis replication/sentinel 博客参考](https://www.cnblogs.com/itzhouq/p/redis5.html)

[redis cluster 博客参考](https://my.oschina.net/ruoli/blog/2252393)

[redis cluster 官方文档](https://redis.io/topics/cluster-tutorial)

[shadow-socks](https://my.oschina.net/u/3163032/blog/1863988)
