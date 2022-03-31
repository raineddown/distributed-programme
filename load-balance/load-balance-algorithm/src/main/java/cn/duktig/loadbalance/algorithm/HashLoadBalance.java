package cn.duktig.loadbalance.algorithm;

import cn.duktig.loadbalance.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description: 源地址Hash 路由算法
 *
 * @author RenShiWei
 * Date: 2021/9/13 16:54
 **/
public class HashLoadBalance implements LoadBalance {

    /** 用于源地址hash的参数（可以是IP/主机名/域名） */
    private String requestHashParam;

    public HashLoadBalance(String requestHashParam) {
        this.requestHashParam = requestHashParam;
    }

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
        // 哈希计算请求的服务器
        int index = requestHashParam.hashCode() % serverList.size();
        return serverList.get(Math.abs(index));
    }

}

