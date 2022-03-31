package cn.duktig.id.service.impl;

import cn.duktig.common.utils.RedisUtils;
import cn.duktig.id.constant.UniqueIDConstants;
import cn.duktig.id.enums.UniqueIDEnum;
import cn.duktig.id.service.IRedisUniqueIDService;
import cn.duktig.id.utils.RedisUniqueIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * description:唯一id生成服务
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
@Service
@RequiredArgsConstructor
public class RedisUniqueIDServiceImpl implements IRedisUniqueIDService {

    private final RedisUtils redisUtils;

    /**
     * 根据单号枚举 生成唯一单号
     *
     * @param uniqueIdEnum 单号枚举
     * @return 唯一单号
     */
    @Override
    public String generateUniqueId(UniqueIDEnum uniqueIdEnum) {
        //获得单号前缀 格式 固定前缀 +时间前缀 示例 ：YF20190101
        String prefix = RedisUniqueIDUtil.getFormNoPrefix(uniqueIdEnum);
        //获得缓存key
        String cacheKey = RedisUniqueIDUtil.getCacheKey(prefix);
        //获得当日自增数，并设置时间
        Long incrementalSerial = redisUtils.incr(cacheKey, UniqueIDConstants.DEFAULT_CACHE_DAYS, TimeUnit.DAYS);
        //组合单号并补全流水号
        String serialWithPrefix = RedisUniqueIDUtil.completionSerial(prefix, incrementalSerial, uniqueIdEnum);
        //补全随机数
        return RedisUniqueIDUtil.completionRandom(serialWithPrefix, uniqueIdEnum);
    }

}

