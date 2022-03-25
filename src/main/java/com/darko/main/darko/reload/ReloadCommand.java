package com.darko.main.darko.reload;

import com.darko.main.common.API.APIs;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.common.config.ConfigSetup;
import com.darko.main.darko.illegalItemCheck.IllegalItemCheck;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.magnet.Magnet;
import com.darko.main.darko.savedItems.SavedItem;
import com.darko.main.darko.sit.Sit;
import com.darko.main.common.database.Database;
import com.darko.main.common.register.Register;
import com.darko.main.darko.timedTips.TimedTips;
import com.darko.main.darko.trapped.Trapped;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
import com.darko.main.darko.rebootWhitelist.RebootWhitelist;
import com.darko.main.darko.spawnLimiter.SpawnLimiterCheck;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //The order of these is important
        BukkitTasksCache.cancelRunningTasks();
        new Methods().checkConfig();
        Logging.initiate();
        ConfigSetup.configSetup();
        Logging.updateCachedLogsFromConfig();
        APIs.APIConnect();
        Register.registerEvents();

        //These can go in whatever order
        SavedItem.loadSavedItems();
        Trapped.initiate();
        SpawnLimiterCheck.reload();
        Sit.startCheckingSeats();
        RebootWhitelist.reload();
        FreezeMailPlayerListener.startFreezemailRepeater();
        TimedTips.initiate();
        IllegalItemCheck.loadIllegalItems();
        Magnet.initiate();

        Database.initiate();

        sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
        return true;

    }

}
