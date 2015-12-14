package com.fcmcpe.nuclear.login.provider;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import com.fcmcpe.nuclear.login.data.LoginData;

import java.net.InetAddress;
import java.sql.Date;
import java.util.UUID;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login.data in project NuclearLogin .
 */
public class LoginDataImpl implements LoginData {

    IPlayer player;

    String hash;

    Date registerDate;

    Date lastLoginDate;

    InetAddress lastAddress;

    UUID uuid;

    public LoginDataImpl(
            Server nukkit,
            String player,
            String hash,
            Date registerDate,
            Date lastLoginDate,
            String lastAddress,
            UUID uuid){
        try {
            this.player = nukkit.getOfflinePlayer(player);
            this.hash = hash;
            this.registerDate = registerDate;
            this.lastLoginDate = lastLoginDate;
            this.lastAddress = InetAddress.getByName(lastAddress);
            this.uuid = uuid;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public LoginDataImpl(Server nukkit, Player player, String hash){
        this(nukkit, player.getName().toLowerCase().trim(), hash, null, null, player.getAddress(), player.getUniqueId());
    }

    public LoginDataImpl(Server nukkit, Player player){
        this(nukkit, player, null);
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public Date getRegisterDate() {
        return registerDate;
    }

    @Override
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    @Override
    public InetAddress getLastAddress() {
        return lastAddress;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }
}
