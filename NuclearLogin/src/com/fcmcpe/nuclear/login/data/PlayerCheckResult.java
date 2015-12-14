package com.fcmcpe.nuclear.login.data;

/**
 * Created on 2015/12/12 by xtypr.
 * Package com.fcmcpe.nuclear.login.data in project NuclearLogin .
 */
public interface PlayerCheckResult {
    boolean isExist();

    boolean isIPMatch();

    boolean isUUIDMatch();

    int getIPAccountCount();

    int getUUIDAccountCount();
}
