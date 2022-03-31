package cn.duktig.id.service;

import cn.duktig.id.enums.UniqueIDEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * description: 测试Redis生成唯一ID
 *
 * @author RenShiWei
 * Date: 2021/08/20 17:26
 **/
@SpringBootTest
public class IRedisUniqueIDServiceTest {

    @Autowired
    private IRedisUniqueIDService redisUniqueIDService;

    /**
     * 测试生成唯一id
     */
    @Test
    public void generateUniqueId() {
        String uniqueId = redisUniqueIDService.generateUniqueId(UniqueIDEnum.TS_ORDER);
        System.out.println("唯一id：" + uniqueId);
    }

    /**
     * 多线程环境下，测试生成唯一ID
     */
    @Test
    public void generateUniqueIdForThread() {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "分布式唯一ID:" + redisUniqueIDService.generateUniqueId(UniqueIDEnum.TS_ORDER));
            }
        }, "thread-0").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "分布式唯一ID:" + redisUniqueIDService.generateUniqueId(UniqueIDEnum.TS_ORDER));
            }
        }, "thread-1").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "分布式唯一ID:" + redisUniqueIDService.generateUniqueId(UniqueIDEnum.TS_ORDER));
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
     * 单机Redis
     * 单线程生成10W个 分布式ID 测速
     * 大约为：110353 ms
     */
    @Test
    public void generateUniqueIdForMore() {
        LocalDateTime startTime = LocalDateTime.now();
        for (int i = 0; i < 100000; i++) {
            String id = redisUniqueIDService.generateUniqueId(UniqueIDEnum.TS_ORDER);
            System.out.println(id);
        }
        LocalDateTime endTime = LocalDateTime.now();
        // 计算时间差值
        long minutes = Duration.between(startTime, endTime).toMillis();
        // 输出
        System.out.println("生成10万个分布式id所用的时间：" + minutes + " ms");
    }

    /**
     * 单机Redis
     * 线程池开10个线程生成10W个 分布式ID 测速
     * 大约为：106959 ms
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

    class ThreadPoolTask implements Callable<String> {

        @Override
        public String call() {
            String id = redisUniqueIDService.generateUniqueId(UniqueIDEnum.TS_ORDER);
            System.out.println(Thread.currentThread().getName() + "---" + id);
            return id;
        }

    }

}
