package com.darko.main.common.database;

import com.darko.main.AlttdUtility;
import com.darko.main.darko.autofix.AutoFix;
import com.darko.main.darko.customCommandMacro.CustomCommandMacroCommand;
import com.darko.main.darko.godMode.GodMode;
import com.darko.main.darko.itemPickup.ItemPickup;
import com.darko.main.darko.magnet.Magnet;
import com.darko.main.darko.petGodMode.PetGodMode;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database implements Listener {

    public static Connection connection = null;

    private static final List<String> logConfirmationMessages = new ArrayList<>();

    public static void initiate() {
        new BukkitRunnable() {
            @Override
            public void run() {

                String driver = AlttdUtility.getInstance().getConfig().getString("Database.driver");
                String ip = AlttdUtility.getInstance().getConfig().getString("Database.ip");
                String port = AlttdUtility.getInstance().getConfig().getString("Database.port");
                String name = AlttdUtility.getInstance().getConfig().getString("Database.name");
                String username = AlttdUtility.getInstance().getConfig().getString("Database.username");
                String password = AlttdUtility.getInstance().getConfig().getString("Database.password");

                String url = "jdbc:" + driver + "://" + ip + ":" + port + "/" + name + "?autoReconnect=true&useSSL=false";

                try {
                    connection = DriverManager.getConnection(url, username, password);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    AlttdUtility.getInstance().getLogger().severe("Connection to the database failed!");
                    connection = null;
                    return;
                }

                createUsersTable();
                createCustomCommandMacroTable();
                createCommandOnJoinTable();
                createFreezeMessageTable();
                createVillagerShopLogTable();
                // createMoneyLogTable();

                for (String message : logConfirmationMessages)
                    AlttdUtility.getInstance().getLogger().info(message);

                Database.reloadAllCaches();

                AlttdUtility.getInstance().getLogger().info("Connected to the database!");

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin_TablesUpdate(PlayerJoinEvent event) {

        if (Database.connection == null)
            return;

        new BukkitRunnable() {
            @Override
            public void run() {

                Player player = event.getPlayer();
                String uuid = player.getUniqueId().toString();
                String username = player.getName();

                String statement;

                // Users table

                try {

                    statement = "SELECT * FROM users WHERE UUID = '" + uuid + "';";
                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    if (!rs.next()) {

                        statement = "INSERT INTO users(UUID, Username, autofix_enabled, block_item_pickup_enabled, god_mode_enabled, pet_god_mode_enabled, magnet_enabled) VALUES(" + "'" + uuid + "', " + "'" + username + "', " + "false" + ", " + "false" + ", " + "false" + ", " + "false" + ", " + "false" + ");";
                        Database.connection.prepareStatement(statement).executeUpdate();

                        AlttdUtility.getInstance().getLogger().info(username + " was not in the database, adding them now.");

                    } else {

                        String existingUsername = rs.getString("Username");

                        if (!existingUsername.equals(username)) {

                            statement = "UPDATE users SET Username = '" + username + "' WHERE UUID = '" + uuid + "';";
                            Database.connection.prepareStatement(statement).executeUpdate();

                            AlttdUtility.getInstance().getLogger().info(username + " had a different username in the users table (" + existingUsername + "). Updated it.");

                        }

                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                // Custom command macro table

                try {

                    statement = "SELECT Username FROM custom_command_macro WHERE UUID = '" + uuid + "' AND Username != '" + username + "';";
                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    if (rs.next()) {

                        String existingUsername = rs.getString("Username");

                        statement = "UPDATE custom_command_macro SET Username = '" + username + "' WHERE UUID = '" + uuid + "' AND Username != '" + username + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();

                        AlttdUtility.getInstance().getLogger().info(username + " had a different username in the custom_command_macro table (" + existingUsername + "). Updated it.");

                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                // Villager shop log table

                try {

                    statement = "SELECT user_username FROM villager_shop_log WHERE user_UUID = '" + uuid + "' AND user_username != '" + username + "';";
                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    if (rs.next()) {

                        String existingUsername = rs.getString("user_username");

                        statement = "UPDATE villager_shop_log SET user_username = '" + username + "' WHERE user_UUID = '" + uuid + "' AND user_username != '" + username + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();

                        AlttdUtility.getInstance().getLogger().info(username + " had a different username in the villager_shop_log table (" + existingUsername + "). Updated it.");

                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    public static void reloadAllCaches() {

        AutoFix.cacheAllPlayers();
        ItemPickup.cacheAllPlayers();
        GodMode.cacheAllPlayers();
        PetGodMode.cacheAllPlayers();
        CustomCommandMacroCommand.cacheAllPlayers();
        FreezeMailPlayerListener.cacheAllPlayers();
        Magnet.cacheAllPlayers();

    }

    private static void createUsersTable() {

        try {
            String usersTableQuery = "CREATE TABLE IF NOT EXISTS users(" + "ID INT NOT NULL AUTO_INCREMENT," + "PRIMARY KEY (ID))";
            connection.prepareStatement(usersTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE users ADD UUID TEXT NOT NULL");
        columns.add("ALTER TABLE users ADD Username TEXT NOT NULL");
        columns.add("ALTER TABLE users ADD autofix_enabled BOOLEAN NOT NULL");
        columns.add("ALTER TABLE users ADD block_item_pickup_enabled BOOLEAN NOT NULL");
        columns.add("ALTER TABLE users ADD god_mode_enabled BOOLEAN NOT NULL");
        columns.add("ALTER TABLE users ADD pet_god_mode_enabled BOOLEAN NOT NULL");
        columns.add("ALTER TABLE users ADD magnet_enabled BOOLEAN NOT NULL");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    private static void createCustomCommandMacroTable() {

        try {
            String customChatMessageTableQuery = "CREATE TABLE IF NOT EXISTS custom_command_macro(" + "ID INT NOT NULL AUTO_INCREMENT," + "PRIMARY KEY (ID))";
            connection.prepareStatement(customChatMessageTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE custom_command_macro ADD UUID TEXT NOT NULL");
        columns.add("ALTER TABLE custom_command_macro ADD Username TEXT NOT NULL");
        columns.add("ALTER TABLE custom_command_macro ADD MacroName TEXT NOT NULL");
        columns.add("ALTER TABLE custom_command_macro ADD Command TEXT NOT NULL");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    private static void createCommandOnJoinTable() {

        try {
            String commandOnJoinTableQuery = "CREATE TABLE IF NOT EXISTS command_on_join(" + "ID INT NOT NULL AUTO_INCREMENT," + "PRIMARY KEY (ID))";
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
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    private static void createFreezeMessageTable() {

        try {
            String freezeMessageTableQuery = "CREATE TABLE IF NOT EXISTS freeze_message(" + "Id int NOT NULL AUTO_INCREMENT, " + "PRIMARY KEY (Id))";
            connection.prepareStatement(freezeMessageTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE freeze_message ADD UUID VARCHAR(36) NOT NULL");
        columns.add("ALTER TABLE freeze_message ADD Message VARCHAR(256) NOT NULL");
        columns.add("ALTER TABLE freeze_message ADD IsRead SMALLINT(1) DEFAULT 0");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    private static void createVillagerShopLogTable() {

        try {
            String villagerShopLogTableQuery = "CREATE TABLE IF NOT EXISTS villager_shop_log(" + "ID INT NOT NULL AUTO_INCREMENT," + "PRIMARY KEY (ID))";
            connection.prepareStatement(villagerShopLogTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE villager_shop_log ADD user_UUID TEXT NOT NULL");
        columns.add("ALTER TABLE villager_shop_log ADD user_username TEXT NOT NULL");
        columns.add("ALTER TABLE villager_shop_log ADD item TEXT NOT NULL");
        columns.add("ALTER TABLE villager_shop_log ADD item_amount INT NOT NULL");
        columns.add("ALTER TABLE villager_shop_log ADD balance_change DOUBLE NOT NULL");
        columns.add("ALTER TABLE villager_shop_log ADD time BIGINT NOT NULL");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    // private static void createMoneyLogTable() {
    //
    // try {
    // String moneyLogTableQuery = "CREATE TABLE IF NOT EXISTS money_log("
    // + "ID INT NOT NULL AUTO_INCREMENT,"
    // + "PRIMARY KEY (ID))";
    // connection.prepareStatement(moneyLogTableQuery).executeUpdate();
    // } catch (Throwable throwable) {
    // throwable.printStackTrace();
    // }
    //
    // List<String> columns = new ArrayList<>();
    // columns.add("ALTER TABLE money_log ADD user_UUID TEXT NOT NULL");
    // columns.add("ALTER TABLE money_log ADD user_username TEXT NOT NULL");
    // columns.add("ALTER TABLE money_log ADD source_UUID TEXT");
    // columns.add("ALTER TABLE money_log ADD source_username TEXT");
    // columns.add("ALTER TABLE money_log ADD from_balance DOUBLE NOT NULL");
    // columns.add("ALTER TABLE money_log ADD to_balance DOUBLE NOT NULL");
    // columns.add("ALTER TABLE money_log ADD action_type TEXT NOT NULL");
    // for (String string : columns) {
    // try {
    // connection.prepareStatement(string).executeUpdate();
    // logConfirmationMessages.add(string + " executed!");
    // } catch (Throwable ignored) {
    // }
    // }
    //
    // }

}
