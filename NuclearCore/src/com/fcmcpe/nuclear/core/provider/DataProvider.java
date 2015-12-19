package com.fcmcpe.nuclear.core.provider;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.core.provider in project NuclearPlugins .
 */
public interface DataProvider {

    void open() throws ProviderException;

    void close() throws ProviderException;

    boolean selfCheck();
}
