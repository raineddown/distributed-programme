package cn.duktig.loadbalance.algorithm;

import cn.duktig.loadbalance.LoadBalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 加权轮询
 *
 * @author RenShiWei
 * Date: 2021/9/13 16:39
 **/
public class WeightRoundRobinLoadBalance implements LoadBalance {

    private static volatile Integer index = 0;

    /**
     * 路由
     *
     * @param serverMap 服务列表
     * @return 选择到的一个服务
     */
    @Override
    public String route(Map<String, Integer> serverMap) {
        Map<String, Integer> tempMap = new HashMap<>(serverMap.size());
        tempMap.putAll(serverMap);
        List<String> serverList = new ArrayList<>();
        for (String server : tempMap.keySet()) {
            // 按照权重比例添加服务节点（权重高，节点数量多）
            for (int i = 0; i < serverMap.get(server); i++) {
                serverList.add(server);
            }
        }
        synchronized (WeightRoundRobinLoadBalance.class) {
            index++;
            index %= serverList.size();
            return serverList.get(index);
        }
    }


}

