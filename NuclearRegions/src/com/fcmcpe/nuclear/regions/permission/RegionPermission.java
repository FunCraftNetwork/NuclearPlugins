package com.fcmcpe.nuclear.regions.permission;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.permission in project NuclearPlugins .
 */
public final class RegionPermission {
    public static final int ENTER = 0x00000001;
    public static final int BLOCK_BREAK = 0x00000010;
    public static final int BLOCK_PLACE = 0x00000020;
    public static final int BLOCK_ALL = 0x000000F0;
    public static final int CONTAINER_ALL = 0x00000F00;
    public static final int USE_ALL = 0x0000F000;
    public static final int WATER_FLOW = 0x00010000;
    public static final int ACTION_ALL = 0x000F0000;
    public static final int GRANT_USER = 0x01000000;
    public static final int GRANT_ADMIN = 0x02000000;
    public static final int GRANT_ALL = 0x0F000000;
    public static final int ALL = 0xFFFFFFFF;

    private int code;

    public RegionPermission(int code) {
        this.code = code;
    }

    public boolean hasPermission(int permission) {
        return (code & permission) == permission;
    }

    public void grant(int permission) {
        code |= permission;
    }

    public void revoke(int permission) {
        code &= ~permission;
    }
}
