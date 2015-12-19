package com.fcmcpe.nuclear.regions.data;

import com.fcmcpe.nuclear.regions.math.RegionBox;
import com.fcmcpe.nuclear.regions.permission.RegionPermission;

import java.util.Map;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.data in project NuclearPlugins .
 */
public interface RegionData {

    int getID();

    RegionBox getBox();

    Map<String, RegionPermission> getPermissions();

}
