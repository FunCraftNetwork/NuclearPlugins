package com.fcmcpe.nuclear.login;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Utils;
import com.fcmcpe.nuclear.core.NuclearCore;
import com.fcmcpe.nuclear.core.language.NuclearDictionary;
import com.fcmcpe.nuclear.login.command.LoginCommand;
import com.fcmcpe.nuclear.login.command.LogoutCommand;
import com.fcmcpe.nuclear.login.provider.LoginDataProviderMySQL;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.login.task.SendLoginMsgTask;
import com.fcmcpe.nuclear.login.command.RegisterCommand;
import com.fcmcpe.nuclear.login.command.UnregisterCommand;
import com.fcmcpe.nuclear.login.listener.NuclearLoginListener;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login in project NuclearLogin .
 */
public final class NuclearLoginPlugin extends PluginBase {

    @Override
    public void onLoad() {
        saveDefaultConfig();
        /* Dictionary init */
        NuclearDictionary.registerPath(this, "com/fcmcpe/nuclear/login/language/");
        getLogger().info("NuclearLogin by Snake1999.");
    }

    @Override
    public void onEnable() {
        try {
            /* Weak Password */
            String weakPassword = Utils.readFile(getResource("com/fcmcpe/nuclear/login/weakpassword.list"));
            NuclearLogin.INSTANCE.mergeWeakPassword(weakPassword.split("\\r?\\n"));
            /* Fire Provider */
            String sql = Utils.readFile(getResource("com/fcmcpe/nuclear/login/provider/mysql-init.sql"));
            NuclearLogin.INSTANCE.setDataProvider(new LoginDataProviderMySQL(getServer(), sql, NuclearCore.INSTANCE.getMySQLLink()));
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
