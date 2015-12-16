package com.fcmcpe.nuclear.economy.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import com.fcmcpe.nuclear.core.language.TranslationSender;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.NuclearEconomy;
import com.fcmcpe.nuclear.economy.NuclearEconomyPlugin;
import com.fcmcpe.nuclear.economy.provider.MoneyDataImpl;

/**
 * Created on 2015/12/16 by xtypr.
 * Package com.fcmcpe.nuclear.economy.listener in project NuclearPlugins .
 */
public class NuclearEconomyListener implements Listener {

    private NuclearEconomyPlugin plugin;

    public NuclearEconomyListener(NuclearEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    //onNuclearLogin Logged in?
    @EventHandler
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        try {
            NuclearEconomy.INSTANCE.getDataProvider().registerIfAbsent(new MoneyDataImpl(plugin.getServer(), event.getPlayer().getName()));
        } catch (ProviderException e) {
            e.printStackTrace();
            TranslationSender.INSTANCE.sendMessage(event.getPlayer(), "nucleareconomy.error");
        }
    }
}
