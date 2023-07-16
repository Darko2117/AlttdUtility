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
    private static final int delay = AlttdUtility.getInstance().getConfig().getInt("GodModeReminderDelay");

    public static void initiate() {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.GodModeReminder"))
            return;

        CMI cmiAPI = APIs.getCMIAPI();

        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (cmiAPI.getPlayerManager().getUser(player).isGod()) {
                    Methods.sendConfigMessage(player, "Messages.GodModeReminder");
                }
            }
            }
        }.runTaskTimer(AlttdUtility.getInstance(), delay, delay)); //using non-asynchronous task timer because APIs should never be accessed by them
    }
}