package com.fcmcpe.nuclear.economy;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import com.fcmcpe.nuclear.core.NuclearCore;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.command.MoneySeeCommand;
import com.fcmcpe.nuclear.economy.listener.NuclearEconomyListener;
import com.fcmcpe.nuclear.economy.provider.MoneyDataProviderMySQL;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.economy in project NuclearPlugins .
 */
public final class NuclearEconomyPlugin extends PluginBase {

    @Override
    public void onLoad() {
        /* Dictionary init */
        NuclearDictionary.registerPath(this, "com/fcmcpe/nuclear/economy/language/");
        getLogger().info("NuclearEconomy by Snake1999.");
    }

    @Override
    public void onEnable() {
        try {
            /* Fire Provider */
            String sql = Utils.readFile(getResource("com/fcmcpe/nuclear/economy/provider/mysql-init.sql"));
            NuclearEconomy.INSTANCE.setDataProvider(new MoneyDataProviderMySQL(getServer(), sql, NuclearCore.INSTANCE.getMySQLLink()));
            /* Listener */
            getServer().getPluginManager().registerEvents(new NuclearEconomyListener(this), this);
            /* Register Commands */
            getServer().getCommandMap().register("NuclearEconomy", new MoneySeeCommand(this));
        } catch (ClassCastException | NullPointerException e1) {
            getServer().getLogger().logException(e1);
            getLogger().alert("Not a valid config file!");
        } catch (ProviderException e2) {
            getServer().getLogger().logException(e2);
            getLogger().alert("Exception caught in provider!");
        } catch (Exception e3) {
            getServer().getLogger().logException(e3);
            getLogger().alert("Unknown exception caught while enabling!");
        }
        getLogger().info("I've enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("I've disabled!");
    }

}
