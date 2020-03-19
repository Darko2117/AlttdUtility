package com.darko.main.utilities.logging.egglogging;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;

import com.darko.main.Main;

public class EggLogFile {

    public static void Initiate() {
        try {
            Main.getInstance().saveDefaultConfig();
            File dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(),
                    "/logs/egg-log.txt");
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().info("Data file failed to load...");
        }
    }
}
