package cn.duktig.loadbalance.utils;

import cn.duktig.loadbalance.IpMap;
import cn.duktig.loadbalance.LoadBalance;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 负载均衡工具类
 *
 * @author RenShiWei
 * Date: 2021/9/13 16:23
 **/
public class LoadBalanceUtil {

    private static String requestIp;

    /**
     * 统计路由结果
     *
     * @param routingMap 记录路由结果的Map
     */
    public static void countRoutingMap(Map<String, Integer> routingMap) {
        // 路由总体结果
        for (Map.Entry<String, Integer> entry : routingMap.entrySet()) {
            System.out.println("IP:" + entry.getKey() + "，次数：" + entry.getValue());
        }
    }

    /**
     * 模拟路由调用
     *
     * @param loadBalance  负载均衡策略
     * @param requestCount 请求次数
     * @return 负载均衡的记录
     */
    public static Map<String, Integer> imitateRouting(LoadBalance loadBalance, int requestCount) {
        Map<String, Integer> serverMap = new ConcurrentHashMap<>(IpMap.serverWeightMap.size());
        for (int i = 0; i < requestCount; i++) {
            String server = loadBalance.route(IpMap.serverWeightMap);
            Integer count = serverMap.getOrDefault(server, 0);
            serverMap.put(server, ++ count);
        }
        return serverMap;
    }

    /**
     * 获取Ip地址
     *
     * @return IP地址
     */
    public static String getIp() {
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            return ip4.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取请求的 Ip地址 （模拟）
     *
     * @return IP地址
     */
    public static String getRequestIp() {
        return requestIp;
    }

    /**
     * 设置请求的IP 模拟
     *
     * @param requestIp 请求IP
     */
    public static void setRequestIp(String requestIp) {
        LoadBalanceUtil.requestIp = requestIp;
    }
}

