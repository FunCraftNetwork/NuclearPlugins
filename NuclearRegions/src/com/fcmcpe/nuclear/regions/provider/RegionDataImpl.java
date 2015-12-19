package com.fcmcpe.nuclear.regions.provider;

import com.fcmcpe.nuclear.regions.data.RegionData;
import com.fcmcpe.nuclear.regions.math.RegionBox;

import java.util.Map;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.provider in project NuclearPlugins .
 */
public class RegionDataImpl implements RegionData {

    private int id;
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;
    private String world;
    private Map<String, Integer> perm;

    RegionDataImpl(int id, RegionBox box, Map<String, Integer> perm) {
        this.id = id;
        this.x1 = box.getMinX();
        this.y1 = box.getMinY();
        this.z1 = box.getMinZ();
        this.x2 = box.getMaxX();
        this.y2 = box.getMaxY();
        this.z2 = box.getMaxZ();
        this.world = box.getWorld();
        this.perm = perm;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public RegionBox getBox() {
        return RegionBox.of(x1, y1, z1, x2, y2, z2, world);
    }

    @Override
    public Map<String, Integer> getPermissions() {
        return perm;
    }
}
