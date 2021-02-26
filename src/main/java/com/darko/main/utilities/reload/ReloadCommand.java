package com.darko.main.utilities.reload;

import com.darko.main.API.APIs;
import com.darko.main.AlttdUtility;
import com.darko.main.config.ConfigSetup;
import com.darko.main.cosmetics.sit.Sit;
import com.darko.main.database.Database;
import com.darko.main.register.Register;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
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

        AlttdUtility.getInstance().saveDefaultConfig();
        AlttdUtility.getInstance().reloadConfig();

        SpawnLimiter.reload();

        Sit.startCheckingSeats();

        FreezeMailPlayerListener.startFreezemailRepeater();

        RebootWhitelist.reload();

        Logging.initiate();

        ConfigSetup.configSetup();

        APIs.APIConnect();

        Register.registerEvents();

        new BukkitRunnable() {
            @Override
            public void run() {
                Database.initiate();
                if (Database.connection != null)
                    Database.reloadLoadedValues();
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

}
