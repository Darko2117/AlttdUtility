package com.darko.main.darko.trapped;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Trapped implements CommandExecutor {

    static List<TrappedObject> trappedObjects = new ArrayList<>();

    public static void initialize() {
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                if (trappedObjects.isEmpty()) return;

                List<TrappedObject> toRemove = new ArrayList<>();

                for (TrappedObject trappedObject : trappedObjects) {

                    trappedObject.update();

                    if (trappedObject.getStatus().equals(Status.AVAILABLE)) {
                        toRemove.add(trappedObject);
                    }

                }

                for (TrappedObject trappedObject : toRemove) {
                    trappedObjects.remove(trappedObject);
                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 1, 1));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.TrappedCommand")) return true;

        if (!(commandSender instanceof Player)) return true;

        Player player = (Player) commandSender;

        if (needToCreateNew(player)) {
            TrappedObject trappedObject = new TrappedObject(player);
            trappedObjects.add(trappedObject);
        } else {
            TrappedObject trappedObject = getTrappedObjectFromPlayer(player);
            if (trappedObject.getStatus().equals(Status.AWAITING_TELEPORT) || trappedObject.getStatus().equals(Status.ON_COOLDOWN)) {

                Integer seconds = trappedObject.getRemainingCooldownSeconds();
                Integer hours = 0, minutes = 0;

                while (seconds >= 60) {
                    minutes++;
                    seconds -= 60;
                }
                while (minutes >= 60) {
                    hours++;
                    minutes -= 60;
                }

                Boolean displayHours = hours > 0;
                Boolean displayMinutes = minutes > 0;
                if (seconds == 0) seconds = 1;

                String time = "";

                if (displayHours) time = time.concat(String.valueOf(hours)).concat(" hours ");
                if (displayMinutes) time = time.concat(String.valueOf(minutes)).concat(" minutes ");
                time = time.concat(String.valueOf(seconds)).concat(" seconds");

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.TrappedCommandOnCooldown")).replace("%time%", time));

            }

        }

        return true;

    }

    private TrappedObject getTrappedObjectFromPlayer(Player player) {

        for (TrappedObject trappedObject : trappedObjects) {
            if (trappedObject.getPlayer().equals(player)) return trappedObject;
        }

        return null;

    }

    private Boolean needToCreateNew(Player player) {

        Boolean found = false;

        for (TrappedObject trappedObject : trappedObjects) {
            if (trappedObject.getPlayer().equals(player)) {
                found = true;
            }
        }

        return !found;

    }

}