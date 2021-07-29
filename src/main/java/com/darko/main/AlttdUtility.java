package com.darko.main;

import com.darko.main.common.API.APIs;
import com.darko.main.common.register.Register;
import com.darko.main.darko.flags.Flags;
import com.darko.main.darko.rebootWhitelist.RebootWhitelist;
import com.darko.main.darko.reload.ReloadCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class AlttdUtility extends JavaPlugin {

    public static AlttdUtility instance;

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

        //instance = this;
        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

        ReloadCommand.reload();

        Register.registerCommands();

        RebootWhitelist.disableAfterBoot();

        AlttdUtility.getInstance().getLogger().info("Utility plugin started...");
        AlttdUtility.getInstance().getLogger().info("--------------------------------------------------");

    }

}
