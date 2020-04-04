package com.darko.main.utilities.config;

import com.darko.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ConfigReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(sender.hasPermission("utility.configreload")){
            Main.getInstance().reloadConfig();
           sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
        }
        return false;
    }
}
