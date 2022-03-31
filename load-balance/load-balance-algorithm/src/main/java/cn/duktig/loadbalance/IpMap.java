package cn.duktig.loadbalance;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 模拟IP列表
 *
 * @author RenShiWei
 * Date: 2021/9/13 15:54
 **/
public class IpMap {

    /**
     * 待路由的Ip列表，Key代表Ip，Value代表该Ip的权重
     */
    public static Map<String, Integer> serverWeightMap =
            new HashMap<>();

    static {
        serverWeightMap.put("192.168.1.100", 1);
        serverWeightMap.put("192.168.1.101", 1);
        serverWeightMap.put("192.168.1.102", 2);
        serverWeightMap.put("192.168.1.103", 2);
        serverWeightMap.put("192.168.1.104", 3);
    }

}

