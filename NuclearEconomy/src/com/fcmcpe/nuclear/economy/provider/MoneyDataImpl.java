package com.fcmcpe.nuclear.economy.provider;

import cn.nukkit.IPlayer;
import cn.nukkit.Server;
import com.fcmcpe.nuclear.economy.data.MoneyData;

/**
 * Created on 2015/12/16 by xtypr.
 * Package com.fcmcpe.nuclear.economy.provider in project NuclearPlugins .
 */
public class MoneyDataImpl implements MoneyData{

    private IPlayer player;

    private long money;

    MoneyDataImpl(Server nukkit, String playerName, long money) {
        this.player = nukkit.getOfflinePlayer(playerName);
        this.money = money;
    }

    MoneyDataImpl(Server nukkit, String playerName) {
        this(nukkit, playerName, 0);
    }

    MoneyDataImpl(){
        player = null;
        money = 0;
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public long getMoney() {
        return money;
    }
}
