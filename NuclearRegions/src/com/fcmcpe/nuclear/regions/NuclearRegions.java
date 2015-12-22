package com.fcmcpe.nuclear.regions;

import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.regions.data.RegionData;
import com.fcmcpe.nuclear.regions.provider.RegionDataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created on 2015/12/20 by xtypr.
 * Package com.fcmcpe.nuclear.regions in project NuclearPlugins .
 */
public enum NuclearRegions {
    INSTANCE;

    private RegionDataProvider dataProvider;
    private Collection<RegionData> regionData = new ArrayList<>();

    private NuclearRegions() {

    }

    public RegionDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(RegionDataProvider dataProvider) throws ProviderException {
        this.dataProvider = dataProvider;
        dataProvider.open();
    }

    public Collection<RegionData> getAllRegionData(){
        return regionData;
    }

    public boolean reloadRegionData() {
        try {
            this.regionData = dataProvider.getAllData();
            return true;
        } catch (Exception e) {
            Server.getInstance().getLogger().logException(e);
            return false;
        }
    }

    public RegionData getInRegion(Position position) {
        final RegionData[] result = {null};
        regionData.forEach((r) -> {
            if (r.getBox().isInBox(position)) result[0] = r;
        });
        return result[0];
    }

    public Position getNearestNoRegion(Position position) {
        int x = position.getFloorX();
        int y = position.getFloorY();
        int z = position.getFloorZ();

        //Step 1: "Snake Algorithm"
        RegionData regionFound = getInRegion(position);
        Position flagPos = new Position(x, y, z, position.getLevel());
        boolean flagPosAdd = false;
        int flagLength = 1;
        int flagDirection = Vector3.SIDE_NORTH;
        while (regionFound != null) {
            flagPos = flagPos.getSide(flagDirection, flagLength);
            flagPosAdd = !flagPosAdd;
            if (flagPosAdd) {
                flagLength++;
                flagDirection++;
            }
            if (flagDirection > Vector3.SIDE_EAST) flagDirection = Vector3.SIDE_NORTH;
            regionFound = getInRegion(flagPos);
        }

        //todo check transparent

        return flagPos;
    }

    public Location getNearestNoRegion(Location location) {
        Position pos = getNearestNoRegion((Position)location);
        return new Location(pos.getX(), pos.getY(), pos.getZ(), location.getYaw(), location.getPitch(), pos.getLevel());
    }

    public RegionData getByID(int id) {
        final RegionData[] found = {null};
        regionData.forEach((r) -> {
            if (r.getID() == id) found[0] = r;
        });
        return found[0];
    }

    public Collection<RegionData> getByPlayer(String playerName) {
        final Collection<RegionData> found = new LinkedList<>();
        regionData.forEach((r) -> {
            if (r.getPermissions().containsKey(playerName)) found.add(r);
        });
        return found;
    }
}
