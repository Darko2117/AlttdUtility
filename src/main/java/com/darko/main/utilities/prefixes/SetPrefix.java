package com.darko.main.utilities.prefixes;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPrefix implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length >= 2) {
            if (args[1].length() <= 10) {

                Player actedPlayer = null;

                String prefix = args[1];

                for (Player p : Bukkit.getOnlinePlayers()) {

                    if (p.getName().equalsIgnoreCase(args[0])) {
                        actedPlayer = p;
                    }

                }

                if (actedPlayer == null) {

                    Methods.sendConfigMessage(player, "Messages.PlayerNotFound");
                    return true;

                }

                for (String group : Main.getInstance().getConfig().getStringList("PrefixAvailableGroups")) {

                    if (actedPlayer.hasPermission("group." + group)) {

                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + actedPlayer.getName() + " meta setprefix 100 " + prefix);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
                                .getString("Messages.PrefixSetConfirmedMessage").replace("%prefix%", prefix).replace("%player%", actedPlayer.getName())));

                    }

                }

            } else {

                Methods.sendConfigMessage(player, "Messages.InvalidPrefixLengthMessage");

            }

        }

        return true;

    }

}
