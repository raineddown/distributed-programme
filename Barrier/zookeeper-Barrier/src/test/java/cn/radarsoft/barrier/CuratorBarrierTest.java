package cn.radarsoft.barrier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class CuratorBarrierTest {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @Test
    public void barrier() {
        // 分布式节点处理业务
        for (int i = 0; i < 5; i++) {
            EXECUTOR_SERVICE.execute(new DistributedDoubleBarrierRunnable());
        }
    }
}
