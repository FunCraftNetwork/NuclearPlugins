package com.fcmcpe.nuclear.core.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import com.fcmcpe.nuclear.core.NuclearCorePlugin;
import com.fcmcpe.nuclear.core.language.CheckLocaleTask;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.core.listener in project NuclearPlugins .
 */
public class CheckLocaleListener implements Listener {

    NuclearCorePlugin plugin;

    public CheckLocaleListener(NuclearCorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPreLogin(PlayerPreLoginEvent event){
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().scheduleAsyncTask(new CheckLocaleTask(player.getName(), player.getAddress()));
    }
}
