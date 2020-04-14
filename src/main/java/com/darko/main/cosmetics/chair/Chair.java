package com.darko.main.cosmetics.chair;

import com.darko.main.Main;
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

            if (GlobalVariables.chairEnabled.contains(player)) {
                GlobalVariables.chairEnabled.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.ChairDisabled")));
            } else {
                GlobalVariables.chairEnabled.add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.ChairEnabled")));
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.NoPermission")));
        }
        return false;
    }
}
