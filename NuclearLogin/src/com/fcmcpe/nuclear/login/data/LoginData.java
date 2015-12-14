package com.fcmcpe.nuclear.login.data;

import cn.nukkit.IPlayer;

import java.net.InetAddress;
import java.sql.Date;
import java.util.UUID;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login.data in project NuclearLogin .
 */
public interface LoginData {

    IPlayer getPlayer();

    String getHash();

    Date getRegisterDate();

    Date getLastLoginDate();

    InetAddress getLastAddress();

    UUID getUUID();

}
