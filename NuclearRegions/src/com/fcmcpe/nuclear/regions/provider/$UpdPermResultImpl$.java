package com.fcmcpe.nuclear.regions.provider;

import com.fcmcpe.nuclear.regions.data.RegionPermUpdateResult;

/**
 * Created by Snake1999 on 2016/1/29.
 * Package com.fcmcpe.nuclear.regions.provider in project NuclearPlugins.
 */
class $UpdPermResultImpl$ implements RegionPermUpdateResult {
    int regionID = -1;

    boolean succeeded = false;

    $UpdPermResultImpl$(int regionID, boolean succeeded) {
        this.regionID = regionID;
        this.succeeded = succeeded;
    }

    @Override
    public int getID() {
        return regionID;
    }

    @Override
    public boolean succeeded() {
        return succeeded;
    }
}
