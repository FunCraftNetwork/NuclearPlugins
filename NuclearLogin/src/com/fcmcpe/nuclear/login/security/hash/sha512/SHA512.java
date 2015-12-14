package com.fcmcpe.nuclear.login.security.hash.sha512;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2015/11/30 by xtypr.
 * Package com.fcmcpe.codefuncore.security.hash.sha512 in project CodeFunCore .
 */
public class SHA512 {
    public byte[] digest(byte[] source){
        // 初始化MessageDigest,SHA即SHA-1的简称
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            // 执行摘要方法
            return md.digest(source);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
