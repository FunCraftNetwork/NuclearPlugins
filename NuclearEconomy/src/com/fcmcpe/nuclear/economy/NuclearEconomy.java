package com.fcmcpe.nuclear.economy;

import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.provider.MoneyDataProvider;

/**
 * Created on 2015/12/16 by xtypr.
 * Package com.fcmcpe.nuclear.economy in project NuclearPlugins .
 */
public enum NuclearEconomy {
    INSTANCE;

    private NuclearEconomy() {}

    private MoneyDataProvider dataProvider;

    public void setDataProvider(MoneyDataProvider dataProvider) throws ProviderException {
        this.dataProvider = dataProvider;
        dataProvider.open();
    }

    public MoneyDataProvider getDataProvider() {
        return dataProvider;
    }
}
