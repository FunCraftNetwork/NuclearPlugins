package com.fcmcpe.nuclear.economy.provider;

import cn.nukkit.Server;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.economy.data.MoneyData;

import java.sql.*;

/**
 * Created on 2015/12/16 by xtypr.
 * Package com.fcmcpe.nuclear.economy.provider in project NuclearPlugins .
 */
public class MoneyDataProviderMySQL implements MoneyDataProvider {
    Server nukkit;
    String defaultSQL;

    String url;

    public MoneyDataProviderMySQL(Server nukkit, String defaultSQL, String url){
        this.nukkit = nukkit;
        this.defaultSQL = defaultSQL;
        this.url = url;
    }

    @Override
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
    public void registerIfAbsent(MoneyData data) throws ProviderException {
        String playerName = data.getPlayer().getName().toLowerCase().trim();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement("CALL `NuclearMoneyRegister`(?)");
            statement.setString(1, playerName);
            statement.execute();
        } catch (Exception e){
            throw new ProviderException("Exception caught when register money data for "+playerName+ ":", e);
        }
    }

    @Override
    public void payMoney(String from, String to, long money) throws ProviderException {

    }

    @Override
    public MoneyData getMoney(MoneyData data) throws ProviderException {
        String playerName = data.getPlayer().getName().toLowerCase().trim();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement("CALL `NuclearMoneyCheck`(?)");
            statement.setString(1, playerName);
            ResultSet rs = statement.executeQuery();
            String name = "";
            //boolean userExist = false;
            long money = 0;
            while (rs.next()) {
                name = rs.getString("name");
                //userExist = rs.getBoolean("exist");
                money = rs.getLong("money");
            }
            return new MoneyDataImpl(Server.getInstance(), name, money);
        } catch (Exception e){
            throw new ProviderException("Exception caught when getting money data for "+playerName+ ":", e);
        }
    }

    @Override
    public boolean selfCheck() {
        return false;
    }
}
