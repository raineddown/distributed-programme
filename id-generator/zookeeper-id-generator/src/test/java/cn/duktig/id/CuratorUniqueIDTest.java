package cn.duktig.id;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@SpringBootTest
public class CuratorUniqueIDTest {

    /**
     * 测试 Curator 生成分布式id
     */
    @Test
    public void generateId() {
        String id = CuratorUniqueID.generateId();
        System.out.println(id);
    }

    /**
     * 测试 Curator 生成唯一ID（多线程下）
     */
    @Test
    public void getUniqueIdForThread() {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "分布式唯一ID:" + CuratorUniqueID.generateId());
            }
        }, "thread-0").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "分布式唯一ID:" + CuratorUniqueID.generateId());
            }
        }, "thread-1").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "分布式唯一ID:" + CuratorUniqueID.generateId());
            }
        }, "thread-2").start();

        //睡眠，用以保证有充足的时间执行
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单机ZooKeeper
     * 单线程生成10W个 分布式ID 测速
     * 大约为：3060085 ms  大约为 51min
     */
    @Test
    public void generateUniqueIdForMore() {
        LocalDateTime startTime = LocalDateTime.now();
        for (int i = 0; i < 100000; i++) {
            String id = CuratorUniqueID.generateId();
            System.out.println(id);
        }
        LocalDateTime endTime = LocalDateTime.now();
        // 计算时间差值
        long minutes = Duration.between(startTime, endTime).toMillis();
        // 输出
        System.out.println("生成10万个分布式id所用的时间：" + minutes + " ms");
    }

    /**
     * 单机ZooKeeper
     * 线程池开10个线程生成10W个 分布式ID 测速
     * 大约为：3073690 ms  基本和单线程环境一致
     */
    @Test
    public void generateUniqueIdForThreadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = null;

        //创建线程池
        threadPoolExecutor = new ThreadPoolExecutor(10,
                20,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20),
                new ThreadPoolExecutor.CallerRunsPolicy());
        LocalDateTime startTime = LocalDateTime.now();
        for (int i = 0; i < 100000; i++) {
            FutureTask<String> futureTask = new FutureTask<>(new ThreadPoolTask());
            threadPoolExecutor.execute(futureTask);
            try {
                String id = futureTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        threadPoolExecutor.shutdown();
        LocalDateTime endTime = LocalDateTime.now();
        // 计算时间差值
        long minutes = Duration.between(startTime, endTime).toMillis();
        // 输出
        System.out.println("线程池 生成10万个分布式id所用的时间：" + minutes + " ms");

    }

    static class ThreadPoolTask implements Callable<String> {

        @Override
        public String call() {
            String id = CuratorUniqueID.generateId();
            System.out.println(Thread.currentThread().getName() + "---" + id);
            return id;
        }

    }

}
