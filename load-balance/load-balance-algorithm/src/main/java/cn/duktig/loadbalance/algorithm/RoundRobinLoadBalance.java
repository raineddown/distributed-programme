package cn.duktig.loadbalance.algorithm;

import cn.duktig.loadbalance.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description: 轮询负载均衡算法
 *
 * @author RenShiWei
 * Date: 2021/9/13 15:57
 **/
public class RoundRobinLoadBalance implements LoadBalance {

    private static volatile Integer index = 0;

    /**
     * 路由
     *
     * @param serverMap 服务列表
     * @return 选择到的一个服务
     */
    @Override
    public String route(Map<String, Integer> serverMap) {
        // 复制遍历用的集合，防止操作中集合有变更
        List<String> serverList = new ArrayList<>(serverMap.size());
        serverList.addAll(serverMap.keySet());
        synchronized (RoundRobinLoadBalance.class) {
            index++;
            index %= serverList.size();
            return serverList.get(index);
        }
    }

}

