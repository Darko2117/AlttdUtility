package com.darko.main.darko.timedTips;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TimedTips {

    private static final List<String> tips = new ArrayList<>();
    private static int delay;
    private static String prefix;
    private static String suffix;
    private static long lastSentTip;

    public static void initiate() {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.TimedTips")) return;

        tips.clear();
        delay = AlttdUtility.getInstance().getConfig().getInt("TimedTips.Delay") * 1000;
        prefix = AlttdUtility.getInstance().getConfig().getString("TimedTips.Prefix");
        suffix = AlttdUtility.getInstance().getConfig().getString("TimedTips.Suffix");
        lastSentTip = System.currentTimeMillis();

        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                if (System.currentTimeMillis() < lastSentTip + delay) return;

                if (tips.isEmpty()) tips.addAll(AlttdUtility.getInstance().getConfig().getStringList("TimedTips.Tips"));

                int tipIndex = ThreadLocalRandom.current().nextInt(tips.size());

                String tip = prefix;
                tip = tip.concat("\n&f");
                tip = tip.concat(tips.get(tipIndex));
                tip = tip.concat("\n");
                tip = tip.concat(suffix);
                tip = ChatColor.translateAlternateColorCodes('&', tip);
                tip = tip.replace("\\n", "\n");

                tips.remove(tipIndex);

                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (!player.hasPermission("utility.seetimedtips")) continue;
                    player.sendMessage(tip);

                }

                lastSentTip = System.currentTimeMillis();

            }
        }.runTaskTimerAsynchronously(AlttdUtility.getInstance(), 20, 20));

    }

}