package com.darko.main.database;


import com.darko.main.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    private Connection connection;
    private String drivers, ip, port, database, username, password;

    /**
     * Sets information for the database and opens the connection.
     *
     * @throws SQLException If it can't open the connection.
     */
    private Database() throws SQLException {
        this.drivers = Main.getInstance().getConfig().getString("Database.driver");
        this.ip = Main.getInstance().getConfig().getString("Database.ip");
        this.port = Main.getInstance().getConfig().getString("Database.port");
        this.database = Main.getInstance().getConfig().getString("Database.name");
        this.username = Main.getInstance().getConfig().getString("Database.username");
        this.password = Main.getInstance().getConfig().getString("Database.password");
        instance = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    instance.openConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    /**
     * Opens the connection if it's not already open.
     *
     * @throws SQLException If it can't create the connection.
     */
    public void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection(
                    "jdbc:" + drivers + "://" + ip + ":" + port + "/" + database, username,
                    password);
        }
    }

    /**
     * Returns the connection for the database
     *
     * @return Returns the connection.
     */
    public static Connection getConnection() {
        try {
            instance.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance.connection;
    }

    /**
     * Sets the connection for this instance
     *
     */
    public static void Initiate(){
        try {
            instance = new Database();
        } catch (SQLException e) {
        }
    }

}
