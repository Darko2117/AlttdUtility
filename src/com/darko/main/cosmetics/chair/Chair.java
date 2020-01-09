package com.darko.main.cosmetics.chair;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chair implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("utility.chair")) {

            if (GlobalVariables.chairEnabled.containsKey(player.getUniqueId())) {

                if (GlobalVariables.chairEnabled.get(player.getUniqueId()) == true) {
                    GlobalVariables.chairEnabled.put(player.getUniqueId(), false);
                } else if (GlobalVariables.chairEnabled.get(player.getUniqueId()) == false) {
                    GlobalVariables.chairEnabled.put(player.getUniqueId(), true);
                }
            } else {
                GlobalVariables.chairEnabled.put(player.getUniqueId(), true);
            }

            if (GlobalVariables.chairEnabled.get(player.getUniqueId()) == true) {
                player.sendMessage(ChatColor.GREEN + "Chair mode enabled, right click on any stairs to sit on them.");
            } else {
                player.sendMessage(ChatColor.RED + "Chair mode disabled.");
            }

        } else {
            player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
        }

        return false;
    }
}
