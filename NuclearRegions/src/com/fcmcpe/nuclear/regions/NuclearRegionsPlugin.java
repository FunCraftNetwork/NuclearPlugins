package com.fcmcpe.nuclear.regions;

import cn.nukkit.plugin.PluginBase;

/**
 * Created on 2015/12/16 by xtypr.
 * Package com.fcmcpe.nuclear.regions in project NuclearPlugins .
 */
public final class NuclearRegionsPlugin extends PluginBase {

    @Override
    public void onLoad() {
        getLogger().info("NuclearRegions by Snake1999.");
    }

    @Override
    public void onEnable() {
        getLogger().info("I've enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("I've disabled!");
    }
}
