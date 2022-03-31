# id-generator
## 简介

分布式问题解决方案（基于Redis、ZooKeeper等常用技术），包括**分布式ID**、分布式锁、**分布式事务**等等。

## 常见分布式解决方案

### 分布式ID解决方案

在复杂分布式系统中，往往需要对大量的数据和消息进行唯一标识。如在美团点评的金融、支付、餐饮、酒店、猫眼电影等产品的系统中，数据日渐增长，**对数据分库分表后需要有一个唯一ID来标识一条数据或消息，数据库的自增ID显然不能满足需求**；特别一点的如订单、骑手、优惠券也都需要有唯一ID做标识。此时一个能够生成全局唯一ID的系统是非常必要的。

概括下来，那业务系统对ID号的要求有哪些呢？

1. **全局唯一性**：不能出现重复的ID号，既然是唯一标识，这是最基本的要求。
2. **趋势递增**：在MySQL InnoDB引擎中使用的是聚集索引，由于多数RDBMS使用B-tree的数据结构来存储索引数据，在主键的选择上面我们应该尽量使用有序的主键保证写入性能。
   1. **什么是递增？** 如：第一次生成的ID为12，下一次生成的ID是13，再下一次生成的ID是14。这个就是生成ID递增。
   2. **什么是趋势递增？** 如：在一段时间内，生成的ID是递增的趋势。如：再一段时间内生成的ID在【0，1000】之间，过段时间生成的ID在【1000，2000】之间。但在【0-1000】区间内的时候，ID生成有可能第一次是12，第二次是10，第三次是14。
3. **单调递增**：保证下一个ID一定大于上一个ID，例如事务版本号、IM增量消息、排序等特殊需求。
4. **信息安全**：如果ID是连续的，恶意用户的扒取工作就非常容易做了，直接按照顺序下载指定URL即可；如果是订单号就更危险了，竞对可以直接知道我们一天的单量。所以在一些应用场景下，会需要ID无规则、不规则。

有时候也会要求**含时间戳**，这样就能够在开发中快速了解这个分布式id的生成时间。

上述123对应三类不同的场景，3和4需求还是互斥的，无法使用同一个方案满足。

同时除了对ID号码自身的要求，业务还对ID号生成系统的可用性要求极高，想象一下，如果ID生成系统瘫痪，整个美团点评支付、优惠券发券、骑手派单等关键动作都无法执行，这就会带来一场灾难。

**ID号生成系统的可用性要求**

1. 高可用：发一个获取分布式ID的请求，服务器就要保证99.999%的情况下给我创建一个唯一分布式ID。

2. 低延迟：发一个获取分布式ID的请求，服务器就要快，极速。

3. 高QPS：假如并发一口气10万个创建分布式ID请求同时杀过来，服务器要顶的住且一下子成功创建10万个分布式ID。

#### 常用方案

1. UUID
2. MySQL主键自增
3. Redis（原子操作INCR和INCRBY）
4. 雪花算法（twitter）
5. 基于Zookeeper生成全局id
6. MongoDb的ObjectId
7. 美团点评——leaf
   1. Leaf-segment数据库方案
   2. Leaf-snowflake方案
8. 百度开源的分布式唯一ID生成器UidGenerator
9. 滴滴 Tinyid

#### 对比Redis和ZooKeeper测试

环境：**单机的Redis和单机的ZooKeeper进行测试**

|                                  | Redis     | ZooKeeper                        |
| -------------------------------- | --------- | -------------------------------- |
| 单线程10万分布式ID               | 110353 ms | 3060085 ms  大约为 51min         |
| 线程池开10个线程生成10万分布式ID | 106959 ms | 3073690 ms  基本和单线程环境一致 |