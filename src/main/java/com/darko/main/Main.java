package com.darko.main;

import com.darko.main.utilities.logging.Logging;
import com.darko.main.utilities.other.APIs;
import com.darko.main.utilities.other.ConfigSetup;
import com.darko.main.utilities.other.ConsoleColors;
import com.darko.main.utilities.other.Register;
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

        Register.RegisterEvents();

        APIs.APIConnect();

        Register.RegisterCommands();

        Main.getInstance().getLogger().info("Utility plugin started... Let's see how long it takes Teri to figure out I changed this message. Hit me up on discord when you find this :)");
        System.out.println("--------------------------------------------------");
    }

}
