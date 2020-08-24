package com.darko.main.database;

import com.darko.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Database implements Listener {

    public static Connection connection = null;

    public static List<Player> chairEnabledPlayers = new ArrayList<>();
    public static List<Player> autofixEnabledPlayers = new ArrayList<>();
    public static List<Player> blockItemPickupEnabledPlayers = new ArrayList<>();

    public static void initiate() {

        new BukkitRunnable() {
            @Override
            public void run() {

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
                columns.add("ALTER TABLE users ADD chair_enabled BOOLEAN NOT NULL");
                columns.add("ALTER TABLE users ADD autofix_enabled BOOLEAN NOT NULL");
                columns.add("ALTER TABLE users ADD block_item_pickup_enabled BOOLEAN NOT NULL");
                for (String string : columns) {
                    try {
                        connection.prepareStatement(string).executeUpdate();
                    } catch (Throwable ignored) {
                    }
                }

                Database.reloadLoadedValues();

                Main.getInstance().getLogger().info("Connected to the database!");

            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (Database.connection == null) return;

                Player player = event.getPlayer();
                String uuid = player.getUniqueId().toString();
                String username = player.getName();

                String statement = "SELECT * FROM users WHERE UUID = '" + uuid + "';";

                try {

                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    if (!rs.next()) {

                        statement = "INSERT INTO users(UUID, Username, chair_enabled, autofix_enabled, block_item_pickup_enabled) VALUES("
                                + "'" + uuid + "', "
                                + "'" + username + "', "
                                + "false"
                                + "false"
                                + "false"
                                + ");";

                        Database.connection.prepareStatement(statement).executeUpdate();
                        Main.getInstance().getLogger().info(username + " was not in the database, adding them now.");

                    } else {

                        String existingUsername = rs.getString("Username");

                        if (!existingUsername.equals(username)) {

                            statement = "UPDATE users SET Username = '" + username + "' WHERE UUID = '" + uuid + "';";

                            Database.connection.prepareStatement(statement).executeUpdate();
                            Main.getInstance().getLogger().info(username + " had a different username in the database (" + existingUsername + "). Updated it.");

                        }

                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    @EventHandler
    public void onPlayerJoin1(PlayerJoinEvent event) {

        if (Database.connection == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.reloadLoadedValues();
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

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

            statement = "SELECT UUID FROM users WHERE chair_enabled = true;";
            rs = Database.connection.prepareStatement(statement).executeQuery();
            chairEnabledPlayers.clear();
            while (rs.next()) {
                Player player = Bukkit.getPlayer(UUID.fromString(rs.getString("UUID")));
                if (Bukkit.getOnlinePlayers().contains(player)) chairEnabledPlayers.add(player);
            }

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

}
