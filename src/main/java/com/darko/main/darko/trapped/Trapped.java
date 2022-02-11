package com.darko.main.darko.trapped;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Trapped implements CommandExecutor, Listener {

    static List<TrappedObject> trappedObjects = new ArrayList<>();

    public static void initiate() {
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

                String timeString = new Methods().getTimeStringFromIntSeconds(seconds);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.TrappedCommandOnCooldown")).replace("%time%", timeString));

            }

        }

        return true;

    }

    private TrappedObject getTrappedObjectFromPlayer(Player player) {

        for (TrappedObject trappedObject : trappedObjects) {
            if (trappedObject.getPlayer().getUniqueId().equals(player.getUniqueId())) return trappedObject;
        }

        return null;

    }

    private Boolean needToCreateNew(Player player) {

        Boolean found = false;

        for (TrappedObject trappedObject : trappedObjects) {
            if (trappedObject.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                found = true;
            }
        }

        return !found;

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        TrappedObject trappedObject = getTrappedObjectFromPlayer(event.getEntity());

        if (trappedObject == null) return;

        trappedObject.setStatus(Status.ON_COOLDOWN);

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        TrappedObject trappedObject = getTrappedObjectFromPlayer(event.getPlayer());

        if (trappedObject == null) return;

        trappedObject.setStatus(Status.ON_COOLDOWN);

    }

}



























