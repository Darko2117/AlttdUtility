package com.darko.main.darko.godModeReminder;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.database.Database;
import com.darko.main.common.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GodModeReminder {

    private static long lastSentReminder;
    private static int delay;

    public static void initiate() {


        delay = AlttdUtility.getInstance().getConfig().getInt("GodModeReminder.Delay");
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                if (System.currentTimeMillis() < lastSentReminder + delay)
                    return;

                try {
                    String statement = "SELECT UUID FROM users WHERE god_mode_enabled = true";
                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    while (rs.next()) {
                        String uuidString = rs.getString((1));
                        UUID uuid = UUID.fromString(uuidString);
                        Player player = Bukkit.getPlayer(uuid);
                        if (player == null) return;
                        Methods.sendConfigMessage(player, "Messages.GodModeReminder");

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                lastSentReminder = System.currentTimeMillis();
            }
        }.runTaskTimerAsynchronously(AlttdUtility.getInstance(), 200, 200));
    }
}
