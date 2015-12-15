package com.fcmcpe.nuclear.economy;

import cn.nukkit.plugin.PluginBase;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;

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

    }

    @Override
    public void onDisable() {

    }

}
