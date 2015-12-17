package com.fcmcpe.nuclear.economy.data;

/**
 * Created on 2015/12/17 by xtypr.
 * Package com.fcmcpe.nuclear.economy.provider in project NuclearPlugins .
 */
public interface MoneyPayResult {

    boolean isFromExist();

    boolean isToExist();

    boolean isFromEnough();

    long getMoney();
}
