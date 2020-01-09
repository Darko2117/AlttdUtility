package com.darko.main.utilities.portal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class onPortalUse implements Listener {

    @EventHandler
    public void onNether(PlayerPortalEvent e) {
        if (e.getPlayer().hasPermission("utility.nether")) {
            Location from = (Location) e.getFrom();
            Location to = (Location) e.getTo();
            Player player = (Player) e.getPlayer();
            if (from.getWorld().getEnvironment().equals(Environment.NETHER)) {
                player.sendMessage(ChatColor.RED + "You have been teleported to spawn.");
                e.setCancelled(true);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi spawn " + player.getName());
            } else if (to.getWorld().getEnvironment().equals(Environment.NETHER)
                    && from.getWorld().getEnvironment().equals(Environment.NORMAL)) {
                player.sendMessage(ChatColor.RED + "When exiting the nether you will be teleported to spawn.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnd(PlayerPortalEvent e) {
        if (e.getPlayer().hasPermission("utility.end")) {
            Location from = (Location) e.getFrom();
            Location to = (Location) e.getTo();
            if (from.getWorld().getEnvironment().equals(Environment.NORMAL)
                    && to.getWorld().getEnvironment().equals(Environment.THE_END)) {
                e.getTo().setX(100);
                e.getTo().setY(51);
                e.getTo().setZ(0);
                for (Integer x = 2; x >= -2; x--) {
                    for (Integer z = 2; z >= -2; z--) {
                        Block block = Bukkit.getWorld(to.getWorld().getName())
                                .getBlockAt(e.getTo().clone().subtract(x, 2, z));
                        if (!block.getType().equals(Material.OBSIDIAN)) {
                            block.setType(Material.OBSIDIAN);
                        }
                    }
                }
                for (Integer x = 2; x >= -2; x--) {
                    for (Integer z = 2; z >= -2; z--) {
                        for (Integer y = -1; y <= 1; y++) {
                            Block block = Bukkit.getWorld(to.getWorld().getName())
                                    .getBlockAt(e.getTo().clone().subtract(x, y, z));
                            if (!block.getType().equals(Material.AIR)) {
                                block.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
            System.out.println(e.getPlayer().getName() + " used the end portal. Teleporting them to X: "
                    + e.getTo().getX() + " Y: " + e.getTo().getY() + " Z: " + e.getTo().getZ());
        }
    }

}
