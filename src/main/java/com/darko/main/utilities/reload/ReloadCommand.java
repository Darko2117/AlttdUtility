package com.darko.main.utilities.reload;

import com.darko.main.API.APIs;
import com.darko.main.Main;
import com.darko.main.config.ConfigSetup;
import com.darko.main.database.Database;
import com.darko.main.register.Register;
import com.darko.main.utilities.logging.Logging;
import com.darko.main.utilities.rebootWhitelist.RebootWhitelist;
import com.darko.main.utilities.spawnLimiter.SpawnLimiter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        reload();

        sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");

        return false;

    }

    public static void reload() {

        Main.getInstance().saveDefaultConfig();
        Main.getInstance().reloadConfig();

        SpawnLimiter.reload();

        RebootWhitelist.reload();

        Logging.initiate();

        ConfigSetup.configSetup();

        APIs.APIConnect();

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.initiate();
                if (Database.connection != null)
                    Database.reloadLoadedValues();
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

}
