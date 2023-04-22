package com.darko.main.darko.tpPunch;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TPPunch implements CommandExecutor, Listener, TabCompleter {

    static HashMap<UUID, Location> playerTPLocationHashMap = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.TPPunchCommand"))
            return true;

        if (!(commandSender instanceof Player)) {
            new Methods().sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length < 3) {
            new Methods().sendConfigMessage(player, "Messages.InvalidUsageTPPunchCommand");
            return true;
        }

        Double X, Y, Z;
        try {
            X = Double.parseDouble(strings[0]);
            Y = Double.parseDouble(strings[1]);
            Z = Double.parseDouble(strings[2]);
        } catch (Throwable throwable) {
            new Methods().sendConfigMessage(player, "Messages.InvalidUsageTPPunchCommand");
            return true;
        }

        World dimension = null;
        if (strings.length >= 4) {
            dimension = Bukkit.getWorld(strings[3]);
            if (dimension == null) {
                new Methods().sendConfigMessage(player, "Messages.InvalidUsageTPPunchCommand");
                return true;
            }
        }

        Location teleportLocation;
        if (dimension != null) {
            teleportLocation = new Location(dimension, X, Y, Z);
        } else {
            teleportLocation = new Location(player.getWorld(), X, Y, Z);
        }

        playerTPLocationHashMap.put(player.getUniqueId(), teleportLocation);
        String locationString = "X:" + teleportLocation.getBlockX() + " Y:" + teleportLocation.getBlockY() + " Z:" + teleportLocation.getBlockZ() + " Dimension: " + teleportLocation.getWorld().getName();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.ValidUsageTPPunchCommand").replace("%location%", locationString)));

        return true;

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player))
            return;
        if (((Player) event.getDamager()).getInventory().getItemInMainHand().getType() != Material.AIR)
            return;

        Player player = (Player) event.getDamager();

        if (!playerTPLocationHashMap.containsKey(player.getUniqueId()))
            return;

        Entity entity = event.getEntity();

        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
        particleBuilder.count(500);
        particleBuilder.location(entity.getBoundingBox().getCenter().toLocation(entity.getWorld()));
        particleBuilder.offset(entity.getBoundingBox().getWidthX() / 2, entity.getBoundingBox().getHeight() / 3, entity.getBoundingBox().getWidthZ() / 2);
        particleBuilder.color(255, 255, 255);
        particleBuilder.spawn();

        Location teleportLocation = playerTPLocationHashMap.get(player.getUniqueId());
        entity.teleport(teleportLocation);

        String locationString = "X:" + teleportLocation.getBlockX() + " Y:" + teleportLocation.getBlockY() + " Z:" + teleportLocation.getBlockZ() + " Dimension: " + teleportLocation.getWorld().getName();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.EntityTeleportedTPPunchCommand").replace("%location%", locationString)));
        AlttdUtility.getInstance().getLogger().info(player.getName() + " tp punched " + entity.getName() + " to " + locationString);

        playerTPLocationHashMap.remove(player.getUniqueId());

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerInteract2(PlayerInteractEvent event) {

        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;

        if (cancelPlayerTPPunch(event.getPlayer()))
            event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerQuitEvent event) {

        cancelPlayerTPPunch(event.getPlayer());

    }

    Boolean cancelPlayerTPPunch(Player player) {

        if (!playerTPLocationHashMap.containsKey(player.getUniqueId()))
            return false;

        playerTPLocationHashMap.remove(player.getUniqueId());

        new Methods().sendConfigMessage(player, "Messages.TPPunchCancelled");

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.TPPunchCommand"))
            return null;

        if (strings.length == 4) {

            List<String> worlds = new ArrayList<>();
            for (World world : Bukkit.getWorlds())
                worlds.add(world.getName());

            List<String> completions = new ArrayList<>();

            for (String string : worlds) {
                if (string.startsWith(strings[3])) {
                    completions.add(string);
                }
            }

            return completions;

        }

        return null;

    }

}
