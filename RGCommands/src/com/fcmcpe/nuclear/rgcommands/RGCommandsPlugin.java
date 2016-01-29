package com.fcmcpe.nuclear.rgcommands;

import cn.nukkit.plugin.PluginBase;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;
import com.fcmcpe.nuclear.regions.NuclearRegions;

/**
 * Created by Snake1999 on 2016/1/29.
 * Package com.fcmcpe.nuclear.rgcommands in project NuclearPlugins.
 */
public class RGCommandsPlugin extends PluginBase {

    @Override
    public void onLoad() {
        NuclearDictionary.registerPath(this, "com/fcmcpe/nuclear/rgcommands/language/");
        getLogger().info("\"/rg\" command for NuclearRegions.");
    }

    @Override
    public void onEnable() {
        getServer().getCommandMap().register("RGCommands", new RGCommand("rg"));
        //DEBUG
        NuclearRegions.INSTANCE.getAllRegionData().forEach((d) -> {
            getLogger().info(d.getID() + " " + d.getBox().getSize() + " " + d.getBox().toString());
        });
    }
}
