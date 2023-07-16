package com.darko.main.fairy;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.common.API.APIs;
import java.util.HashMap;
import org.bukkit.Bukkit;
import com.Zrips.CMI.CMI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GodModeReminderCMI implements Listener {

    private static final HashMap<Player, Long> enabledTimes = new HashMap<>();

    public static void initiate() {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.GodModeReminder"))
            return;

        if (!APIs.isCMIFound())
            return;

        int delay = AlttdUtility.getInstance().getConfig().getInt("GodModeReminderDelay");

        CMI cmiAPI = APIs.getCMIAPI();

        enabledTimes.clear();

        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (player.hasPermission("utility.ignoregodmodereminders"))
                        continue;

                    boolean inHashMap = enabledTimes.containsKey(player);
                    boolean isGod = cmiAPI.getPlayerManager().getUser(player).isGod();

                    if (!inHashMap && isGod) {

                        enabledTimes.put(player, System.currentTimeMillis());

                    } else if (inHashMap && !isGod) {

                        enabledTimes.remove(player);

                    } else if (inHashMap && isGod && enabledTimes.get(player) + (delay * 1000) < System.currentTimeMillis()) {

                        Methods.sendConfigMessage(player, "Messages.GodModeReminder");

                        enabledTimes.put(player, System.currentTimeMillis());

                    }

                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 200, 200));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {

        enabledTimes.remove(playerQuitEvent.getPlayer());

    }

}
