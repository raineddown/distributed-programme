package cn.duktig.id;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * description:利用 Curator框架 生成唯一id （底层使用zk）
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
@Slf4j
public class CuratorUniqueID {

    private static CuratorFramework curatorFrameworkClient;

    private static RetryPolicy retryPolicy;

    private static final String IP = "127.0.0.1:2181";

    private static String ROOT = "/uniqueId-curator";

    private static String NODE_NAME = "/uniqueId";

    static {
        retryPolicy = new ExponentialBackoffRetry(1000, 3);
        curatorFrameworkClient = CuratorFrameworkFactory
                .builder()
                .connectString(IP)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        curatorFrameworkClient.start();
        try {
            //请先判断父节点/root节点是否存在
            Stat stat = curatorFrameworkClient.checkExists().forPath(ROOT);
            if (stat == null) {
                curatorFrameworkClient.create().withMode(CreateMode.PERSISTENT).forPath(ROOT, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成唯一id
     *
     * @return 唯一id
     */
    public static String generateId() {
        String backPath = "";
        String fullPath = ROOT.concat(NODE_NAME);
        try {
            // 关键点：创建临时顺序节点
            backPath = curatorFrameworkClient.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(fullPath,
                    null);
            log.info("zk创建临时有序节点：{}", backPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backPath.substring(ROOT.length() + NODE_NAME.length());
    }

}

