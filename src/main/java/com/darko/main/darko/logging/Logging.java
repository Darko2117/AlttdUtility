package com.darko.main.darko.logging;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class Logging {

    static Integer cachedDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

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
    public static String commandsWithLocationLogName = "commandsWithLocation";
    public static String droppedItemsOnDeathLogName = "droppedItemsOnDeath";
    public static String nicknameLogName = "nicknames";
    public static String petItemPickupLogName = "petItemPickup";
    public static String minecartsDestroyedLogName = "minecartsDestroyed";
    public static String lightningStrikesLogName = "lightningStrikes";
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
        logNamesAndConfigPaths.put(commandsWithLocationLogName, "Logging.CommandsWithLocation");
        logNamesAndConfigPaths.put(droppedItemsOnDeathLogName, "Logging.DroppedItemsOnDeath");
        logNamesAndConfigPaths.put(nicknameLogName, "Logging.Nicknames");
        logNamesAndConfigPaths.put(petItemPickupLogName, "Logging.PetItemPickup");
        logNamesAndConfigPaths.put(minecartsDestroyedLogName, "Logging.MinecartsDestroyed");
        logNamesAndConfigPaths.put(lightningStrikesLogName, "Logging.LightningStrikes");
        logNamesAndConfigPaths.put(tridentsLogName, "Logging.Tridents");

        List<String> directories = new ArrayList<>();
        directories.add("logs");
        directories.add("compressed-logs");
        directories.add("search-output");
        directories.add("temporary-files");

        for (String directory : directories)
            new File(AlttdUtility.getInstance().getDataFolder() + "/" + directory).mkdir();

        CreateAllBlankLogFiles();

        new BukkitRunnable() {
            @Override
            public void run() {
                CheckAndCompress();
                CheckAndDeleteOld();
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        new BukkitRunnable() {
            @Override
            public void run() {
                CompressIfDateChanged();
            }
        }.runTaskTimerAsynchronously(AlttdUtility.getInstance(), 1200, 1200);

    }

    static void CreateAllBlankLogFiles() {

        for (Map.Entry<String, String> entry : logNamesAndConfigPaths.entrySet()) {
            Logging.WriteToFile(entry.getKey(), "");
        }

    }

    static void CheckAndCompress() {

        String[] logsNames = new File(AlttdUtility.getInstance().getDataFolder() + "/logs").list();

        for (String logName : logsNames) {

            File log = new File(AlttdUtility.getInstance().getDataFolder() + "/logs/" + logName);

            if (log.getName().startsWith(new Methods().getDateStringYYYYMMDD())) continue;

            try {
                if (new Methods().compressFile(log.getAbsolutePath(), log.getAbsolutePath().replace("\\logs\\", "\\compressed-logs\\").replace("/logs/", "/compressed-logs/").concat(".gz"))) {
                    if (!log.delete())
                        AlttdUtility.getInstance().getLogger().warning("Something failed during deletion of the file " + log.getAbsolutePath());
                } else {
                    AlttdUtility.getInstance().getLogger().warning("Something failed during file compression of the file " + log.getAbsolutePath());
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

    }

    static void CheckAndDeleteOld() {

        for (String fileName : new File(AlttdUtility.getInstance().getDataFolder() + "/compressed-logs").list()) {

            try {

                File file = new File(AlttdUtility.getInstance().getDataFolder() + "/compressed-logs/" + fileName);

                String fileNameWithoutDate = file.getName().substring(11, file.getName().indexOf(".txt.gz"));

                Integer numberOfLogsToKeepFromConfig = AlttdUtility.getInstance().getConfig().getInt(Logging.logNamesAndConfigPaths.get(fileNameWithoutDate) + ".NumberOfLogsToKeep");

                if (numberOfLogsToKeepFromConfig == -1) numberOfLogsToKeepFromConfig = 999999999;

                if (numberOfLogsToKeepFromConfig == 0) throw new Throwable();

                Integer day = new Methods().getDateValuesFromStringYYYYMMDD(fileName.substring(0, 10))[0];
                Integer month = new Methods().getDateValuesFromStringYYYYMMDD(fileName.substring(0, 10))[1];
                Integer year = new Methods().getDateValuesFromStringYYYYMMDD(fileName.substring(0, 10))[2];
                LocalDate fileDateLD = LocalDate.of(year, month, day);

                Integer epochDayOfFileCreation = Math.toIntExact(fileDateLD.toEpochDay());
                Integer epochDayRightNow = Math.toIntExact(LocalDate.now().toEpochDay());

                if (epochDayOfFileCreation + numberOfLogsToKeepFromConfig < epochDayRightNow) {

                    if (file.delete())
                        AlttdUtility.getInstance().getLogger().info(file.getName() + " deleted.");
                    else
                        AlttdUtility.getInstance().getLogger().warning("Something failed during deletion of file " + file.getAbsolutePath());
                }

            } catch (Throwable throwable) {
                AlttdUtility.getInstance().getLogger().warning(fileName + " has an invalid name. Please set it to yyyy-mm-dd format if you want the plugin to keep track of it and delete it after the specified time.");
            }

        }

    }

    static void CompressIfDateChanged() {

        Integer dayNow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if (!cachedDayOfMonth.equals(dayNow)) {

            CheckAndCompress();
            CheckAndDeleteOld();
            CreateAllBlankLogFiles();

            cachedDayOfMonth = dayNow;

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

    public static Location getLocationFromBetterLocationString(String string) {

        string = string.substring(7);

        String worldName = string.substring(0, string.indexOf(" "));

        string = string.substring(string.indexOf(" ") + 1);
        string = string.substring(12);

        String dimension = string.substring(0, string.indexOf(" "));

        string = string.substring(string.indexOf(" ") + 1);
        string = string.substring(2);

        String X = string.substring(0, string.indexOf(" "));

        string = string.substring(string.indexOf(" ") + 1);
        string = string.substring(2);

        String Y = string.substring(0, string.indexOf(" "));

        string = string.substring(string.indexOf(" ") + 1);
        string = string.substring(2);

        String Z = string;

        return new Location(Bukkit.getWorld(worldName), Double.parseDouble(X), Double.parseDouble(Y), Double.parseDouble(Z));

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

                    FileWriter writer = new FileWriter(AlttdUtility.getInstance().getDataFolder() + logPath, true);
                    writer.write(messageCopy);
                    writer.close();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    static List<String> getArgumentListFromLogName(String logName) {

        List<String> arguments = new ArrayList<>();

        if (logName.equals("claimsCreated") || logName.equals("claimsDeleted") || logName.equals("claimsExpired") || logName.equals("claimsModified")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("LowestY:");
            arguments.add("Area:");

        } else if (logName.equals("eggsThrown")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Location:");
            arguments.add("ClaimOwner:");

        } else if (logName.equals("droppedItems")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

        } else if (logName.equals("itemsPlacedInItemFrames")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

        } else if (logName.equals("itemsTakenOutOfItemFrames")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

        } else if (logName.equals("mcmmoRepairUse")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");

        } else if (logName.equals("cratePrizes")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Items:");
            arguments.add("Commands:");
            arguments.add("Crate:");

        } else if (logName.equals("spawnLimitReached")) {

            arguments.add("Time:");
            arguments.add("EntityType:");
            arguments.add("Location:");
            arguments.add("ClaimOwner:");

        } else if (logName.equals("pickedUpItems")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

        } else if (logName.equals("uiClicks")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("InventoryName:");
            arguments.add("ClickedItem:");
            arguments.add("Location:");

        } else if (logName.equals("itemsBroken")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

        } else if (logName.equals("numberOfClaimsNotification")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("NumberOfClaims:");

        } else if (logName.equals("itemsDespawned")) {

            arguments.add("Time:");
            arguments.add("Item:");
            arguments.add("Location:");

        } else if (logName.equals("farmLimiter")) {

            arguments.add("Time:");
            arguments.add("Entity:");
            arguments.add("Location:");
            arguments.add("ClaimOwner:");

        } else if (logName.equals("itemsDestroyed")) {

            arguments.add("Time:");
            arguments.add("Item:");
            arguments.add("Location:");
            arguments.add("Cause:");

        } else if (logName.equals("commandsWithLocation")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Command:");
            arguments.add("Location:");

        } else if (logName.equals("droppedItemsOnDeath")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Killer:");
            arguments.add("Items:");
            arguments.add("Location:");

        } else if (logName.equals("nicknames")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Nickname:");
            arguments.add("WhoResponded:");
            arguments.add("Action:");

        } else if (logName.equals("petItemPickup")) {

            arguments.add("Time:");
            arguments.add("Pet:");
            arguments.add("Owner:");
            arguments.add("Item:");
            arguments.add("Location:");

        } else if (logName.equals("minecartsDestroyed")) {

            arguments.add("Time:");
            arguments.add("Attacker:");
            arguments.add("Location:");
            arguments.add("ClaimOwner:");

        } else if (logName.equals("lightningStrikes")) {

            arguments.add("Time:");
            arguments.add("Cause:");
            arguments.add("Location:");

        } else if (logName.equals("tridents")) {

            arguments.add("Time:");
            arguments.add("Player:");
            arguments.add("Trident:");
            arguments.add("Location:");
            arguments.add("Action:");
            arguments.add("Target:");

        }

        if (arguments.contains("Location:") || arguments.contains("Area:"))
            arguments.add("-radius:");

        return arguments;

    }

    static List<String> getAdditionalLogNames() {

        List<String> logNames = new ArrayList<>();

        for (String s : AlttdUtility.getInstance().getConfig().getStringList("AdditionalLogs")) {

            s = s.substring(s.indexOf("Name:") + 5);
            s = s.substring(0, s.indexOf(" "));

            logNames.add(s);

        }

        return logNames;

    }

}