package cn.radarsoft.barrier;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorFrameworkProperties {
    // 连接地址
    public static final String CONNECT_ADDRESS = "192.168.1.3:9000";
    // 连接超时时间
    public static final int CONNECTION_TIMEOUT_MS = 40000;
    // Session超时时间
    public static final int SESSION_TIMEOUT_MS = 10000;
    // 命名空间
    public static final String NAMESPACE = "MyNamespace";
    // 重试策略
    public static final RetryPolicy RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);

    public static CuratorFramework getCuratorFramework() {
        // 创建CuratorFramework实例
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(CuratorFrameworkProperties.CONNECT_ADDRESS)
                .retryPolicy(CuratorFrameworkProperties.RETRY_POLICY)
                .connectionTimeoutMs(CuratorFrameworkProperties.CONNECTION_TIMEOUT_MS)
                .sessionTimeoutMs(CuratorFrameworkProperties.SESSION_TIMEOUT_MS)
                .namespace(CuratorFrameworkProperties.NAMESPACE)
                .build();
        curator.start();
        assert curator.getState().equals(CuratorFrameworkState.STARTED);
        return curator;
    }
}
