package com.darko.main.utilities.logging;

import com.darko.main.utilities.logging.ClaimLogging.ClaimCreatedLogFile;
import com.darko.main.utilities.logging.ClaimLogging.ClaimDeletedLogFile;
import com.darko.main.utilities.logging.ClaimLogging.ClaimExpiredLogFile;
import com.darko.main.utilities.logging.ClaimLogging.ClaimModifiedLogFile;
import com.darko.main.utilities.logging.EggLogging.EggLogFile;
import com.darko.main.utilities.logging.ItemsLogging.DroppedItemsLogFile;
import com.darko.main.utilities.logging.ItemsLogging.ItemPlacedInItemFrameLogFile;
import com.darko.main.utilities.logging.ItemsLogging.MCMMORepairUseLogFile;
import com.darko.main.utilities.logging.PrizeLogging.CratePrizeLogFile;
import com.darko.main.utilities.logging.SpawnLogging.SpawnLimitReachedLogFile;
import org.bukkit.Location;

public class Logging {

    public static void Initiate() {

        LogDirectory.CreateLogDirectory();
        ClaimCreatedLogFile.Initiate();
        ClaimDeletedLogFile.Initiate();
        ClaimExpiredLogFile.Initiate();
        ClaimModifiedLogFile.Initiate();
        EggLogFile.Initiate();
        CratePrizeLogFile.Initiate();
        SpawnLimitReachedLogFile.Initiate();
        DroppedItemsLogFile.Initiate();
        ItemPlacedInItemFrameLogFile.Initiate();
        MCMMORepairUseLogFile.Initiate();
    }



    public static String getBetterLocationString(Location location){

        StringBuilder message = new StringBuilder();

        String X = String.valueOf(location.getBlockX());
        String Y = String.valueOf(location.getBlockY());
        String Z = String.valueOf(location.getBlockZ());

        String dimension = location.getWorld().getEnvironment().toString();
        String worldName = location.getWorld().getName();

        message.append("World: "+ worldName + " Dimension: " + dimension + " X:" + X + " Y:" + Y + " Z:" + Z);

        return message.toString();
    }





}
