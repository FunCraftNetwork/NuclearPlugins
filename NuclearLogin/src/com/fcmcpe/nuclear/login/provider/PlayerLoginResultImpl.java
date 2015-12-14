package com.fcmcpe.nuclear.login.provider;

import cn.nukkit.IPlayer;
import com.fcmcpe.nuclear.login.data.PlayerLoginResult;

/**
 * Created on 2015/12/12 by xtypr.
 * Package com.fcmcpe.nuclear.login.data in project NuclearLogin .
 */
class PlayerLoginResultImpl implements PlayerLoginResult {

    private IPlayer player;

    private boolean correct;
    private boolean exist;

    public PlayerLoginResultImpl(IPlayer player, boolean correct, boolean exist){
        this.player = player;
        this.correct = correct;
        this.exist = exist;
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isPasswordCorrect() {
        return correct;
    }

    @Override
    public boolean isUserExist() {
        return exist;
    }

}
