package com.darko.main.darko.toggleGC;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleGC implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ToggleGCCommand"))
            return true;

        if (!(sender instanceof Player)) {
            new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        if (sender.hasPermission("chatcontrol.notify.bungeechat")) {
            String toggleCommand = "lp user " + sender.getName() + " permission set chatcontrol.notify.bungeechat false";
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), toggleCommand);
            sender.sendMessage(ChatColor.YELLOW + "Global chat turned " + ChatColor.RED + "off" + ChatColor.YELLOW + ".");
        } else {
            String toggleCommand = "lp user " + sender.getName() + " permission unset chatcontrol.notify.bungeechat";
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), toggleCommand);
            sender.sendMessage(ChatColor.YELLOW + "Global chat turned " + ChatColor.GREEN + "on" + ChatColor.YELLOW + ".");
        }

        return true;

    }

}
