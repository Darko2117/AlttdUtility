package com.darko.main.utilities.servermsg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.darko.main.Main;

public class Servermsg implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender.hasPermission("utility.servermsg")) {
            if (args.length >= 2) {

                String receiver = null;
                Boolean receiverIsPlayer = false;

                for (Player p : Bukkit.getOnlinePlayers()) {

                    if (p.getName().equalsIgnoreCase(args[0])) {
                        receiver = p.getName();
                        receiverIsPlayer = true;
                        break;
                    }

                }

                if (receiver == null) {
                    receiver = args[0];
                }

                StringBuilder message = new StringBuilder();

                for (Integer i = 1; i < args.length; i++) {
                    if (i != 1) {
                        message.append(" ");
                    }
                    message.append(args[i]);
                }

                if (receiverIsPlayer) {
                    Bukkit.getPlayer(receiver).sendMessage(ChatColor.translateAlternateColorCodes('&', message.toString()).replace("^n", "\n"));
                    sender.sendMessage(ChatColor.GOLD + "Following message sent to the user: " + ChatColor.RESET + receiver + "\n" + ChatColor.translateAlternateColorCodes('&', message.toString()).replace("^n", "\n"));
                } else {
                    Bukkit.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', message.toString()).replace("^n", "\n"), receiver);
                    sender.sendMessage(ChatColor.GOLD + "Following message sent to users with the permission: " + ChatColor.RESET + receiver + "\n" + ChatColor.translateAlternateColorCodes('&', message.toString()).replace("^n", "\n"));
                }

            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.servermsgInvalidUsage")));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.NoPermission")));
        }

        return false;
    }
}