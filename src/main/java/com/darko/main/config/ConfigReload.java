package com.darko.main.config;

import com.darko.main.Main;
import com.darko.main.database.Database;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfigReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Main.getInstance().reloadConfig();

        new BukkitRunnable(){
            @Override
            public void run() {
                Database.reloadLoadedValues();
            }
        }.runTaskAsynchronously(Main.getInstance());

        sender.sendMessage(ChatColor.GREEN + "Config reloaded!");

        return false;

    }
}
