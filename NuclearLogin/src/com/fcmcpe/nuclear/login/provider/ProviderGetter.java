package com.fcmcpe.nuclear.login.provider;

import com.fcmcpe.nuclear.core.provider.ProviderException;

/**
 * Created on 2015/12/11 by xtypr.
 * Package com.fcmcpe.nuclear.login.provider in project NuclearLogin .
 */
public final class ProviderGetter {

    @SuppressWarnings("unchecked")
    public static LoginDataProvider getProvider(String className) throws ProviderException {
        try {
            Class rawClass = Class.forName(className);
            if (rawClass.isAssignableFrom(LoginDataProvider.class)) {
                Class<LoginDataProvider> clazz = (Class<LoginDataProvider>) rawClass;
                return clazz.newInstance();
            }
        } catch (Exception e) {
            throw new ProviderException("Exception found when getting provider: ", e);
        }
        return null;
    }

}
