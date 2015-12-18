package com.fcmcpe.nuclear.core;

import cn.nukkit.plugin.PluginBase;
import com.fcmcpe.nuclear.core.ipgeo.DummyIPGEO;
import com.fcmcpe.nuclear.core.ipgeo.IPGEOEngine;
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
        NuclearDictionary.registerPath(this, "com/fcmcpe/nuclear/core/language/");
    }

    @Override
    public void onEnable() {
        /* Fire IP-GEO */
        IPGEOEngine dummy = new DummyIPGEO(getConfig().getNestedAs("language.language", String.class));
        if (getConfig().getNestedAs("language.auto-detection-of-language", Boolean.TYPE)) {
            NuclearCore.INSTANCE.setIPGEOEngine(ClassGetter.getOrDefault(IPGEOEngine.class, getConfig().getNestedAs("language.ip-geo-engine", String.class), dummy));
            System.out.println(NuclearCore.INSTANCE.getIPGEOEngine().getClass().toString()); //DEBUG
        } else {
            NuclearCore.INSTANCE.setIPGEOEngine(dummy);
        }
        /* Register listeners and tasks */
        getServer().getPluginManager().registerEvents(new CheckLocaleListener(this), this);
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
