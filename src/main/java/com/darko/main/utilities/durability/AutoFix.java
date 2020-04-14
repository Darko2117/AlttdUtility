package com.darko.main.utilities.durability;

import com.darko.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AutoFix implements CommandExecutor {

    public static List<Player> AutoFix = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("utility.autofix")) {
            if (!AutoFix.contains(player)) {
                AutoFix.add(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.AutoFixEnabled")));
            } else {
                AutoFix.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.AutoFixDisabled")));
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.NoPermission")));
        }
        return false;
    }
}
