package com.fcmcpe.nuclear.rgcommands;

import com.fcmcpe.nuclear.regions.data.RegionBox;
import com.fcmcpe.nuclear.regions.data.RegionData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snake1999 on 2016/1/29.
 * Package com.fcmcpe.nuclear.rgcommands in project NuclearPlugins.
 */
class $LimitedRegionData$ implements RegionData {

    RegionBox box;
    Map<String, Integer> perm = new HashMap<>();

    static $LimitedRegionData$ of(RegionBox box) {
        $LimitedRegionData$ data = new $LimitedRegionData$();
        data.box = box;
        return data;
    }

    @Override
    public int getID() {
        return -1;
    }

    @Override
    public RegionBox getBox() {
        return box;
    }

    @Override
    public Map<String, Integer> getPermissions() {
        return perm;
    }
}
