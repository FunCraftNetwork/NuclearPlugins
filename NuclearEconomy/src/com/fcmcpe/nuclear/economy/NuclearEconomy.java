package com.fcmcpe.nuclear.economy;

import cn.nukkit.Server;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.data.MoneyData;
import com.fcmcpe.nuclear.economy.data.MoneyPayResult;
import com.fcmcpe.nuclear.economy.provider.MoneyDataImpl;
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

    public MoneyData getMoney(String playerName) {
        try {
            return this.getDataProvider().getMoney(new MoneyDataImpl(Server.getInstance(), playerName));
        } catch (ProviderException e) {
            Server.getInstance().getLogger().logException(e);
            return null;
        }
    }

    public MoneyPayResult payMoney(String from, String to, long money) {
        try {
            return this.getDataProvider().payMoney(from, to, money);
        } catch (ProviderException e) {
            Server.getInstance().getLogger().logException(e);
            return null;
        }
    }
}
