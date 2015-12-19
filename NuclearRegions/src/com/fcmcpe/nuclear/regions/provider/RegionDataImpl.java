package com.fcmcpe.nuclear.regions.provider;

import com.fcmcpe.nuclear.regions.data.RegionData;
import com.fcmcpe.nuclear.regions.math.RegionBox;
import com.fcmcpe.nuclear.regions.permission.RegionPermission;

import java.util.Map;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.provider in project NuclearPlugins .
 */
public class RegionDataImpl implements RegionData {

    private int id;
    public int x1;
    public int y1;
    public int z1;
    public int x2;
    public int y2;
    public int z2;

    RegionDataImpl(int id, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.id = id;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public RegionBox getBox() {
        return RegionBox.of(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public Map<String, RegionPermission> getPermissions() {
        return null;
    }
}
