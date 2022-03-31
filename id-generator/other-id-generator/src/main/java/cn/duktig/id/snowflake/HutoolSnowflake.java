package cn.duktig.id.snowflake;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * description:Hutool的雪花算法实现
 * <p>
 * 可以使用Hutool默认提供的方法 IdUtil.getSnowflake 实现雪花算法，一般使用此即可。可根据情况传入 5位dataCenterId 和 5位workerId
 * <p>
 * 如果使用 IdUtil.createSnowflake 使用雪花算法，需要自行维护单例模式（不同的Snowflake对象创建的ID可能会有重复）。
 * 一个比较好的选择是交由 Spring 管理（默认单例）
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
@Slf4j
public class HutoolSnowflake {

    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.now();
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        for (int i = 0; i < 100000; i++) {
            long id = snowflake.nextId();
//            System.out.println(Long.toBinaryString(id));
//            System.out.println(id);
        }
        LocalDateTime endTime = LocalDateTime.now();
        // 计算时间差值
        long minutes = Duration.between(startTime, endTime).toMillis();
        // 输出
        System.out.println("HutoolSnowflake 生成10万个分布式id所用的时间：" + minutes + " ms");
    }

}

