package com.darko.main.common.database;

import com.darko.main.AlttdUtility;
import com.darko.main.darko.autofix.AutoFix;
import com.darko.main.darko.customCommandMacro.CustomCommandMacroCommand;
import com.darko.main.darko.godMode.GodMode;
import com.darko.main.darko.itemPickup.ItemPickup;
import com.darko.main.darko.petGodMode.PetGodMode;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
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

    public static List<Player> unreadFreezemailPlayers = new ArrayList<>();

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
                createNicknamesTable();
                createRequestedNicknamesTable();

                for (String message : logConfirmationMessages) AlttdUtility.getInstance().getLogger().info(message);

                Database.reloadAllCaches();

                AlttdUtility.getInstance().getLogger().info("Connected to the database!");

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
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

                        statement = "INSERT INTO users(UUID, Username, autofix_enabled, block_item_pickup_enabled, god_mode_enabled, pet_god_mode_enabled) VALUES("
                                + "'" + uuid + "', "
                                + "'" + username + "', "
                                + "false" + ", "
                                + "false" + ", "
                                + "false" + ", "
                                + "false"
                                + ");";

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

                //Custom command macro table

                statement = "SELECT * FROM custom_command_macro WHERE UUID = '" + uuid + "';";

                try {

                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    while (rs.next()) {

                        String existingUsername = rs.getString("Username");

                        if (!existingUsername.equals(username)) {

                            statement = "UPDATE custom_command_macro SET Username = '" + username + "' WHERE UUID = '" + uuid + "';";

                            Database.connection.prepareStatement(statement).executeUpdate();
                            AlttdUtility.getInstance().getLogger().info(username + " had a different username in the custom_command_macro table (" + existingUsername + "). Updated it.");

                        }

                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin_ReloadLoadedValues(PlayerJoinEvent event) {

        if (Database.connection == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.reloadAllCaches();
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuit_ReloadLoadedValues(PlayerQuitEvent event) {

        if (Database.connection == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.reloadAllCaches();
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    public static void reloadAllCaches() {

        try {

            AutoFix.cacheAllPlayers();

            ItemPickup.cacheAllPlayers();

            GodMode.cacheAllPlayers();

            PetGodMode.cacheAllPlayers();

            CustomCommandMacroCommand.cacheMacros();

            String statement = "SELECT uuid FROM freeze_message WHERE IsRead = 0;";
            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
            unreadFreezemailPlayers.clear();
            while (rs.next()) {
                Player player = Bukkit.getPlayer(UUID.fromString(rs.getString("uuid")));
                if (Bukkit.getOnlinePlayers().contains(player)) unreadFreezemailPlayers.add(player);
            }
            FreezeMailPlayerListener.refreshON();

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
        columns.add("ALTER TABLE users ADD god_mode_enabled BOOLEAN NOT NULL");
        columns.add("ALTER TABLE users ADD pet_god_mode_enabled BOOLEAN NOT NULL");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    static void createCustomCommandMacroTable() {

        try {
            String customChatMessageTableQuery = "CREATE TABLE IF NOT EXISTS custom_command_macro("
                    + "ID INT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (ID))";
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
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    static void createFreezeMessageTable() {

        try {
            String freezeMessageTableQuery = "CREATE TABLE IF NOT EXISTS freeze_message("
                    + "Id int NOT NULL AUTO_INCREMENT, "
                    + "PRIMARY KEY (Id))";
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

    static void createNicknamesTable() {

        try {
            String nicknamesTableQuery = "CREATE TABLE IF NOT EXISTS nicknames("
                    + "uuid VARCHAR(48) NOT NULL,"
                    + "PRIMARY KEY (uuid))";
            connection.prepareStatement(nicknamesTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE nicknames ADD nickname VARCHAR(192)");
        columns.add("ALTER TABLE nicknames ADD date_changed BIGINT default 0");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

    static void createRequestedNicknamesTable() {

        try {
            String requestedNicknamesTableQuery = "CREATE TABLE IF NOT EXISTS requested_nicknames("
                    + "uuid VARCHAR(48) NOT NULL,"
                    + "PRIMARY KEY (uuid))";
            connection.prepareStatement(requestedNicknamesTableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE requested_nicknames ADD nickname VARCHAR(192)");
        columns.add("ALTER TABLE requested_nicknames ADD date_requested BIGINT default 0");
        for (String string : columns) {
            try {
                connection.prepareStatement(string).executeUpdate();
                logConfirmationMessages.add(string + " executed!");
            } catch (Throwable ignored) {
            }
        }

    }

}
