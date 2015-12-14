package com.fcmcpe.nuclear.login.language;

import cn.nukkit.Player;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2015/12/10 by xtypr.
 * Package com.fcmcpe.nuclear.login.language in project NuclearLogin .
 */
public enum TranslationSender {
    INSTANCE;

    private TranslationSender(){}

    final Map<String, Locale> localeMap = new ConcurrentHashMap<>();

    public synchronized boolean hasLocale(String playerName) {
        return localeMap != null && localeMap.containsKey(playerName.toLowerCase().trim());
    }

    public void setLocale(String playerName, Locale locale) {
        synchronized (localeMap) {
            localeMap.putIfAbsent(playerName.trim().toLowerCase(), locale);
        }
    }

    public void sendTip(Player player, String translation){
        player.sendTip(Dictionary.get(
                localeMap.getOrDefault(player.getName().toLowerCase().trim(), Locale.getDefault()),
                translation
        ));
    }

    public void sendPopup(Player player, String translation){
        player.sendPopup(Dictionary.get(
                localeMap.getOrDefault(player.getName().toLowerCase().trim(), Locale.getDefault()),
                translation
        ));
    }

    public void sendMessage(Player player, String translation){
        player.sendMessage(Dictionary.get(
                localeMap.getOrDefault(player.getName().toLowerCase().trim(), Locale.getDefault()),
                translation
        ));
    }

    public void sendKick(Player player, String translation) {
        player.kick(Dictionary.get(
                localeMap.getOrDefault(player.getName().toLowerCase().trim(), Locale.getDefault()),
                translation
        ), false);
    }
}
