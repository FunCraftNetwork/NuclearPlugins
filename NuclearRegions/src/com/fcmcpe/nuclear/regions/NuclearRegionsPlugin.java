package com.fcmcpe.nuclear.regions;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import com.fcmcpe.nuclear.core.NuclearCore;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.regions.listener.NuclearRegionListener;
import com.fcmcpe.nuclear.regions.provider.RegionDataProviderMySQL;

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
        try {
            /* Fire provider */
            String sql = Utils.readFile(getResource("com/fcmcpe/nuclear/regions/provider/mysql-init.sql"));
            NuclearRegions.INSTANCE.setDataProvider(new RegionDataProviderMySQL(getServer(), sql, NuclearCore.INSTANCE.getMySQLLink()));
            /* Fire listener */
            getServer().getPluginManager().registerEvents(new NuclearRegionListener(this), this);
            /* Self check */
            selfCheck();
            /* Reload data */
            NuclearRegions.INSTANCE.reloadRegionData();
            getLogger().info("All region data reloaded.");
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

    private void selfCheck(){
        try {
            getLogger().info("Checking provider: "+ NuclearRegions.INSTANCE.getDataProvider().selfCheck());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
