package com.fcmcpe.nuclear.login;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import com.fcmcpe.nuclear.core.NuclearCore;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;
import com.fcmcpe.nuclear.login.command.LoginCommand;
import com.fcmcpe.nuclear.login.command.LogoutCommand;
import com.fcmcpe.nuclear.login.provider.LoginDataProviderMySQL;
import com.fcmcpe.nuclear.login.provider.ProviderException;
import com.fcmcpe.nuclear.login.task.SendLoginMsgTask;
import com.fcmcpe.nuclear.core.ipgeo.BaiduIPGEO;
import com.fcmcpe.nuclear.core.ipgeo.DummyIPGEO;
import com.fcmcpe.nuclear.login.command.RegisterCommand;
import com.fcmcpe.nuclear.login.command.UnregisterCommand;
import com.fcmcpe.nuclear.login.listener.NuclearLoginListener;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login in project NuclearLogin .
 */
public final class NuclearLoginPlugin extends PluginBase {

    private static NuclearLoginPlugin instance;
    public static NuclearLoginPlugin getInstance(){
        return instance;
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        NuclearLoginPlugin.instance = this;
        getLogger().info("NuclearLogin by Snake1999.");
    }

    @Override
    public void onEnable() {
        try {
            /* Dictionary init */
            NuclearDictionary.registerPath(this, "com/fcmcpe/nuclear/login/language/");
            /* Fire IP-GEO */
            if (getConfig().getNestedAs("language.auto-detection-of-language", Boolean.TYPE))
                NuclearCore.INSTANCE.setIPGEOEngine(new BaiduIPGEO());
            else
                NuclearCore.INSTANCE.setIPGEOEngine(new DummyIPGEO(getConfig().getNestedAs("language.language", String.class)));
            /* Fire Provider */
            String sql = Utils.readFile(getResource("com/fcmcpe/nuclear/login/provider/mysql-init.sql"));
            String sqlLink = "jdbc:mysql://" +
                    getConfig().getNestedAs("mysql.host", String.class) + ":" +
                    String.valueOf(getConfig().getNestedAs("mysql.port", Integer.TYPE)) + "/" +
                    getConfig().getNestedAs("mysql.database", String.class) +
                    "?allowMultiQueries=true";
            String user = getConfig().getNestedAs("mysql.username", String.class);
            String password = getConfig().getNestedAs("mysql.password", String.class);
            NuclearLogin.INSTANCE.setDataProvider(new LoginDataProviderMySQL(this.getServer(), sql, sqlLink, user, password));
            /* Fire Tasks */
            getServer().getPluginManager().registerEvents(new NuclearLoginListener(this), this);
            getServer().getScheduler().scheduleRepeatingTask(new SendLoginMsgTask(this), 5);
            /* Register Commands */
            getServer().getCommandMap().register("NuclearLogin", new LoginCommand(this));
            getServer().getCommandMap().register("NuclearLogin", new RegisterCommand(this));
            if (getConfig().getNestedAs("logout.enabled", Boolean.TYPE))
                getServer().getCommandMap().register("NuclearLogin", new LogoutCommand(this));
            if (getConfig().getNestedAs("unregister.enabled", Boolean.TYPE))
                getServer().getCommandMap().register("NuclearLogin", new UnregisterCommand(this));
            /* SelfCheck */
            selfCheck();
        } catch (ClassCastException | NullPointerException e1) {
            getServer().getLogger().logException(e1);
            getServer().getLogger().alert("Not a valid config file!");
        } catch (ProviderException e2) {
            getServer().getLogger().logException(e2);
            getServer().getLogger().alert("Exception caught in provider!");
        } catch (Exception e3) {
            getServer().getLogger().logException(e3);
            getServer().getLogger().alert("Unknown exception caught while enabling!");
        }
        getLogger().info("I've enabled!");
    }

    @Override
    public void onDisable() {
        try {
            NuclearLogin.INSTANCE.getDataProvider().close();
        } catch (Exception e) {
            getServer().getLogger().logException(e);
        }
        getLogger().info("I've disabled!");
    }

    private void selfCheck(){
        try {
            getLogger().info("Checking provider: "+ NuclearLogin.INSTANCE.getDataProvider().selfCheck());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
