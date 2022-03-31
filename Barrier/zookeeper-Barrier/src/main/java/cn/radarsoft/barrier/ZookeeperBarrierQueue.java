package cn.radarsoft.barrier;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


public class ZookeeperBarrierQueue implements Watcher{

    private static final String Addr = "zk集群地址";

    private String root = null;

    private ZooKeeper zk = null;

    private static CountDownLatch latch = new CountDownLatch(1);

    private static final CountDownLatch countDownLatch = new CountDownLatch(10);

    public ZookeeperBarrierQueue(String root) {
        this.root = root;
        try {
            // 连接zk服务器
            zk = new ZooKeeper(Addr, 3000, this);
            if (zk != null) {
                // 建立根目录节点
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT);
                    zk.setData(root, "10".getBytes(), -1);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    void add(String path,CountDownLatch countDownLatch) {
        try {
            if(null != zk){
                // 设置一个监控的标志,当大小为10时,所有子节点都已经创建完毕,进行主流程处理
                zk.exists(root + "/start", true);
                zk.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
                List<String> list = zk.getChildren(root, false);
                System.out.println("子节点的个数:" + list.size() + ",跟节点默认参考值:" + Integer.parseInt(new String(zk.getData(root,false, new Stat()))) );
                if (list.size() < Integer.parseInt(new String(zk.getData(root,false, new Stat())))) {
                    countDownLatch.countDown();
                } else {
                    if (null == zk.exists(root + "/start", false)) {
                        zk.create(root + "/start", new byte[0],Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                }
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent event) {
        if ((root + "/start").equals(event.getPath())&& event.getType() == EventType.NodeCreated) {
            System.out.println(root + "/start" + "---" + "节点被传建了");
            try {
                List<String> list = zk.getChildren(root, false);
                for (final String node : list) {
                    if(!"start".equals(node)){
                        System.out.println(node);
                    }
                }
                System.out.println("所以人到齐,开始吃饭");
                countDownLatch.countDown();
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }else if(event.getState() == KeeperState.SyncConnected){
            latch.countDown();
        }
    }

}