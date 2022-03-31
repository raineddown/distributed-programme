package cn.radarsoft.barrier;

package com.kaven.zookeeper;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;

import java.util.Random;

public class DistributedDoubleBarrierRunnable implements Runnable{
    @SneakyThrows
    @Override
    public void run() {
        // 使用不同的CuratorFramework实例，表示不同的分布式节点
        CuratorFramework curator = CuratorFrameworkProperties.getCuratorFramework();

        // 模拟随机加入的分布式节点
        int randomSleep = new Random().nextInt(20000);
        Thread.sleep(randomSleep);

        // 分布式屏障的路径
        String barrierPath = "/kaven";

        // 创建DistributedDoubleBarrier实例，用于提供分布式屏障功能
        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(curator, barrierPath, 5);

        System.out.println(Thread.currentThread().getName() + " 等待进入屏障");
        long start = System.currentTimeMillis();
        // 等待进入屏障
        barrier.enter();
        System.out.println(Thread.currentThread().getName() + " 等待了 "
                + (System.currentTimeMillis() - start) / 1000 + " s");
        System.out.println(Thread.currentThread().getName() + " 进入屏障");
        Thread.sleep(1000);
        // 等待离开屏障
        barrier.leave();
        System.out.println(Thread.currentThread().getName() + " 离开屏障");
    }
}
