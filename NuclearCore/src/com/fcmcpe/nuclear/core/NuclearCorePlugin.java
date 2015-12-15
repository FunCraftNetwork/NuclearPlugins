package com.fcmcpe.nuclear.core;

import cn.nukkit.plugin.PluginBase;
import com.fcmcpe.nuclear.core.ipgeo.BaiduIPGEO;
import com.fcmcpe.nuclear.core.ipgeo.DummyIPGEO;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;
import com.fcmcpe.nuclear.core.listener.CheckLocaleListener;

import java.util.Locale;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.core in project NuclearPlugins .
 */
public final class NuclearCorePlugin extends PluginBase {

    private static NuclearCorePlugin instance;

    public static NuclearCorePlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        saveResource("mysql.yml");
    }

    @Override
    public void onEnable() {
        /* Fire IP-GEO */
        if (getConfig().getNestedAs("language.auto-detection-of-language", Boolean.TYPE))
            NuclearCore.INSTANCE.setIPGEOEngine(new BaiduIPGEO());
        else
            NuclearCore.INSTANCE.setIPGEOEngine(new DummyIPGEO(getConfig().getNestedAs("language.language", String.class)));
        /* Register listeners and tasks */
        getServer().getPluginManager().registerEvents(new CheckLocaleListener(this), this);
        NuclearDictionary.registerPath(this, "com/fcmcpe/nuclear/core/language/");
        /* Self check */
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
