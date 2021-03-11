package com.darko.main.darko.toggleScruff;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleScruff implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ToggleScruffCommand")) return true;

        if (!(sender instanceof Player)) {
            new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        if (sender.hasPermission("utility.seetimedtips")) {
            String toggleCommand = "lp user " + sender.getName() + " permission set utility.seetimedtips false";
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), toggleCommand);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Scruff's stuff" + ChatColor.YELLOW + " turned " + ChatColor.RED + "off" + ChatColor.YELLOW + ".");
        } else {
            String toggleCommand = "lp user " + sender.getName() + " permission unset utility.seetimedtips";
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), toggleCommand);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Scruff's stuff" + ChatColor.YELLOW + " turned " + ChatColor.GREEN + "on" + ChatColor.YELLOW + ".");
        }

        return true;

    }

}
