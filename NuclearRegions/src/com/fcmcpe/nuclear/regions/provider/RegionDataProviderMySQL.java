package com.fcmcpe.nuclear.regions.provider;

import cn.nukkit.Server;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.regions.data.RegionAddResult;
import com.fcmcpe.nuclear.regions.data.RegionData;
import com.fcmcpe.nuclear.regions.math.RegionBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        Collection<RegionData> dataCollection = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet rs2 = statement.executeQuery("SELECT * FROM `NuclearRegions-Permission`;");
            Map<Integer, Map<String, Integer>> permissions = new HashMap<>();
            while (rs2.next()) {
                Map<String, Integer> permission = new HashMap<>();
                permission.putIfAbsent(rs2.getString("name"), rs2.getInt("perm"));
                permissions.putIfAbsent(rs2.getInt("idRegion"), permission);
            }
            ResultSet rs1 = statement.executeQuery("SELECT * FROM `NuclearRegions-Location`;");
            while (rs1.next()) {
                int fromX = rs1.getInt("fromX");
                int toX = fromX + rs1.getInt("deltaX");
                int fromY = rs1.getInt("fromY");
                int toY = fromY + rs1.getInt("deltaY");
                int fromZ = rs1.getInt("fromZ");
                int toZ = fromZ + rs1.getInt("deltaZ");
                String world = rs1.getString("world");
                RegionBox box = RegionBox.of(fromX, fromY, fromZ, toX, toY, toZ, world);
                int id = rs1.getInt("idRegion");
                RegionData data = new RegionDataImpl(id, box, permissions.getOrDefault(id, new HashMap<>()));
                dataCollection.add(data);
            }
            statement.close();
            return dataCollection;
        } catch (Exception e){
            throw new ProviderException("Exception caught when fetching all data:", e);
        }
    }

    @Override
    public RegionAddResult addRegion(RegionData data) throws ProviderException {
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement("CALL `NuclearRegionAdd`(?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, data.getBox().getWorld());
            statement.setInt(2, data.getBox().getMinX());
            statement.setInt(3, data.getBox().getMaxX() - data.getBox().getMinX());
            statement.setInt(4, data.getBox().getMinY());
            statement.setInt(5, data.getBox().getMaxY() - data.getBox().getMinY());
            statement.setInt(6, data.getBox().getMinZ());
            statement.setInt(7, data.getBox().getMaxZ() - data.getBox().getMinZ());
            ResultSet rs = statement.executeQuery();
            int idRegion = -1;
            boolean conflict = false;
            while (rs.next()) {
                idRegion = rs.getInt("idRegion");
                conflict = rs.getBoolean("conflict");
            }
            return new RegionAddResultImpl(new RegionDataImpl(idRegion, data.getBox(), new HashMap<>()), conflict);
        } catch (Exception e) {
            throw new ProviderException("Exception caught when adding region:", e);
        }
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
