package cn.duktig.loadbalance.algorithm;

import cn.duktig.loadbalance.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * description: 随机负载均衡算法
 *
 * @author RenShiWei
 * Date: 2021/9/13 16:34
 **/
public class RandomLoadBalance implements LoadBalance {


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
        // 随机数随机访问
        int randomInt = new Random().nextInt(serverList.size());
        return serverList.get(randomInt);
    }

}

