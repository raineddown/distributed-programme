package cn.duktig.id.uuid;

import cn.hutool.core.util.IdUtil;

import java.util.UUID;

/**
 * description:
 *
 * @author guozhixian
 * Date: 2021/6/19 9:20
 **/
public class MyUUID {

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        String simpleUUID = IdUtil.simpleUUID();
        System.out.println(simpleUUID);
    }

}

