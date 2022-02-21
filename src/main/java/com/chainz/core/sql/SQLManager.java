package com.chainz.core.sql;

import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {
    private static Connection connection;

    public static void initialize() {
        SQLManager.connectSQL(ConfigManager.get("config.yml").getString("MySQL.ip"),
                ConfigManager.get("config.yml").getString("MySQL.port"),
                ConfigManager.get("config.yml").getString("MySQL.database"),
                ConfigManager.get("config.yml").getString("MySQL.user"),
                ConfigManager.get("config.yml").getString("MySQL.pass"));
        SQLManager.createLobbyOptionsTable();
        SQLManager.createLevelTable();
        SQLManager.createPlayerSettingsTable();
        SQLManager.createNameDataTable();
    }

    public static Connection getConnection() {
        try {
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (Exception ignored) {
        }
    }

    public static void connectSQL(String ip, String port, String database, String user, String pass) {
        try {
            Bukkit.getServer().getConsoleSender().sendMessage("jdbc:mysql://" + ip + ":" + port + "/" + database + "?autoReconnect=true");
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database + "?autoReconnect=true", user, pass);
            Bukkit.getServer().getConsoleSender().sendMessage("a la base de datos correctamente");
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("ERROR: No se ha podido conectar a la base de datos: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("ERROR: se encontro JDBC Driver");
        }
    }

    public static void createLobbyOptionsTable() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS lobbyoptions(uuid VARCHAR(36) PRIMARY KEY NOT NULL,visibility ENUM('true', 'false') NOT NULL,speed ENUM('true', 'false') NOT NULL,fly ENUM('true', 'false') NOT NULL) DEFAULT CHARSET=utf8;");
            statement.execute();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("No se pudo crear la tabla: " + e.getMessage());
        }
    }

    public static void createPlayerSettingsTable() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playersettings(uuid VARCHAR(36) PRIMARY KEY NOT NULL,friendrequests ENUM('true', 'false') NOT NULL,partyrequests ENUM('true', 'false') NOT NULL,msg ENUM('true', 'false') NOT NULL,chat ENUM('true', 'false') NOT NULL,ads ENUM('true', 'false') NOT NULL) DEFAULT CHARSET=utf8;");
            statement.execute();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("No se pudo crear la tabla: " + e.getMessage());
        }
    }

    public static void createNameDataTable() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS namedata(uuid VARCHAR(36) PRIMARY KEY NOT NULL,name VARCHAR(36) NOT NULL) DEFAULT CHARSET=utf8;");
            statement.execute();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("No se pudo crear la tabla: " + e.getMessage());
        }
    }

    public static void createLevelTable() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerlevel(uuid VARCHAR(36) PRIMARY KEY NOT NULL,level INT(5) NOT NULL,xp INT(12) NOT NULL) DEFAULT CHARSET=utf8;");
            statement.execute();
        } catch (SQLException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("No se pudo crear la tabla: " + e.getMessage());
        }
    }
}
