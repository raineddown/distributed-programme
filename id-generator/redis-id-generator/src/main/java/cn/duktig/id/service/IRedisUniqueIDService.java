package cn.duktig.id.service;

import cn.duktig.id.enums.UniqueIDEnum;

/**
 * description:唯一id生成接口
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
public interface IRedisUniqueIDService {

    /**
     * 根据单号枚举 生成唯一单号
     *
     * @param uniqueIdEnum 单号枚举
     * @return 唯一单号
     */
    String generateUniqueId(UniqueIDEnum uniqueIdEnum);

}

