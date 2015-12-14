package com.fcmcpe.nuclear.login.util.ipgeo;

import com.fcmcpe.nuclear.login.NuclearLoginPlugin;

import java.util.Locale;

/**
 * Created on 2015/12/12 by xtypr.
 * Package com.fcmcpe.nuclear.login.util.ipgeo in project NuclearLogin .
 */
public class DummyIPGEO implements IPGEOEngine {
    @Override
    public Locale getLocaleFromIP(String ip) {
        try {
            return Locale.forLanguageTag(NuclearLoginPlugin.getInstance().getConfig().getNestedAs("language.language", String.class));
        } catch (Exception e) {
            return Locale.ENGLISH;
        }
    }
}
