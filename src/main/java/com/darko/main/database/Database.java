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
//                createClaimCreatedLogTable();
//                createClaimDeletedLogTable();
//                createClaimExpiredLogTable();
//                createClaimModifiedLogTable();
//                createEggLogTable();
//                createDroppedItemsLogTable();
//                createItemsPlacedInItemFramesLogTable();
//                createItemsTakenOutOfItemFramesLogTable();
//                createMCMMORepairUseLogTable();
//                createPickedUpItemsLogTable();
//                createCratePrizeLogTable();
//                createSpawnLimiterLogTable();
//                createUIClicksLogTable();

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
                                + "false" + ", "
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

    static void createUsersTable(){

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

    }

//    static void createClaimCreatedLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_claimcreated("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_claimcreated ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimcreated ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_claimcreated ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimcreated ADD CreatorUUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimcreated ADD CreatorUsername TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimcreated ADD Area TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimcreated ADD LowestY TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createClaimDeletedLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_claimdeleted("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_claimdeleted ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimdeleted ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_claimdeleted ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimdeleted ADD OwnerUUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimdeleted ADD OwnerUsername TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimdeleted ADD Area TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimdeleted ADD LowestY TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createClaimExpiredLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_claimexpired("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_claimexpired ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimexpired ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_claimexpired ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimexpired ADD OwnerUUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimexpired ADD OwnerUsername TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimexpired ADD Area TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimexpired ADD LowestY TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createClaimModifiedLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_claimmodified("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_claimmodified ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimmodified ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_claimmodified ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimmodified ADD OwnerUUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimmodified ADD OwnerUsername TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimmodified ADD Area TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_claimmodified ADD LowestY TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createEggLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_egg("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_egg ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_egg ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_egg ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_egg ADD ThrowerUUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_egg ADD ThrowerUsername TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_egg ADD Location TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_egg ADD InClaimOf TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createDroppedItemsLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_droppeditems("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_droppeditems ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_droppeditems ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_droppeditems ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_droppeditems ADD UUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_droppeditems ADD Username TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_droppeditems ADD Location TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_droppeditems ADD Item TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createItemsPlacedInItemFramesLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_itemsplacedinitemframes("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_itemsplacedinitemframes ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemsplacedinitemframes ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_itemsplacedinitemframes ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemsplacedinitemframes ADD UUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemsplacedinitemframes ADD Username TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemsplacedinitemframes ADD Location TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemsplacedinitemframes ADD Item TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createItemsTakenOutOfItemFramesLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_itemstakenoutofitemframes("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_itemstakenoutofitemframes ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemstakenoutofitemframes ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_itemstakenoutofitemframes ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemstakenoutofitemframes ADD UUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemstakenoutofitemframes ADD Username TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemstakenoutofitemframes ADD Location TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_itemstakenoutofitemframes ADD Item TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createMCMMORepairUseLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_mcmmorepairuse("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_mcmmorepairuse ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_mcmmorepairuse ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_mcmmorepairuse ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_mcmmorepairuse ADD UUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_mcmmorepairuse ADD Username TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_mcmmorepairuse ADD Item TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createPickedUpItemsLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_pickedupitems("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_pickedupitems ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_pickedupitems ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_pickedupitems ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_pickedupitems ADD UUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_pickedupitems ADD Username TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_pickedupitems ADD Location TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_pickedupitems ADD Item TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createCratePrizeLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_crateprize("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_crateprize ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_crateprize ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_crateprize ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_crateprize ADD UUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_crateprize ADD Username TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_crateprize ADD Text TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createSpawnLimiterLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_spawnlimiter("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_spawnlimiter ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_spawnlimiter ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_spawnlimiter ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_spawnlimiter ADD Entity TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_spawnlimiter ADD Location TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_spawnlimiter ADD InClaimOf TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }
//
//    static void createUIClicksLogTable(){
//
//        try {
//            String usersTableQuery = "CREATE TABLE IF NOT EXISTS logging_uiclicks("
//                    + "ID INT NOT NULL AUTO_INCREMENT,"
//                    + "PRIMARY KEY (ID))";
//            connection.prepareStatement(usersTableQuery).executeUpdate();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        List<String> columns = new ArrayList<>();
//        columns.add("ALTER TABLE logging_uiclicks ADD Server TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_uiclicks ADD Time BIGINT NOT NULL");
//        columns.add("ALTER TABLE logging_uiclicks ADD Date TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_uiclicks ADD UUID TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_uiclicks ADD Username TEXT NOT NULL");
//        columns.add("ALTER TABLE logging_uiclicks ADD Message TEXT NOT NULL");
//        for (String string : columns) {
//            try {
//                connection.prepareStatement(string).executeUpdate();
//            } catch (Throwable ignored) {
//            }
//        }
//
//    }

}
