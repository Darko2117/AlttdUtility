package com.darko.main;

import com.darko.main.common.API.APIs;
import com.darko.main.common.reload.Reload;
import com.darko.main.common.reload.ReloadType;
import com.darko.main.darko.flags.Flags;
import com.darko.main.darko.logging.Logging;
import org.bukkit.plugin.java.JavaPlugin;

public class AlttdUtility extends JavaPlugin {

    private static AlttdUtility instance;

    public static AlttdUtility getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {

        instance = this;

        APIs.connectWorldGuardAPI();
        if (APIs.isWorldGuardFound())
            Flags.FlagsEnable();

    }

    @Override
    public void onEnable() {

        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

        Reload.reload(ReloadType.ON_ENABLE);

        AlttdUtility.getInstance().getLogger().info("Utility plugin started...");
        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

    }

    @Override
    public void onDisable() {

        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

        Logging.isShuttingDown = true;

        AlttdUtility.getInstance().getLogger().info("Blocking the main thread until logging completes...");

        while (!Logging.logWritingIsDone) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException ingored) {
            }

        }

        AlttdUtility.getInstance().getLogger().info("Logging done, bye bye!");
        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

    }

}
