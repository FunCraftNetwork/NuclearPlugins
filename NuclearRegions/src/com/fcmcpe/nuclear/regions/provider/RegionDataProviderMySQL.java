package com.fcmcpe.nuclear.regions.provider;

import cn.nukkit.Server;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.regions.data.RegionData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

/**
 * Created on 2015/12/19 by xtypr.
 * Package com.fcmcpe.nuclear.regions.provider in project NuclearPlugins .
 */
public class RegionDataProviderMySQL implements RegionDataProvider {
    Server nukkit;
    String defaultSQL;

    String url;

    public RegionDataProviderMySQL(Server nukkit, String defaultSQL, String url){
        this.nukkit = nukkit;
        this.defaultSQL = defaultSQL;
        this.url = url;
    }

    @Override
    public Collection<RegionData> getAllData() throws ProviderException {
        return null;
    }

    @Override
    public RegionData addRegion(RegionData data) throws ProviderException {
        return null;
    }

    @Override
    public void removeRegion(RegionData data) throws ProviderException {

    }

    @Override
    public RegionData updatePerm(RegionData data) throws ProviderException {
        return null;
    }

    public void open() throws ProviderException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String[] sqlArray = defaultSQL.split("-- Cutting Line --");
            for (String aDefaultSQL : sqlArray) {
                statement.execute(aDefaultSQL);
            }
            statement.close();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new ProviderException("Exception caught when opening MySQL provider:", e);
        }
    }

    @Override
    public void close() throws ProviderException {

    }

    @Override
    public boolean selfCheck() {
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(
                    "DESCRIBE `NuclearRegions-Location`;DESCRIBE `NuclearRegions-Permission`;"
            );
            statement.execute();
            statement.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
