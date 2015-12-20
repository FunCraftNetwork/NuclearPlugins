package com.fcmcpe.nuclear.regions;

import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.regions.provider.RegionDataProvider;

/**
 * Created on 2015/12/20 by xtypr.
 * Package com.fcmcpe.nuclear.regions in project NuclearPlugins .
 */
public enum NuclearRegions {
    INSTANCE;

    private RegionDataProvider dataProvider;

    private NuclearRegions() {

    }

    public RegionDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(RegionDataProvider dataProvider) throws ProviderException {
        this.dataProvider = dataProvider;
        dataProvider.open();
    }
}
