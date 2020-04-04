package com.darko.main.utilities.logging.ItemsLogging;

import com.darko.main.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

public class ItemPlacedInItemFrameLogFile {

    public static void Initiate() {
        try {
            Main.getInstance().saveDefaultConfig();
            File dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(),
                    "/logs/item-placed-in-item-frame-log.txt");
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().info("Data file failed to load...");
        }
    }

}
