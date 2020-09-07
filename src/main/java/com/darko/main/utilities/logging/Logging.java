package com.darko.main.utilities.logging;

import com.darko.main.Main;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public static String date = "";

    public static void initiate() {

        updateDate();

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

        List<String> directories = new ArrayList<>();
        directories.add("logs");
        directories.add("compressed-logs");

        for (String directory : directories) {
            new File(Main.getInstance().getDataFolder() + "/" + directory).mkdir();
        }

        for (Map.Entry<String, String> entry : logNamesAndConfigPaths.entrySet()) {
            Logging.WriteToFile(entry.getKey(), "");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                updateDate();
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                CheckAndCompress();
                CheckAndDeleteOld();
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    static void updateDate() {

        Integer day = LocalDate.now().getDayOfMonth();
        Integer month = LocalDate.now().getMonthValue();
        Integer year = LocalDate.now().getYear();

        String tempDate = "";
        if (day < 10) {
            tempDate = tempDate.concat("0");
        }
        tempDate = tempDate.concat(day.toString());
        tempDate = tempDate.concat("-");
        if (month < 10) {
            tempDate = tempDate.concat("0");
        }
        tempDate = tempDate.concat(month.toString());
        tempDate = tempDate.concat("-");
        tempDate = tempDate.concat(year.toString());

        date = tempDate;

    }

    static void CheckAndCompress() {

        for (String fileName : new File(Main.getInstance().getDataFolder() + "/logs").list()) {

            if (!fileName.startsWith(date)) {

                try {

                    File file = new File(Main.getInstance().getDataFolder() + "/logs/" + fileName);
                    String zipFileName = file.getName().concat(".gz");

                    FileOutputStream fos = new FileOutputStream(zipFileName);
                    ZipOutputStream zos = new ZipOutputStream(fos);

                    zos.putNextEntry(new ZipEntry(file.getName()));
                    byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                    zos.close();

                    new File(Main.getInstance().getDataFolder() + "/compressed-logs").mkdir();

                    Files.move(Paths.get(zipFileName), Paths.get(Main.getInstance().getDataFolder() + "/compressed-logs/" + zipFileName));

                    file.delete();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    Main.getInstance().getLogger().warning("Something failed during file compression of the file " + fileName);
                }

            }

        }

    }

    static void CheckAndDeleteOld() {

        for (String fileName : new File(Main.getInstance().getDataFolder() + "/compressed-logs").list()) {

            try {

                File file = new File(Main.getInstance().getDataFolder() + "/compressed-logs/" + fileName);

                StringBuilder fileDate = new StringBuilder(file.getName());

                Integer fileDay = Integer.valueOf(fileDate.substring(0, 2));
                Integer fileMonth = Integer.valueOf(fileDate.substring(3, 5));
                Integer fileYear = Integer.valueOf(fileDate.substring(6, 10));

                LocalDate fileDateLD = LocalDate.of(fileYear, fileMonth, fileDay);

                String fileNameWithoutDate = file.getName().substring(11);
                fileNameWithoutDate = fileNameWithoutDate.substring(0, fileNameWithoutDate.indexOf(".txt.gz"));

                Integer numberOfLogsToKeepFromConfig = Main.getInstance().getConfig().getInt(Logging.logNamesAndConfigPaths.get(fileNameWithoutDate) + ".NumberOfLogsToKeep");

                if (numberOfLogsToKeepFromConfig == 0) throw new Throwable();

                Integer epochDayOfFileCreation = Math.toIntExact(fileDateLD.toEpochDay());
                Integer epochDayRightNow = Math.toIntExact(LocalDate.now().toEpochDay());

                if (epochDayOfFileCreation + numberOfLogsToKeepFromConfig <= epochDayRightNow) {
                    file.delete();
                    Main.getInstance().getLogger().info(file.getName() + " deleted.");
                }

            } catch (Throwable throwable) {
                Main.getInstance().getLogger().warning(fileName + " has an invalid name. Please set it to dd-mm-yyyy format if you want the plugin to keep track of it and delete it after the specified time.");
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
                    String logPath = "/logs/" + date + "-" + logName + ".txt";
                    FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + logPath, true);
                    writer.write(message);
                    if (!message.equals("")) writer.write("\n");
                    writer.close();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

}