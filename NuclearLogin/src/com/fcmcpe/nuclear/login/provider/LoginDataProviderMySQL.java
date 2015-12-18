package com.fcmcpe.nuclear.login.provider;

import cn.nukkit.Server;
import com.fcmcpe.nuclear.core.provider.ProviderException;
import com.fcmcpe.nuclear.login.data.LoginData;
import com.fcmcpe.nuclear.login.data.PlayerCheckResult;
import com.fcmcpe.nuclear.login.data.PlayerLoginResult;
import com.fcmcpe.nuclear.login.data.PlayerUnregisterResult;

import java.sql.*;

/**
 * Created on 2015/12/9 by xtypr.
 * Package com.fcmcpe.nuclear.login.provider in project NuclearLogin .
 */
public class LoginDataProviderMySQL implements LoginDataProvider {

    Server nukkit;
    String defaultSQL;

    String url;

    public LoginDataProviderMySQL(Server nukkit, String defaultSQL, String url){
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
    public PlayerLoginResult login(LoginData data) throws ProviderException {
        String playerName = data.getPlayer().getName().toLowerCase().trim();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement("CALL `NuclearPlayerLogin`(?, ?, ?, ?)");
            statement.setString(1, playerName);
            statement.setString(2, data.getHash());
            statement.setString(3, data.getLastAddress().getHostAddress());
            statement.setString(4, data.getUUID().toString());
            ResultSet resultSet = statement.executeQuery();
            PlayerLoginResult result;
            boolean passwordCorrect = false;
            boolean userExist = false;
            while (resultSet.next()) {
                passwordCorrect = resultSet.getBoolean("success");
                userExist = resultSet.getBoolean("exist");
            }
            result = new PlayerLoginResultImpl(data.getPlayer(), passwordCorrect, userExist);
            statement.close();
            return result;
        } catch (Exception e){
            throw new ProviderException("Exception caught when logging in "+playerName+ ":", e);
        }
    }

    @Override
    public void logout(LoginData data) throws ProviderException {
        String playerName = data.getPlayer().getName().toLowerCase().trim();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement("CALL `NuclearPlayerLogout`(?, ?, ?)");
            statement.setString(1, playerName);
            statement.setString(2, data.getLastAddress().getHostAddress());
            statement.setString(3, data.getHash());
            statement.execute();
            statement.close();
        } catch (Exception e){
            throw new ProviderException("Exception caught when logging out "+playerName+ ":", e);
        }
    }

    @Override
    public PlayerCheckResult checkPlayer(LoginData data) throws ProviderException {
        String playerName = data.getPlayer().getName().toLowerCase().trim();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement("CALL `NuclearPlayerCheck`(?, ?, ?)");
            statement.setString(1, playerName);
            statement.setString(2, data.getLastAddress().getHostAddress());
            statement.setString(3, data.getUUID().toString());
            ResultSet resultSet = statement.executeQuery();
            boolean exist = false;
            boolean matchIP = false;
            boolean matchUUID = false;
            int countIP = 0;
            int countUUID = 0;
            PlayerCheckResult result;
            while (resultSet.next()) {
                exist = resultSet.getBoolean("exist");
                matchIP = resultSet.getBoolean("matchIP");
                matchUUID = resultSet.getBoolean("matchUUID");
                countIP = resultSet.getInt("countIP");
                countUUID = resultSet.getInt("countUUID");
            }
            result = new PlayerCheckResultImpl(exist, matchIP, matchUUID, countIP, countUUID);
            statement.close();
            return result;
        } catch (Exception e){
            throw new ProviderException("Exception caught when logging in "+playerName+ ":", e);
        }
    }

    @Override
    public PlayerUnregisterResult unregisterIfPresent(LoginData data) throws ProviderException {
        String playerName = data.getPlayer().getName().toLowerCase().trim();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement("CALL `NuclearPlayerUnregister`(?, ?)");
            statement.setString(1, playerName);
            statement.setString(2, data.getHash());
            ResultSet resultSet = statement.executeQuery();
            PlayerUnregisterResult result;
            boolean passwordCorrect = false;
            boolean userExist = false;
            while (resultSet.next()) {
                passwordCorrect = resultSet.getBoolean("hash");
                userExist = resultSet.getBoolean("exist");
            }
            result = new PlayerUnregisterResultImpl(data.getPlayer(), passwordCorrect, userExist);
            statement.close();
            return result;
        } catch (Exception e){
            throw new ProviderException("Exception caught when unregister "+playerName+ ":", e);
        }
    }
    @Override
    public void registerIfAbsent(LoginData data) throws ProviderException {
        String playerName = data.getPlayer().getName().toLowerCase().trim();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(
                    "CALL `NuclearPlayerRegister`(?, ?, ?, ?)"
            );
            statement.setString(1, playerName);
            statement.setString(2, data.getHash());
            statement.setString(3, data.getLastAddress().getHostAddress());
            statement.setString(4, data.getUUID().toString());
            statement.execute();
            statement.close();
        } catch (Exception e){
            throw new ProviderException("Exception caught when register "+playerName+ ":", e);
        }
    }

    @Override
    public boolean selfCheck() {
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(
                    "DESCRIBE `NuclearLogin`;"
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
