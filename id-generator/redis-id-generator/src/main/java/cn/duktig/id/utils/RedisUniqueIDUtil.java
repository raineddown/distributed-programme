package cn.duktig.id.utils;

import cn.duktig.id.constant.UniqueIDConstants;
import cn.duktig.id.enums.UniqueIDEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * description:唯一ID生成工具类
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
public class RedisUniqueIDUtil {

    /**
     * 生成单号前缀：自定义前缀 + 一定格式的时间
     *
     * @param uniqueIdEnum 自定义的枚举
     * @return 单号前缀
     */
    public static String getFormNoPrefix(UniqueIDEnum uniqueIdEnum) {
        //格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(uniqueIdEnum.getDatePattern());
        StringBuffer sb = new StringBuffer();
        sb.append(uniqueIdEnum.getPrefix());
        sb.append(formatter.format(LocalDateTime.now()));
        return sb.toString();
    }

    /**
     * 构建流水号缓存Key
     *
     * @param serialPrefix 流水号前缀
     * @return 流水号缓存Key
     */
    public static String getCacheKey(String serialPrefix) {
        return UniqueIDConstants.SERIAL_CACHE_PREFIX.concat(serialPrefix);
    }

    /**
     * 补全流水号
     *
     * @param serialPrefix      单号前缀
     * @param incrementalSerial 当天自增流水号
     */
    public static String completionSerial(String serialPrefix, Long incrementalSerial, UniqueIDEnum uniqueIdEnum) {
        StringBuffer sb = new StringBuffer(serialPrefix);
        //需要补0的长度=流水号长度 -当日自增计数长度
        int length = uniqueIdEnum.getSerialLength() - String.valueOf(incrementalSerial).length();
        //补零
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        //redis当日自增数
        sb.append(incrementalSerial);
        return sb.toString();
    }

    /**
     * 补全随机数
     *
     * @param serialWithPrefix 当前单号
     * @param uniqueIdEnum     单号生成枚举
     */
    public static String completionRandom(String serialWithPrefix, UniqueIDEnum uniqueIdEnum) {
        StringBuffer sb = new StringBuffer(serialWithPrefix);
        //随机数长度
        int length = uniqueIdEnum.getRandomLength();
        if (length > 0) {
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                //十以内随机数补全
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }

}

