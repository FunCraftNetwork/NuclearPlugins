package com.fcmcpe.nuclear.core;

import cn.nukkit.plugin.PluginBase;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;

import java.util.Locale;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.core in project NuclearPlugins .
 */
public final class NuclearCorePlugin extends PluginBase {
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        NuclearDictionary.registerPath(this, "com/fcmcpe/nuclear/core/language/");
        selfCheck();
    }

    @Override
    public void onDisable() {

    }

    void selfCheck(){
        getLogger().info("Checking IP-GEO: " + NuclearCore.INSTANCE.getIPGEOEngine().getLocaleFromIP("127.0.0.1").toString());
        getLogger().info("Checking dictionary: "+ NuclearDictionary.get(Locale.ENGLISH, "nuclearcore.language.using"));
    }

}
