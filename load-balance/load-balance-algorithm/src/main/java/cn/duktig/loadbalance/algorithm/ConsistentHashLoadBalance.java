package cn.duktig.loadbalance.algorithm;

import cn.duktig.loadbalance.HashFunction;
import cn.duktig.loadbalance.LoadBalance;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * description: 一致性hash 负载均衡算法
 *
 * @author RenShiWei
 * Date: 2021/9/13 21:50
 **/
public class ConsistentHashLoadBalance implements LoadBalance {

    /** Hash函数 */
    private final HashFunction hashFunction;

    /** 节点的复制因子, 虚拟节点个数 = 实际节点个数 * numberOfReplicas */
    private final int numberOfReplicas;

    /** 存储虚拟节点的hash值到真实节点的映射 */
    private final SortedMap<Long, String> circle;

    /** 用于源地址hash的参数（可以是IP/主机名/域名） */
    private String requestHashParam;

    public ConsistentHashLoadBalance(HashFunction hashFunction, int numberOfReplicas, String requestHashParam) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
        this.requestHashParam = requestHashParam;
        circle = new TreeMap<>();
    }

    /**
     * 添加服务实例，并构建虚拟节点
     *
     * @param node 待添加的服务实例
     */
    public void add(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            /* 对于一个实际机器节点 node, 对应 numberOfReplicas 个虚拟节点
             * 不同的虚拟节点(i不同)有不同的hash值,但都对应同一个实际机器node
             * 虚拟node一般是均衡分布在环上的,数据存储在顺时针方向的虚拟node上
             */
            circle.put(hashFunction.hash(node + i), node);
        }
    }

    /**
     * 移除节点
     *
     * @param node 待移除节点
     */
    public void remove(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hash(node + i));
        }
    }

    /**
     * 获得一个最近的顺时针节点,根据给定的key 取Hash
     * 然后再取得顺时针方向上最近的一个虚拟节点对应的实际节点
     * 再从实际节点中取得 数据
     */
    public String get(String key) {
        if (circle.isEmpty()) {
            return null;
        }
        // node 用String来表示,获得node在哈希环中的hashCode
        long hash = hashFunction.hash(key);
        //数据映射在两台虚拟机器所在环之间,就需要按顺时针方向寻找机器
        if (! circle.containsKey(hash)) {
            SortedMap<Long, String> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    /**
     * 查询虚拟节点数量
     *
     * @return 虚拟节点数量
     */
    public long getSize() {
        return circle.size();
    }

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
        for (String server : tempMap.keySet()) {
            // 依据实际节点 构建 虚拟节点
            this.add(server);
        }
        return this.get(requestHashParam);
    }

}

