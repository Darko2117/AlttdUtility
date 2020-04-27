package com.darko.main;

import com.darko.main.config.ConfigSetup;
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

        //Database.Initiate();

        Main.getInstance().getLogger().info("Utility plugin started... Let's see how long it takes Teri to figure out I changed this message. Hit me up on discord when you find this :)");
        System.out.println("--------------------------------------------------");
    }

}
