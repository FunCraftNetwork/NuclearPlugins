package com.fcmcpe.nuclear.core;

/**
 * Created on 2015/12/17 by xtypr.
 * Package com.fcmcpe.nuclear.core in project NuclearPlugins .
 */
public final class ClassGetter {

    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(Class<T> type, String className, T defaultValue) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
