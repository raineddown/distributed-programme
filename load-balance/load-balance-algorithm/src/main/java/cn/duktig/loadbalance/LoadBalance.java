package cn.duktig.loadbalance;

import java.util.Map;

/**
 * description: 负载均衡策略接口
 *
 * @author RenShiWei
 * Date: 2021/9/13 16:09
 **/
public interface LoadBalance {

    /**
     * 路由
     *
     * @param serverMap 服务列表
     * @return 选择到的一个服务
     */
    String route(Map<String, Integer> serverMap);

}
