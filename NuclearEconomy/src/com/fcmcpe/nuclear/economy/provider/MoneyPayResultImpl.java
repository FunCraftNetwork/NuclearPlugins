package com.fcmcpe.nuclear.economy.provider;

import com.fcmcpe.nuclear.economy.data.MoneyPayResult;

/**
 * Created on 2015/12/17 by xtypr.
 * Package com.fcmcpe.nuclear.economy.provider in project NuclearPlugins .
 */
public class MoneyPayResultImpl implements MoneyPayResult {

    private boolean fromExist;
    private boolean toExist;
    private boolean fromEnough;
    private long money;

    public MoneyPayResultImpl(boolean fromExist, boolean toExist, boolean fromEnough, long money) {
        this.fromExist = fromExist;
        this.toExist = toExist;
        this.fromEnough = fromEnough;
        this.money = money;
    }

    @Override
    public boolean isFromExist() {
        return fromExist;
    }

    @Override
    public boolean isToExist() {
        return toExist;
    }

    @Override
    public boolean isFromEnough() {
        return fromEnough;
    }

    @Override
    public long getMoney() {
        return money;
    }
}
