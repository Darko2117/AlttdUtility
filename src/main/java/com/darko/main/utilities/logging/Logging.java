package com.darko.main.utilities.logging;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class Logging {

    public static HashMap<String, String> logNamesAndConfigPaths = new LinkedHashMap<>();

    public static String claimsCreatedLogName = "claimsCreated";
    public static String claimsDeletedLogName = "claimsDeleted";
    public static String claimsExpiredLogName = "claimsExpired";
    public static String claimsModifiedLogName = "claimsModified";
    public static String eggsThrownLogName = "eggsThrown";
    public static String droppedItemsLogName = "droppedItems";
    public static String itemsPlacedInItemFramesLogName = "itemsPlacedInItemFrames";
    public static String itemsTakenOutOfItemFramesLogName = "itemsTakenOutOfItemFrames";
    public static String mcmmoRepairUseLogName = "mcmmoRepairUse";
    public static String cratePrizesLogName = "cratePrizes";
    public static String spawnLimitReachedLogName = "spawnLimitReached";
    public static String pickedUpItemsLogName = "pickedUpItems";
    public static String uiClicksLogName = "uiClicks";
    public static String itemsBrokenLogName = "itemsBroken";
    public static String numberOfClaimsNotificationLogName = "numberOfClaimsNotification";
    public static String itemsDespawnedLogName = "itemsDespawned";
    public static String farmLimiterLogName = "farmLimiter";
    public static String itemsDestroyedLogName = "itemsDestroyed";
    public static String tridentsLogName = "tridents";


    public static void initiate() {

        logNamesAndConfigPaths.put(claimsCreatedLogName, "Logging.ClaimsCreatedLog");
        logNamesAndConfigPaths.put(claimsDeletedLogName, "Logging.ClaimsDeletedLog");
        logNamesAndConfigPaths.put(claimsExpiredLogName, "Logging.ClaimsExpiredLog");
        logNamesAndConfigPaths.put(claimsModifiedLogName, "Logging.ClaimsModifiedLog");
        logNamesAndConfigPaths.put(eggsThrownLogName, "Logging.EggsThrownLog");
        logNamesAndConfigPaths.put(droppedItemsLogName, "Logging.DroppedItemsLog");
        logNamesAndConfigPaths.put(itemsPlacedInItemFramesLogName, "Logging.ItemsPlacedInItemFramesLog");
        logNamesAndConfigPaths.put(itemsTakenOutOfItemFramesLogName, "Logging.ItemsTakenOutOfItemFramesLog");
        logNamesAndConfigPaths.put(mcmmoRepairUseLogName, "Logging.MCMMORepairUseLog");
        logNamesAndConfigPaths.put(cratePrizesLogName, "Logging.CratePrizesLog");
        logNamesAndConfigPaths.put(spawnLimitReachedLogName, "Logging.SpawnLimitReachedLog");
        logNamesAndConfigPaths.put(pickedUpItemsLogName, "Logging.PickedUpItemsLog");
        logNamesAndConfigPaths.put(uiClicksLogName, "Logging.UIClicksLog");
        logNamesAndConfigPaths.put(itemsBrokenLogName, "Logging.ItemsBrokenLog");
        logNamesAndConfigPaths.put(numberOfClaimsNotificationLogName, "Logging.NumberOfClaimsNotification");
        logNamesAndConfigPaths.put(itemsDespawnedLogName, "Logging.ItemsDespawned");
        logNamesAndConfigPaths.put(farmLimiterLogName, "Logging.FarmLimiter");
        logNamesAndConfigPaths.put(itemsDestroyedLogName, "Logging.ItemsDestroyed");
        logNamesAndConfigPaths.put(tridentsLogName, "Logging.Tridents");

        List<String> directories = new ArrayList<>();
        directories.add("logs");
        directories.add("compressed-logs");
        directories.add("search-output");
        directories.add("temporary-files");

        for (String directory : directories)
            new File(Main.getInstance().getDataFolder() + "/" + directory).mkdir();

        for (Map.Entry<String, String> entry : logNamesAndConfigPaths.entrySet()) {
            Logging.WriteToFile(entry.getKey(), "");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                CheckAndCompress();
                CheckAndDeleteOld();
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    static void CheckAndCompress() {

        String[] logsNames = new File(Main.getInstance().getDataFolder() + "/logs").list();

        for (String logName : logsNames) {

            File log = new File(Main.getInstance().getDataFolder() + "/logs/" + logName);

            if (log.getName().startsWith(new Methods().getDateStringYYYYMMDD())) continue;

            try {
                if (new Methods().compressFile(log.getAbsolutePath(), log.getAbsolutePath().replace("\\logs\\", "\\compressed-logs\\").replace("/logs/", "/compressed-logs/").concat(".gz"))) {
                    if (!log.delete())
                        Main.getInstance().getLogger().warning("Something failed during deletion of the file " + log.getAbsolutePath());
                } else {
                    Main.getInstance().getLogger().warning("Something failed during file compression of the file " + log.getAbsolutePath());
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

    }

    static void CheckAndDeleteOld() {

        for (String fileName : new File(Main.getInstance().getDataFolder() + "/compressed-logs").list()) {

            try {

                File file = new File(Main.getInstance().getDataFolder() + "/compressed-logs/" + fileName);

                String fileNameWithoutDate = file.getName().substring(11, file.getName().indexOf(".txt.gz"));

                Integer numberOfLogsToKeepFromConfig = Main.getInstance().getConfig().getInt(Logging.logNamesAndConfigPaths.get(fileNameWithoutDate) + ".NumberOfLogsToKeep");

                if (numberOfLogsToKeepFromConfig == 0) throw new Throwable();

                Integer day = new Methods().getDateValuesFromStringYYYYMMDD(fileName.substring(0, 10))[0];
                Integer month = new Methods().getDateValuesFromStringYYYYMMDD(fileName.substring(0, 10))[1];
                Integer year = new Methods().getDateValuesFromStringYYYYMMDD(fileName.substring(0, 10))[2];
                LocalDate fileDateLD = LocalDate.of(year, month, day);

                Integer epochDayOfFileCreation = Math.toIntExact(fileDateLD.toEpochDay());
                Integer epochDayRightNow = Math.toIntExact(LocalDate.now().toEpochDay());

                if (epochDayOfFileCreation + numberOfLogsToKeepFromConfig < epochDayRightNow) {

                    if (file.delete())
                        Main.getInstance().getLogger().info(file.getName() + " deleted.");
                    else
                        Main.getInstance().getLogger().warning("Something failed during deletion of file " + file.getAbsolutePath());
                }

            } catch (Throwable throwable) {
                Main.getInstance().getLogger().warning(fileName + " has an invalid name. Please set it to yyyy-mm-dd format if you want the plugin to keep track of it and delete it after the specified time.");
            }

        }

    }

    public static String getBetterLocationString(Location location) {

        String worldName = location.getWorld().getName();

        String dimension = location.getWorld().getEnvironment().toString();

        String X = String.valueOf(location.getBlockX());
        String Y = String.valueOf(location.getBlockY());
        String Z = String.valueOf(location.getBlockZ());

        String message = "";
        message = message.concat("World: ");
        message = message.concat(worldName);
        message = message.concat(" Dimension: ");
        message = message.concat(dimension);
        message = message.concat(" X:");
        message = message.concat(X);
        message = message.concat(" Y:");
        message = message.concat(Y);
        message = message.concat(" Z:");
        message = message.concat(Z);

        return message;

    }

    public static void WriteToFile(String logName, String message) {

        new BukkitRunnable() {
            public void run() {
                try {

                    String messageCopy = message;
                    messageCopy = messageCopy.replace("\n", "\\n");
                    if (!messageCopy.equals("")) messageCopy = messageCopy.concat("\n");
//                    messageCopy = ChatColor.stripColor(messageCopy);

                    String logPath = "/logs/" + new Methods().getDateStringYYYYMMDD() + "-" + logName + ".txt";

                    FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + logPath, true);
                    writer.write(messageCopy);
                    writer.close();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

}