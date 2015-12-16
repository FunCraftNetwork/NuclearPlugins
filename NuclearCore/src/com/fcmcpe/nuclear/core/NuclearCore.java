package com.fcmcpe.nuclear.core;

import cn.nukkit.utils.Config;
import com.fcmcpe.nuclear.core.ipgeo.DummyIPGEO;
import com.fcmcpe.nuclear.core.ipgeo.IPGEOEngine;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    public String getMySQLLink() {
        NuclearCorePlugin plugin = NuclearCorePlugin.getInstance();
        Config mysqlConfig = new Config(new File(plugin.getDataFolder() ,"mysql.yml"), Config.YAML);
        try {
            return "jdbc:mysql://" +
                    mysqlConfig.getNestedAs("mysql.host", String.class).trim() + ":" +
                    String.valueOf(mysqlConfig.getNestedAs("mysql.port", Integer.TYPE)) + "/" +
                    mysqlConfig.getNestedAs("mysql.database", String.class) +
                    "?allowMultiQueries=true" +
                    "&user=" + URLEncoder.encode(mysqlConfig.getNestedAs("mysql.user", String.class), "UTF-8") +
                    "&password=" + URLEncoder.encode(mysqlConfig.getNestedAs("mysql.password", String.class), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
