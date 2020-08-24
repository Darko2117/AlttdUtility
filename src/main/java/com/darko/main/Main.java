package com.darko.main;

import com.darko.main.config.ConfigSetup;
import com.darko.main.database.Database;
import com.darko.main.utilities.logging.Logging;
import com.darko.main.API.APIs;
import com.darko.main.register.Register;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        System.out.println("--------------------------------------------------");

        instance = this;

        ConfigSetup.onConfigSetup();

        Logging.Initiate();

        APIs.APIConnect();

        Register.RegisterEvents();

        Register.RegisterCommands();

        Database.initiate();

        Main.getInstance().getLogger().info("Utility plugin started...");
        System.out.println("--------------------------------------------------");
    }

}
