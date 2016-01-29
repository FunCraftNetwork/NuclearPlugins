package com.fcmcpe.nuclear.regions.data;

import com.fcmcpe.nuclear.regions.permission.RegionPermission;

import java.util.Map;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.data in project NuclearPlugins .
 */
public interface RegionData {

    int getID();

    RegionBox getBox();

    Map<String, Integer> getPermissions();

    default String getOwnerName() {
        final String[] found = {null};
        getPermissions().forEach((s, i) -> {
            if (RegionPermission.of(i).hasPermission(RegionPermission.OWNER)) found[0] = s;
        });
        return found[0];
    }

}
