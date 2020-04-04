package com.darko.main.utilities.logging.ClaimLogging;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;

import com.darko.main.Main;

public class ClaimCreatedLogFile {
    public static void Initiate() {
        try {
            Main.getInstance().saveDefaultConfig();
            File dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(),
                    "/logs/claim-created-log.txt");
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().info("Data file failed to load...");
        }
    }
}
