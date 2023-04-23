package com.darko.main.common.reload;

import com.darko.main.common.API.APIs;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.common.config.Config;
import com.darko.main.common.database.Database;
import com.darko.main.common.register.Register;
import com.darko.main.darko.aprilfools.AprilFools;
import com.darko.main.darko.blockBlockPlace.BlockBlockPlace;
import com.darko.main.darko.illegalItemCheck.IllegalItemCheck;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.LoggingSearch;
import com.darko.main.darko.magnet.Magnet;
import com.darko.main.darko.rebootWhitelist.RebootWhitelist;
import com.darko.main.darko.savedItems.SavedItem;
import com.darko.main.darko.sit.Sit;
import com.darko.main.darko.spawnLimiter.SpawnLimiter;
import com.darko.main.darko.timedTips.TimedTips;
import com.darko.main.darko.trapped.Trapped;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;

public class Reload {

    public static void reload(ReloadType reloadType) {

        new Methods().checkConfig(); // Needs to be called before loading the config

        Config.configSetup();

        BukkitTasksCache.cancelRunningTasks(); // Needs to be called before any new tasks are added

        Logging.initiate();

        Logging.updateCachedLogsFromConfig(); // Reads from the config, needs to be called after Config.configSetup()

        LoggingSearch.startProgressBossBarTask();

        APIs.APIConnect();

        Register.registerEvents(); // Needs to be called after APIs.APIConnect()

        Register.registerCommands(); // Needs to be called after APIs.APIConnect()

        if (reloadType.equals(ReloadType.ON_ENABLE)) {
            RebootWhitelist.reload(); // Reads from the config, needs to be called after Config.configSetup()
            RebootWhitelist.disableAfterBoot(); // Reads from the config, needs to be called after Config.configSetup()
        }

        SavedItem.loadSavedItems(); // Reads from the config, needs to be after Config.configSetup()

        Trapped.initiate(); // Reads from the config, needs to be after Config.configSetup()

        SpawnLimiter.reload(); // Reads from the config, needs to be after Config.configSetup()

        Sit.startCheckingSeats();

        FreezeMailPlayerListener.startFreezemailRepeater(); // Reads from the config, needs to be after Config.configSetup()

        TimedTips.initiate(); // Reads from the config, needs to be after Config.configSetup()

        IllegalItemCheck.loadIllegalItems(); // Reads from the config, needs to be after Config.configSetup()

        BlockBlockPlace.initiate(); // Reads from the config, needs to be after Config.configSetup()

        Magnet.initiate();

        AprilFools.initiate();


        Database.initiate();

    }

}
