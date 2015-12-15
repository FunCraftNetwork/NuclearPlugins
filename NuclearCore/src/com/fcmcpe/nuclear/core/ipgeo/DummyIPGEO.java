package com.fcmcpe.nuclear.core.ipgeo;

import java.util.Locale;

/**
 * Created on 2015/12/12 by xtypr.
 * Package com.fcmcpe.nuclear.core.ipgeo in project NuclearLogin .
 */
public class DummyIPGEO implements IPGEOEngine {

    private String languageTag;

    public DummyIPGEO(String languageTag) {
        this.languageTag = languageTag;
    }

    @Override
    public Locale getLocaleFromIP(String ip) {
        try {
            return Locale.forLanguageTag(languageTag);
        } catch (Exception e) {
            return Locale.ENGLISH;
        }
    }
}
