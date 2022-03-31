package cn.duktig.id.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * description:唯一ID枚举类
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
@Getter
@AllArgsConstructor
public enum UniqueIDEnum {

    /**
     * 测试单号
     */
    TS_ORDER("YF", "yyyyMMdd", 7, 3, 20),
    ;

    /**
     * 单号前缀
     * 为空时填""
     */
    private String prefix;

    /**
     * 时间格式表达式
     * 例如：yyyyMMdd
     */
    private String datePattern;

    /**
     * 流水号长度
     */
    private Integer serialLength;

    /**
     * 随机数长度
     */
    private Integer randomLength;

    /**
     * 总长度
     */
    private Integer totalLength;

}

