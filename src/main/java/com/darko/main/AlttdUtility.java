package com.darko.main;

import com.darko.main.register.Register;
import com.darko.main.utilities.rebootWhitelist.RebootWhitelist;
import com.darko.main.utilities.reload.ReloadCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class AlttdUtility extends JavaPlugin {

    public static AlttdUtility instance;

    public static AlttdUtility getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

        ReloadCommand.reload();

        Register.registerCommands();

        RebootWhitelist.disableAfterBoot();

        AlttdUtility.getInstance().getLogger().info("Utility plugin started...");
        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

    }

}
