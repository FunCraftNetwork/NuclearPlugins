package com.fcmcpe.nuclear.login.provider;

import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
import com.fcmcpe.nuclear.login.data.LoginData;
import com.fcmcpe.nuclear.login.data.PlayerLoginResult;
import com.fcmcpe.nuclear.login.data.PlayerUnregisterResult;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login.provider in project NuclearLogin .
 */
public interface LoginDataProvider {

    void open() throws ProviderException;

    void close() throws ProviderException;

    PlayerLoginResult login(LoginData data) throws ProviderException;

    void logout(LoginData data) throws ProviderException;

    void registerIfAbsent(LoginData data) throws ProviderException;

    PlayerUnregisterResult unregisterIfPresent(LoginData data) throws ProviderException;

    PlayerCheckResult checkPlayer(LoginData data) throws ProviderException;

    boolean selfCheck();
}
