package com.darko.main.darko.reload;

import com.darko.main.common.API.APIs;
import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.common.config.ConfigSetup;
import com.darko.main.darko.illegalItemCheck.IllegalItemCheck;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.sit.Sit;
import com.darko.main.common.database.Database;
import com.darko.main.common.register.Register;
import com.darko.main.darko.timedTips.TimedTips;
import com.darko.main.darko.trapped.Trapped;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
import com.darko.main.darko.rebootWhitelist.RebootWhitelist;
import com.darko.main.darko.spawnLimiter.SpawnLimiter;
import org.bukkit.Bukkit;
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

        return true;

    }

    public static void reload() {

        AlttdUtility.getInstance().saveDefaultConfig();
        if (new Methods().checkConfig()) {
            AlttdUtility.getInstance().reloadConfig();
        } else {
            Bukkit.getPluginManager().disablePlugin(AlttdUtility.getInstance());
            return;
        }

        Logging.cacheLogs();

        BukkitTasksCache.cancelRunningTasks();

        Trapped.initialize();

        SpawnLimiter.reload();

        Sit.startCheckingSeats();

        RebootWhitelist.reload();

        Logging.initiate();

        ConfigSetup.configSetup();

        Logging.updateCachedLogsFromConfig();

        APIs.APIConnect();

        Register.registerEvents();

        FreezeMailPlayerListener.startFreezemailRepeater();

        TimedTips.initiate();

        IllegalItemCheck.loadIllegalItems();

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
