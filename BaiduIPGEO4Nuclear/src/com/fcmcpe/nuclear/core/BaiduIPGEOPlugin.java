package com.fcmcpe.nuclear.core;

import cn.nukkit.plugin.PluginBase;
import com.fcmcpe.nuclear.core.ipgeo.BaiduIPGEO;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.core in project NuclearPlugins .
 */
public class BaiduIPGEOPlugin extends PluginBase {
    @Override
    public void onLoad() {
        saveDefaultConfig();
        BaiduIPGEO.setAPIKey(getConfig().getNestedAs("api-key", String.class));
    }

    @Override
    public void onEnable() {
        BaiduIPGEO.setAPIKey(getConfig().getNestedAs("api-key", String.class));
    }

    @Override
    public void onDisable() {

    }
}
