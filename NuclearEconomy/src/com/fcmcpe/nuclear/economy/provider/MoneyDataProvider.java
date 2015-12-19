package com.fcmcpe.nuclear.economy.provider;

import com.fcmcpe.nuclear.core.provider.DataProvider;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.data.MoneyData;
import com.fcmcpe.nuclear.economy.data.MoneyPayResult;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.economy.provider in project NuclearPlugins .
 */
public interface MoneyDataProvider extends DataProvider {

    void registerIfAbsent(MoneyData data) throws ProviderException;

    MoneyPayResult payMoney(String from, String to, long money) throws ProviderException;

    MoneyData getMoney(MoneyData data) throws ProviderException;

}
