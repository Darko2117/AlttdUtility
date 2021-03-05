package com.darko.main.darko.timedTips;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimedTips {

    static List<String> messages = new ArrayList<>();
    static Integer delay = null;

    public static void initiate() {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.TimedTips")) return;

        messages = AlttdUtility.getInstance().getConfig().getStringList("TimedTips.Messages");
        delay = AlttdUtility.getInstance().getConfig().getInt("TimedTips.Delay") * 20 * 60;

        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                String message = ChatColor.translateAlternateColorCodes('&', messages.get(new Random().nextInt(messages.size())).replace("\\n", "\n"));

                for (String message1 : message.split("\\n"))
                    AlttdUtility.getInstance().getLogger().info(message1);

                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (!player.hasPermission("utility.seetimedtips")) continue;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

                }

            }
        }.runTaskTimerAsynchronously(AlttdUtility.getInstance(), delay, delay));

    }

}