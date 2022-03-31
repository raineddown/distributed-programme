package cn.duktig.loadbalance;

/**
 * description: Hash函数接口
 *
 * @author RenShiWei
 * Date: 2021/9/13 21:56
 **/
public interface HashFunction {

    /**
     * hash 函数
     *
     * @param key hash的key
     * @return hashcode
     */
    long hash(String key);

}
