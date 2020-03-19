package com.darko.main.utilities.logging.prizelogger;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;

import com.darko.main.Main;

public class CratePrizeLogFile {

    public static void Initiate() {
        try {
            Main.getInstance().saveDefaultConfig();
            File dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(),
                    "/logs/crate-prize-log.txt");
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().info("Data file failed to load...");
        }
    }
}
