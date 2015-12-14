package com.fcmcpe.nuclear.login.security;

import com.fcmcpe.nuclear.login.security.hash.sha512.SHA512;
import com.fcmcpe.nuclear.login.security.hash.whirlpool.Whirlpool;

import java.util.Objects;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login.security in project NuclearLogin .
 */
public enum PasswordUtil {
    INSTANCE;

    private SHA512 sha512;

    private PasswordUtil(){
        sha512 = new SHA512();
    }
    /**
     * Uses SHA-512 [http://en.wikipedia.org/wiki/SHA-2] an Whirlpool [http://en.wikipedia.org/wiki/Whirlpool_(cryptography)]
     *
     * Both of them have an output of 512 bits. Even if one of them is broken in the future, you have to break both of them
     * at the same time due to being hashed separately and then XORed to mix their results equally.
     *
     * @param salt salt
     * @param password password
     *
     * @return string[128] hex 512-bit hash
     */
    public String getHash(String salt, String password){
        String s1 = password + salt;
        String s2 = salt + password;
        byte[] d1 = sha512.digest(s1.getBytes());
        byte[] d2 = new Whirlpool().digest(s2.getBytes());
        byte[] result = new byte[64];

        for(int i=0;i<d1.length;i++){
            byte temp1 = d1[i];
            byte temp2 = d2[i];
            byte temp3 = (byte)(temp1^temp2);
            result[i] = temp3;
        }

        return bytesToHexString(result);
    }

    public boolean isPasswordCorrect(String name, String password, String hash){
        return Objects.equals(getHash(name, password), hash);
    }

    /**
     * Convert byte[] to hex string.
     * Code from: http://blog.csdn.net/redhat456/article/details/4492310
     * @param src byte[] data
     * @return hex string
     */
    public String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
