package com.darko.main;

import com.darko.main.config.ConfigSetup;
import com.darko.main.database.Database;
import com.darko.main.utilities.logging.Logging;
import com.darko.main.API.APIs;
import com.darko.main.register.Register;
import com.darko.main.utilities.spawnLimiter.SpawnLimiter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Main.getInstance().getLogger().info("--------------------------------------------------");

        Main.getInstance().saveDefaultConfig();

        Logging.initiate();

        ConfigSetup.configSetup();

        SpawnLimiter.reloadLimitedEntities();

        APIs.APIConnect();

        Register.registerEvents();

        Register.registerCommands();

        Database.initiate();

        Main.getInstance().getLogger().info("Utility plugin started...");
        Main.getInstance().getLogger().info("--------------------------------------------------");
    }

}
