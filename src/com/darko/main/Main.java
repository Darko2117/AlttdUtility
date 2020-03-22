package com.darko.main;

import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.utilities.logging.Logging;
import com.darko.main.utilities.other.APIs;
import com.darko.main.utilities.config.ConfigSetup;
import com.darko.main.utilities.other.ConsoleColors;
import com.darko.main.utilities.other.Register;

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

        Main.getInstance().getLogger().info("Utility plugin started... Teri bring back " + ConsoleColors.GREEN_BOLD
                + "Sugar Cane" + ConsoleColors.RESET + " please thank you :)");
        System.out.println("--------------------------------------------------");
    }

}
