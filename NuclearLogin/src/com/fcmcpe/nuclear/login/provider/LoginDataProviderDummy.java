package com.fcmcpe.nuclear.login.provider;

import com.fcmcpe.nuclear.login.data.LoginData;
import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
import com.fcmcpe.nuclear.login.data.PlayerLoginResult;
import com.fcmcpe.nuclear.login.data.PlayerUnregisterResult;
/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login.provider in project NuclearLogin .
 */
public class LoginDataProviderDummy implements LoginDataProvider{
    @Override
    public void open() throws ProviderException {

    }

    @Override
    public void close() throws ProviderException {

    }

    @Override
    public PlayerLoginResult login(LoginData data) throws ProviderException {
        return null;
    }

    @Override
    public void logout(LoginData data) throws ProviderException {

    }

    @Override
    public void registerIfAbsent(LoginData data) throws ProviderException {

    }

    @Override
    public PlayerUnregisterResult unregisterIfPresent(LoginData data) throws ProviderException {
        return null;
    }

    @Override
    public PlayerCheckResult checkPlayer(LoginData data) throws ProviderException {
        return null;
    }

    @Override
    public boolean selfCheck() {
        return false;
    }
}
