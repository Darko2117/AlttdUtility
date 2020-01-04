package com.darko.main.cosmetics.chair;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class onPlayerTeleportCommand implements Listener {

    @EventHandler
    public void onTeleport(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();
        String command = e.getMessage();
        Boolean sitting = false;
        String[] tpCommands = { "/tp", "/ptp", "/spawn", "/warp", "/home", "/back" };
        try {
            if (player.getVehicle().getCustomName().equals(GlobalVariables.ChairName)) {
                sitting = true;
            }
        } catch (Exception ex) {
        }

        if (sitting) {
            for (Integer i = 0; i < tpCommands.length; i++) {
                if (command.startsWith(tpCommands[i])) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.WHITE + "You " + ChatColor.RED + "can't" + ChatColor.WHITE
                            + " teleport while sitting.");
                }
            }
        }
    }
}