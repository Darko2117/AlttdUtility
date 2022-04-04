package com.darko.main.darko.aprilfools;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AprilFools {

    private static final long minSoundDelayMiliseconds = 900000;
    private static final long maxSoundDelayMiliseconds = 1200000;

    private static boolean isAprilFools = false;

    private static final List<AprilFoolsObject> aprilFoolsObjectList = new ArrayList<>();

    public static void initiate() {
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                isAprilFools = LocalDate.now().getMonthValue() == 4 && LocalDate.now().getDayOfMonth() == 1;

                if (!isAprilFools) return;

                long timeNow = System.currentTimeMillis();

                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (getAprilFoolsObjectFromPlayer(player) == null) {
                        aprilFoolsObjectList.add(new AprilFoolsObject(player, timeNow, getRandomSoundDelay()));
                        AlttdUtility.getInstance().getLogger().info("AprilFools: Added " + player.getName());
                    }

                }

                for (AprilFoolsObject aprilFoolsObject : aprilFoolsObjectList) {
                    if (timeNow >= aprilFoolsObject.getLastSoundTime() + aprilFoolsObject.getNextSoundDelay()) {

                        Player player = aprilFoolsObject.getPlayer();

                        if (player.isOnline()) {
                            Sound sound = getRandomSound();
                            player.playSound(player.getLocation(), sound, 1, 1);
                            AlttdUtility.getInstance().getLogger().info("AprilFools: Played " + sound.name() + " for " + player.getName());
                        }

                        aprilFoolsObject.setLastSoundTime(timeNow);
                        aprilFoolsObject.setNextSoundDelay(getRandomSoundDelay());

                    }
                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 20, 20));
    }

    private static AprilFoolsObject getAprilFoolsObjectFromPlayer(Player player) {
        for (AprilFoolsObject aprilFoolsObject : aprilFoolsObjectList) {
            if (aprilFoolsObject.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                aprilFoolsObject.setPlayer(player);
                return aprilFoolsObject;
            }
        }
        return null;
    }

    private static long getRandomSoundDelay() {
        return minSoundDelayMiliseconds + (long) (Math.random() * (maxSoundDelayMiliseconds - minSoundDelayMiliseconds));
    }

    private static Sound getRandomSound() {
        Sound sound = null;
        while (sound == null || sound.name().toLowerCase().contains("step")) {
            sound = Sound.values()[new Random().nextInt(Sound.values().length)];
        }
        return sound;
    }

}
