package cn.duktig.loadbalance.support;

import cn.duktig.loadbalance.HashFunction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * description: 依托MD5实现的 hash函数
 *
 * @author RenShiWei
 * Date: 2021/9/13 22:27
 **/
public class Md5HashFunction implements HashFunction {

    private MessageDigest md5 = null;

    /**
     * MD5 实现 hash函数
     *
     * @param key hash函数的key
     * @return hashcode
     */
    @Override
    public long hash(String key) {
        if (md5 == null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("no md5 algorithm found");
            }
        }
        md5.reset();
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();
        //具体的哈希函数实现细节--每个字节 & 0xFF 再移位
        long result = ((long) (bKey[3] & 0xFF) << 24)
                | ((long) (bKey[2] & 0xFF) << 16
                | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF));
        return result & 0xffffffffL;
    }

}

