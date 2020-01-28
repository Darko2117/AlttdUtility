package com.darko.main.cosmetics.chair;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.darko.main.Main;

public class Chair implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("utility.chair")) {

            if (GlobalVariables.chairEnabled.contains(player)) {
                GlobalVariables.chairEnabled.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.ChairDisabled")));
            } else {
                GlobalVariables.chairEnabled.add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.ChairEnabled")));
            }

            /*
             * if (GlobalVariables.chairEnabled.containsKey(player.getUniqueId())) {
             * 
             * if (GlobalVariables.chairEnabled.get(player.getUniqueId()) == true) {
             * GlobalVariables.chairEnabled.put(player.getUniqueId(), false); } else if
             * (GlobalVariables.chairEnabled.get(player.getUniqueId()) == false) {
             * GlobalVariables.chairEnabled.put(player.getUniqueId(), true); } } else {
             * GlobalVariables.chairEnabled.put(player.getUniqueId(), true); }
             * 
             * if (GlobalVariables.chairEnabled.get(player.getUniqueId()) == true) {
             * player.sendMessage(ChatColor.translateAlternateColorCodes('&',
             * Main.getInstance().getConfig().getString("Messages.ChairEnabled"))); } else {
             * player.sendMessage(ChatColor.translateAlternateColorCodes('&',
             * Main.getInstance().getConfig().getString("Messages.ChairDisabled"))); }
             * 
             * } else { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
             * Main.getInstance().getConfig().getString("Messages.NoPermission"))); }
             */
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.NoPermission")));
        }
        return false;
    }
}
