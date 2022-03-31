package cn.duktig.loadbalance;

import cn.duktig.loadbalance.algorithm.*;
import cn.duktig.loadbalance.support.Md5HashFunction;
import cn.duktig.loadbalance.utils.LoadBalanceUtil;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class LoadBalanceTest {

    /**
     * 测试 轮询
     */
    @Test
    public void testRoundRobin() {
        Map<String, Integer> routingMap = LoadBalanceUtil.imitateRouting(new RoundRobinLoadBalance(), 20000);
        // 统计路由结果
        LoadBalanceUtil.countRoutingMap(routingMap);
    }

    /**
     * 测试 随机
     */
    @Test
    public void testRandom() {
        Map<String, Integer> routingMap = LoadBalanceUtil.imitateRouting(new RandomLoadBalance(), 20000);
        // 统计路由结果
        LoadBalanceUtil.countRoutingMap(routingMap);
    }

    /**
     * 测试 加权轮询
     */
    @Test
    public void testWeightRoundRobin() {
        Map<String, Integer> routingMap = LoadBalanceUtil.imitateRouting(new WeightRoundRobinLoadBalance(), 20000);
        // 统计路由结果
        LoadBalanceUtil.countRoutingMap(routingMap);
    }

    /**
     * 测试 加权随机
     */
    @Test
    public void testWeightRandom() {
        Map<String, Integer> routingMap = LoadBalanceUtil.imitateRouting(new WeightRandomLoadBalance(), 20000);
        // 统计路由结果
        LoadBalanceUtil.countRoutingMap(routingMap);
    }

    /**
     * 测试 源地址Hash
     */
    @Test
    public void testHash() {
        // 提前设置请求的Ip
        LoadBalanceUtil.setRequestIp("127.0.0.1");
        Map<String, Integer> routingMap =
                LoadBalanceUtil.imitateRouting(new HashLoadBalance(LoadBalanceUtil.getRequestIp()), 20000);
        // 统计路由结果
        LoadBalanceUtil.countRoutingMap(routingMap);
    }

    /**
     * 对比 几种负载均衡算法的时间
     */
    @Test
    public void testLoadBalanceTime() {
        System.out.println("---测试10W访问量，各种负载均衡算法用时---");
        LocalDateTime startTime = LocalDateTime.now();
        LoadBalanceUtil.imitateRouting(new RoundRobinLoadBalance(), 100000);
        LocalDateTime endTime = LocalDateTime.now();
        long minutes = Duration.between(startTime, endTime).toMillis();
        System.out.println("轮询负载均衡算法 用时：" + minutes + " ms");

        startTime = LocalDateTime.now();
        LoadBalanceUtil.imitateRouting(new RandomLoadBalance(), 100000);
        endTime = LocalDateTime.now();
        minutes = Duration.between(startTime, endTime).toMillis();
        System.out.println("随机负载均衡算法 用时：" + minutes + " ms");

        startTime = LocalDateTime.now();
        LoadBalanceUtil.imitateRouting(new WeightRoundRobinLoadBalance(), 100000);
        endTime = LocalDateTime.now();
        minutes = Duration.between(startTime, endTime).toMillis();
        System.out.println("加权轮询负载均衡算法 用时：" + minutes + " ms");

        startTime = LocalDateTime.now();
        LoadBalanceUtil.imitateRouting(new WeightRandomLoadBalance(), 100000);
        endTime = LocalDateTime.now();
        minutes = Duration.between(startTime, endTime).toMillis();
        System.out.println("加权随机负载均衡算法 用时：" + minutes + " ms");

        // 提前设置请求的Ip
        LoadBalanceUtil.setRequestIp("127.0.0.1");
        startTime = LocalDateTime.now();
        LoadBalanceUtil.imitateRouting(new HashLoadBalance(LoadBalanceUtil.getRequestIp()), 100000);
        endTime = LocalDateTime.now();
        minutes = Duration.between(startTime, endTime).toMillis();
        System.out.println("源地址Hash负载均衡算法 用时：" + minutes + " ms");
    }

    /**
     * 测试 一致性hash 实现负载均衡
     */
    @Test
    public void testConsistentHashLoadBalance() {
        System.out.println("---第一台机器测试---");
        // 提前设置请求的Ip
        LoadBalanceUtil.setRequestIp("192.122.2.1");
        Map<String, Integer> routingMap =
                LoadBalanceUtil.imitateRouting(new ConsistentHashLoadBalance(new Md5HashFunction(), 3,
                        LoadBalanceUtil.getRequestIp()), 4000);
        // 统计路由结果
        LoadBalanceUtil.countRoutingMap(routingMap);

        System.out.println("---第二台机器测试---");
        LoadBalanceUtil.setRequestIp("192.122.2.1");
        routingMap = LoadBalanceUtil.imitateRouting(new ConsistentHashLoadBalance(new Md5HashFunction(), 3,
                LoadBalanceUtil.getRequestIp()), 4000);
        LoadBalanceUtil.countRoutingMap(routingMap);

        System.out.println("---第三台机器测试---");
        LoadBalanceUtil.setRequestIp("192.122.2.2");
        routingMap = LoadBalanceUtil.imitateRouting(new ConsistentHashLoadBalance(new Md5HashFunction(), 3,
                LoadBalanceUtil.getRequestIp()), 4000);
        LoadBalanceUtil.countRoutingMap(routingMap);

        System.out.println("---第四台机器测试---");
        LoadBalanceUtil.setRequestIp("192.122.2.2");
        routingMap = LoadBalanceUtil.imitateRouting(new ConsistentHashLoadBalance(new Md5HashFunction(), 3,
                LoadBalanceUtil.getRequestIp()), 4000);
        LoadBalanceUtil.countRoutingMap(routingMap);

        System.out.println("---第五台机器测试---");
        LoadBalanceUtil.setRequestIp("192.122.2.3");
        routingMap = LoadBalanceUtil.imitateRouting(new ConsistentHashLoadBalance(new Md5HashFunction(), 3,
                LoadBalanceUtil.getRequestIp()), 4000);
        LoadBalanceUtil.countRoutingMap(routingMap);
    }

}
