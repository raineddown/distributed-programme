package cn.duktig.loadbalance.algorithm;

import cn.duktig.loadbalance.LoadBalance;

import java.util.*;

/**
 * description: 加权随机 路由算法
 *
 * @author RenShiWei
 * Date: 2021/9/13 16:51
 **/
public class WeightRandomLoadBalance implements LoadBalance {

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
        int randomInt = new Random().nextInt(serverList.size());
        return serverList.get(randomInt);
    }

}

