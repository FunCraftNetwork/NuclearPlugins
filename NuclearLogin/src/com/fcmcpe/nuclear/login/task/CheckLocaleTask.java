package com.fcmcpe.nuclear.login.task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import com.fcmcpe.nuclear.login.NuclearLoginPlugin;
import com.fcmcpe.nuclear.login.language.TranslationSender;

import java.util.Locale;

/**
 * Created on 2015/12/11 by xtypr.
 * Package com.fcmcpe.nuclear.login.task in project NuclearLogin .
 */
public class CheckLocaleTask extends AsyncTask {

    private String playerIdentifier;

    private String address;

    public CheckLocaleTask(String playerIdentifier, String address) {
        this.playerIdentifier = playerIdentifier.toLowerCase().trim();
        this.address = address;
    }

    @Override
    public void onRun() {
        try {
            setResult(NuclearLoginPlugin.getInstance().getIPGEOEngine().getLocaleFromIP(address));
        } catch (Exception e) {
            setResult(e);
        }

    }

    @Override
    public void onCompletion(Server server) {
        if (getResult() instanceof Locale) {
            TranslationSender.INSTANCE.setLocale(playerIdentifier, (Locale) getResult());
        } else if (getResult() instanceof Exception) {
            Exception e = (Exception) getResult();
            Server.getInstance().getLogger().logException(e);
        }
    }
}
