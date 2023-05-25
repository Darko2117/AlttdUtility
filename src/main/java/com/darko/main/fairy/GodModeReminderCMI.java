package com.darko.main.fairy;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.common.API.APIs;
import org.bukkit.Bukkit;
import com.Zrips.CMI.CMI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GodModeReminderCMI {
    private static long lastSentReminder;
    private static int delay;

    public static void initiate() {

        CMI cmiAPI = APIs.getCMIAPI();

        delay = AlttdUtility.getInstance().getConfig().getInt("GodModeReminder.Delay");
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

        if (System.currentTimeMillis() < lastSentReminder + delay)
            return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (cmiAPI.getPlayerManager().getUser(player).isGod()) {
                if (player == null) return;
                Methods.sendConfigMessage(player, "Messages.GodModeReminder");
            }
        }
                lastSentReminder = System.currentTimeMillis();
            }
        }.runTaskTimerAsynchronously(AlttdUtility.getInstance(), 200, 200));
    }
}
