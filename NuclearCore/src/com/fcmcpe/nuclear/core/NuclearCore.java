package com.fcmcpe.nuclear.core;

import com.fcmcpe.nuclear.core.ipgeo.DummyIPGEO;
import com.fcmcpe.nuclear.core.ipgeo.IPGEOEngine;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.core in project NuclearPlugins .
 */
public enum NuclearCore {
    INSTANCE;

    IPGEOEngine ipgeoEngine = new DummyIPGEO("en");

    public void setIPGEOEngine(IPGEOEngine ipgeoEngine) {
        this.ipgeoEngine = ipgeoEngine;
    }

    public IPGEOEngine getIPGEOEngine() {
        return ipgeoEngine;
    }
}
