package cn.radarsoft.barrier;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class ZooKeeperBarrierTest {

    private static CountDownLatch latch = new CountDownLatch(1);

    private static final CountDownLatch countDownLatch = new CountDownLatch(10);

    private static String subNode = "/element";

    @Test
    public void barrier() throws InterruptedException {
        try {
            final ZookeeperBarrierQueue queue2 = new ZookeeperBarrierQueue("/queue_barrier");
            latch.await();
            for (int i = 0; i < 10; i++) {
                queue2.add("/queue_barrier" + subNode,countDownLatch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        countDownLatch.await();
    }
}
