package com.fcmcpe.nuclear.regions.provider;

import com.fcmcpe.nuclear.core.provider.DataProvider;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.regions.data.RegionAddResult;
import com.fcmcpe.nuclear.regions.data.RegionData;

import java.util.Collection;

/**
 * Created on 2015/12/18 by xtypr.
 * Package com.fcmcpe.nuclear.regions.provider in project NuclearPlugins .
 */
public interface RegionDataProvider extends DataProvider {

    Collection<RegionData> getAllData() throws ProviderException;

    RegionAddResult addRegion(RegionData data) throws ProviderException;

    void removeRegion(RegionData data) throws ProviderException;

    RegionData updatePerm(RegionData data) throws ProviderException;

}
