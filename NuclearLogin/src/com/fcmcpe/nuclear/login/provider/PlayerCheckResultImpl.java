package com.fcmcpe.nuclear.login.provider;

import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
/**
 * Created on 2015/12/12 by xtypr.
 * Package com.fcmcpe.nuclear.login.provider in project NuclearLogin .
 */
class PlayerCheckResultImpl implements PlayerCheckResult {

    boolean exist = false;
    boolean matchIP = false;
    boolean matchUUID = false;
    int countIP = 0;
    int countUUID = 0;

    PlayerCheckResultImpl(boolean exist, boolean matchIP, boolean matchUUID, int countIP, int countUUID) {
        this.exist = exist;
        this.matchIP = matchIP;
        this.matchUUID = matchUUID;
        this.countIP = countIP;
        this.countUUID = countUUID;
    }

    @Override
    public boolean isExist() {
        return exist;
    }

    @Override
    public boolean isIPMatch() {
        return matchIP;
    }

    @Override
    public boolean isUUIDMatch() {
        return matchUUID;
    }

    @Override
    public int getIPAccountCount() {
        return countIP;
    }

    @Override
    public int getUUIDAccountCount() {
        return countUUID;
    }
}
