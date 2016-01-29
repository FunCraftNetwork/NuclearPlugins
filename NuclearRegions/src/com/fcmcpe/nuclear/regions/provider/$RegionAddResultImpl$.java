package com.fcmcpe.nuclear.regions.provider;

import com.fcmcpe.nuclear.regions.data.RegionAddResult;
import com.fcmcpe.nuclear.regions.data.RegionData;

/**
 * Created on 2015/12/20 by xtypr.
 * Package com.fcmcpe.nuclear.regions.provider in project NuclearPlugins .
 */
class $RegionAddResultImpl$ implements RegionAddResult {

    private RegionData data;
    private boolean conflict;

    $RegionAddResultImpl$(RegionData data, boolean conflict) {
        this.data = data;
        this.conflict = conflict;
    }

    @Override
    public RegionData getData() {
        return data;
    }

    @Override
    public boolean isConflict() {
        return conflict;
    }
}
