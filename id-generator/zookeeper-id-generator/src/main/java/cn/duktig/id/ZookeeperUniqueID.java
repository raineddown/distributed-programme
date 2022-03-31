package cn.duktig.id;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * description:zk实现分布式id
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
@Slf4j
public class ZookeeperUniqueID implements Watcher {

    /** 计数器对象 */
    public static CountDownLatch countDownLatch = new CountDownLatch(1);

    /** 连接对象 */
    public static ZooKeeper zooKeeper;

    private final String IP = "127.0.0.1:2181";

    /** 用户生成序号的节点 */
    private final String DEFAULT_PATH = "/uniqueId";
    /** 根节点 */
    private final String ROOT_PATH = "/uniqueId";

    public ZookeeperUniqueID() {
        try {
            zooKeeper = new ZooKeeper(IP, 6000, this);
            //等待zk正常连接后，往下走程序
            countDownLatch.await();
            // 判断根节点是否存在
            Stat stat = zooKeeper.exists(ROOT_PATH, false);
            if (stat == null) {
                // 创建一下根节点
                zooKeeper.create(ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            //EventType = None时
            if (watchedEvent.getType() == Event.EventType.None) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    log.info("连接成功");
                    countDownLatch.countDown();
                } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                    log.info("断开连接");
                } else if (watchedEvent.getState() == Event.KeeperState.Expired) {
                    log.info("会话超时");
                    // 超时后服务器端已经将连接释放，需要重新连接服务器端
                    zooKeeper = new ZooKeeper(IP, 6000, this);
                } else if (watchedEvent.getState() == Event.KeeperState.AuthFailed) {
                    log.info("认证失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据 zk 临时有序节点，生成唯一ID
     *
     * @return 唯一ID
     */
    public String getUniqueId() {
        String path = "";
        //创建临时有序节点
        try {
            path = zooKeeper.create(ROOT_PATH + DEFAULT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("zk创建临时有序节点：{}", path);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        //截取defaultPath的长度
        return path.substring(ROOT_PATH.length() + DEFAULT_PATH.length());
    }

}

