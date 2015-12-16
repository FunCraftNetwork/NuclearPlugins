package com.fcmcpe.nuclear.login;

import cn.nukkit.IPlayer;
import com.fcmcpe.nuclear.login.provider.LoginDataProvider;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.login.provider.LoginDataProviderDummy;

import java.util.*;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login in project NuclearLogin .
 */
public enum NuclearLogin {
    INSTANCE;

    private LoginDataProvider dataProvider = new LoginDataProviderDummy();

    Map<String, Boolean> isLoggedIn = new LinkedHashMap<>();
    Map<String, Integer> loginAttempts = new LinkedHashMap<>();
    Collection<String> weakPasswords = new ArrayList<>();

    private NuclearLogin(){}

    public void mergeWeakPassword(String[] source) {
        Collection<String> strings = new ArrayList<>();
        Collections.addAll(strings, source);
        mergeWeakPassword(strings);
    }

    public void mergeWeakPassword(Collection<String> source) {
        weakPasswords.addAll(source);
    }

    public boolean isPasswordWeak(String password) {
        return weakPasswords.contains(password.toLowerCase());
    }

    public void setDataProvider(LoginDataProvider dataProvider) throws ProviderException {
        this.dataProvider = dataProvider;
        dataProvider.open();
    }

    public LoginDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setLogin(IPlayer player, boolean value){
        String playerName = player.getName().trim().toLowerCase();
        isLoggedIn.remove(playerName);
        isLoggedIn.putIfAbsent(playerName, value);
        loginAttempts.replace(playerName, 1);
    }

    public boolean isLoggedIn(IPlayer player){
        String identifier = player.getName().trim().toLowerCase();
        return isLoggedIn.getOrDefault(identifier, false);
    }

    public int getLoginAttempts(IPlayer player) {
        String identifier = player.getName().trim().toLowerCase();
        return loginAttempts.getOrDefault(identifier, 1);
    }

    public void loginAttemptPlusOne(IPlayer player) {
        String identifier = player.getName().trim().toLowerCase();
        loginAttempts.putIfAbsent(identifier, 1);
        loginAttempts.replace(identifier, loginAttempts.getOrDefault(identifier, 1) + 1);
    }
}
