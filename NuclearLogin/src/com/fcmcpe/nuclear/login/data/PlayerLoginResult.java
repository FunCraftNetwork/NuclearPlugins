package com.fcmcpe.nuclear.login.data;

import cn.nukkit.IPlayer;

/**
 * Created on 2015/12/12 by xtypr.
 * Package com.fcmcpe.nuclear.login.data in project NuclearLogin .
 */
public interface PlayerLoginResult {

    IPlayer getPlayer();

    boolean isPasswordCorrect();

    boolean isUserExist();
}
