package com.fcmcpe.nuclear.rgcommands;

import cn.nukkit.IPlayer;
import cn.nukkit.level.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snake1999 on 2016/1/29.
 * Package com.fcmcpe.nuclear.rgcommands in project NuclearPlugins.
 */
public enum RGCommands {
    INSTANCE;

    private Map<IPlayer, Position> pos1Map = new HashMap<>();
    private Map<IPlayer, Position> pos2Map = new HashMap<>();

    public void setPos1(IPlayer player, Position pos1) {
        pos1Map.put(player, pos1.round());
    }

    public Position getPos1(IPlayer player) {
        return pos1Map.getOrDefault(player, null);
    }

    public void setPos2(IPlayer player, Position pos2) {
        pos2Map.put(player, pos2.round());
    }

    public Position getPos2(IPlayer player) {
        return pos2Map.getOrDefault(player, null);
    }

    public boolean isPosValid(IPlayer player) {
        Position pos1 = pos1Map.getOrDefault(player, null);
        Position pos2 = pos2Map.getOrDefault(player, null);
        if (pos1 == null || pos2 == null) return false;
        if (pos1.getLevel() != pos2.getLevel()) return false;
        return true;
    }
}
