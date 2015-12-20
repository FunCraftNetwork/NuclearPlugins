package com.fcmcpe.nuclear.regions;

import cn.nukkit.Server;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.regions.data.RegionData;
import com.fcmcpe.nuclear.regions.provider.RegionDataProvider;

import java.util.ArrayList;
import java.util.Collection;

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
}
