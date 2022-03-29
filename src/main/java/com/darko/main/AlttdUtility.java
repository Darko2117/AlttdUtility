package com.darko.main;

import com.darko.main.common.API.APIs;
import com.darko.main.common.Methods;
import com.darko.main.common.config.ConfigSetup;
import com.darko.main.common.database.Database;
import com.darko.main.common.register.Register;
import com.darko.main.darko.aprilfools.AprilFools;
import com.darko.main.darko.flags.Flags;
import com.darko.main.darko.illegalItemCheck.IllegalItemCheck;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.magnet.Magnet;
import com.darko.main.darko.rebootWhitelist.RebootWhitelist;
import com.darko.main.darko.savedItems.SavedItem;
import com.darko.main.darko.sit.Sit;
import com.darko.main.darko.spawnLimiter.SpawnLimiterCheck;
import com.darko.main.darko.timedTips.TimedTips;
import com.darko.main.darko.trapped.Trapped;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class AlttdUtility extends JavaPlugin {

    private static AlttdUtility instance;

    public static AlttdUtility getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {

        instance = this;

        APIs.APIConnect();
        if (APIs.WorldGuardFound) Flags.FlagsEnable();

    }

    @Override
    public void onEnable() {

        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

        //The order of these is important
        new Methods().checkConfig();
        Logging.initiate();
        ConfigSetup.configSetup();
        Logging.updateCachedLogsFromConfig();
        Register.registerEvents();
        Register.registerCommands();
        RebootWhitelist.reload();
        RebootWhitelist.disableAfterBoot();

        //These can go in whatever order
        SavedItem.loadSavedItems();
        Trapped.initiate();
        SpawnLimiterCheck.reload();
        Sit.startCheckingSeats();
        FreezeMailPlayerListener.startFreezemailRepeater();
        TimedTips.initiate();
        IllegalItemCheck.loadIllegalItems();
        Magnet.initiate();
        AprilFools.initiate();

        Database.initiate();

        AlttdUtility.getInstance().getLogger().info("Utility plugin started...");
        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

    }

}
