package com.darko.main.database;

import com.darko.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Database implements Listener {

    public static Connection connection = null;

    public static List<Player> autofixEnabledPlayers = new ArrayList<>();
    public static List<Player> blockItemPickupEnabledPlayers = new ArrayList<>();

    public static void initiate() {

        new BukkitRunnable() {
            @Override
            public void run() {

                connection = null;

                String driver, ip, port, name, username, password;

                driver = Main.getInstance().getConfig().getString("Database.driver");
                ip = Main.getInstance().getConfig().getString("Database.ip");
                port = Main.getInstance().getConfig().getString("Database.port");
                name = Main.getInstance().getConfig().getString("Database.name");
                username = Main.getInstance().getConfig().getString("Database.username");
                password = Main.getInstance().getConfig().getString("Database.password");

                String url = "jdbc:mysql://" + ip + ":" + port + "/" + name;

                try {
                    connection = DriverManager.getConnection(url, username, password);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    Main.getInstance().getLogger().severe("Connection to the database failed!");
                    connection = null;
                    return;
                }

                createUsersTable();
                createCustomChatMessageTable();
                createCommandOnJoinTable();
                createFreezeMessageTable();
                createNicknamesTable();

                Database.reloadLoadedValues();

                Main.getInstance().getLogger().info("Connected to the database!");

            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin_TablesUpdate(PlayerJoinEvent event) {

        if (Database.connection == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {

                Player player = event.getPlayer();
                String uuid = player.getUniqueId().toString();
                String username = player.getName();

                String statement;

                //Users table

                statement = "SELECT * FROM users WHERE UUID = '" + uuid + "';";

                try {

                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    if (!rs.next()) {

                        statement = "INSERT INTO users(UUID, Username, autofix_enabled, block_item_pickup_enabled) VALUES("
                                + "'" + uuid + "', "
                                + "'" + username + "', "
                                + "false" + ", "
                                + "false"
                                + ");";

                        Database.connection.prepareStatement(statement).executeUpdate();
                        Main.getInstance().getLogger().info(username + " was not in the database, adding them now.");

                    } else {

                        String existingUsername = rs.getString("Username");

                        if (!existingUsername.equals(username)) {

                            statement = "UPDATE users SET Username = '" + username + "' WHERE UUID = '" + uuid + "';";

                            Database.connection.prepareStatement(statement).executeUpdate();
                            Main.getInstance().getLogger().info(username + " had a different username in the users table (" + existingUsername + "). Updated it.");

                        }

                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                //Custom chat message table

                statement = "SELECT * FROM custom_chat_message WHERE UUID = '" + uuid + "';";

                try {

                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    while (rs.next()) {

                        String existingUsername = rs.getString("Username");

                        if (!existingUsername.equals(username)) {

                            statement = "UPDATE custom_chat_message SET Username = '" + username + "' WHERE UUID = '" + uuid + "';";

                            Database.connection.prepareStatement(statement).executeUpdate();
                            Main.getInstance().getLogger().info(username + " had a different username in the custom_chat_message table (" + existingUsername + "). Updated it.");

                        }

                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin_ReloadLoadedValues(PlayerJoinEvent event) {

        if (Database.connection == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.reloadLoadedValues();
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit_ReloadLoadedValues(PlayerQuitEvent event) {

        if (Database.connection == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.reloadLoadedValues();
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    public static void reloadLoadedValues() {

        try {

            ResultSet rs;
            String statement;

            statement = "SELECT UUID FROM users WHERE autofix_enabled = true;";
            rs = Database.connection.prepareStatement(statement).executeQuery();
            autofixEnabledPlayers.clear();
            while (rs.next()) {
                Player player = Bukkit.getPlayer(UUID.fromString(rs.getString("UUID")));
                if (Bukkit.getOnlinePlayers().contains(player)) autofixEnabledPlayers.add(player);
            }

            statement = "SELECT UUID FROM users WHERE block_item_pickup_enabled = true;";
            rs = Database.connection.prepareStatement(statement).executeQuery();
            blockItemPickupEnabledPlayers.clear();
            while (rs.next()) {
                Player player = Bukkit.getPlayer(UUID.fromString(rs.getString("UUID")));
                if (Bukkit.getOnlinePlayers().contains(player)) blockItemPickupEnabledPlayers.add(player);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    static void createUsersTable() {

        try {
            String usersTableQuery = "CREATE TABLE IF NOT EXISTS users("
                    + "ID INT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (ID))";
            connection.prepareStatement(usersTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE users ADD UUID TEXT NOT NULL");
        columns.add("ALTER TABLE users ADD Username TEXT NOT NULL");
        columns.add("ALTER TABLE users ADD autofix_enabled BOOLEAN NOT NULL");
        columns.add("ALTER TABLE users ADD block_item_pickup_enabled BOOLEAN NOT NULL");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
            } catch (Throwable ignored) {
            }
        }

    }

    static void createCustomChatMessageTable() {

        try {
            String customChatMessageTableQuery = "CREATE TABLE IF NOT EXISTS custom_chat_message("
                    + "ID INT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (ID))";
            connection.prepareStatement(customChatMessageTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE custom_chat_message ADD UUID TEXT NOT NULL");
        columns.add("ALTER TABLE custom_chat_message ADD Username TEXT NOT NULL");
        columns.add("ALTER TABLE custom_chat_message ADD MessageName TEXT NOT NULL");
        columns.add("ALTER TABLE custom_chat_message ADD Message TEXT NOT NULL");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
            } catch (Throwable ignored) {
            }
        }

    }

    static void createCommandOnJoinTable() {

        try {
            String commandOnJoinTableQuery = "CREATE TABLE IF NOT EXISTS command_on_join("
                    + "ID INT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (ID))";
            connection.prepareStatement(commandOnJoinTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE command_on_join ADD Username TEXT NOT NULL");
        columns.add("ALTER TABLE command_on_join ADD Command TEXT NOT NULL");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
            } catch (Throwable ignored) {
            }
        }

    }

    static void createFreezeMessageTable() {
        try {
            String freezeMessageTableQuery = "CREATE TABLE IF NOT EXISTS freeze_message("
                    + "Id int NOT NULL AUTO_INCREMENT,"
                    + "UUID VARCHAR(36) NOT NULL,"
                    + "Message VARCHAR(256) NOT NULL,"
                    + "IsRead SMALLINT(1) DEFAULT 0,"
                    + "PRIMARY KEY (Id))";
            connection.createStatement().execute(freezeMessageTableQuery);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    static void createNicknamesTable(){
        final String nicknamesTableQuery = "CREATE TABLE IF NOT EXISTS nicknames (uuid VARCHAR(48) NOT NULL, nickname VARCHAR(192), PRIMARY KEY(uuid));";
        try {
            connection.createStatement().execute(nicknamesTableQuery);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
