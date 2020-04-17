package com.darko.main.utilities.logging;

import com.darko.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Logging {

    public static HashMap<String, String> LogNamesAndConfigPaths = new LinkedHashMap<>();

    public static String claimCreatedLogName = null;
    public static String claimDeletedLogName = null;
    public static String claimExpiredLogName = null;
    public static String claimModifiedLogName = null;
    public static String eggLogLogName = null;
    public static String droppedItemsLogName = null;
    public static String itemPlacedInItemFrameLogName = null;
    public static String mcmmoRepairUseLogName = null;
    public static String cratePrizeLogName = null;
    public static String spawnLimitReachedLogName = null;

    public static StringBuilder date = new StringBuilder();

    public static void Initiate() {

        new BukkitRunnable() {
            @Override
            public void run() {

                try {

                    Main.getInstance().saveDefaultConfig();

                    File directory = new File(Main.getInstance().getDataFolder() + "/logs");
                    directory.mkdir();

                    File compressedDirectory = new File(Main.getInstance().getDataFolder() + "/compressed-logs");
                    compressedDirectory.mkdir();

                    UpdateLogDates();
                    CheckAndCompress();
                    CheckAndDeleteOld();

                    List<String> dataFilesNames = new ArrayList<>();

                    dataFilesNames.add(claimCreatedLogName);
                    dataFilesNames.add(claimDeletedLogName);
                    dataFilesNames.add(claimExpiredLogName);
                    dataFilesNames.add(claimModifiedLogName);
                    dataFilesNames.add(eggLogLogName);
                    dataFilesNames.add(droppedItemsLogName);
                    dataFilesNames.add(itemPlacedInItemFrameLogName);
                    dataFilesNames.add(mcmmoRepairUseLogName);
                    dataFilesNames.add(cratePrizeLogName);
                    dataFilesNames.add(spawnLimitReachedLogName);

                    for (String fileName : dataFilesNames) {

                        File dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), fileName);
                        if (!dataFile.exists()) {
                            dataFile.createNewFile();
                        }

                    }

                } catch (IOException e) {
                }

            }
        }.runTaskAsynchronously(Main.getInstance());

    }


    public static void UpdateLogDates() {

        Integer day = LocalDate.now().getDayOfMonth();
        Integer month = LocalDate.now().getMonthValue();
        Integer year = LocalDate.now().getYear();

        StringBuilder format = new StringBuilder();

        if (day < 10) {
            format.append("0");
        }
        format.append(day + "-");
        if (month < 10) {
            format.append("0");
        }
        format.append(month + "-" + year + "-");

        date.replace(0, date.length(), format.toString());

        claimCreatedLogName = "/logs/" + date + "claim-created-log.txt";
        claimDeletedLogName = "/logs/" + date + "claim-deleted-log.txt";
        claimExpiredLogName = "/logs/" + date + "claim-expired-log.txt";
        claimModifiedLogName = "/logs/" + date + "claim-modified-log.txt";
        eggLogLogName = "/logs/" + date + "egg-log.txt";
        droppedItemsLogName = "/logs/" + date + "dropped-items-log.txt";
        itemPlacedInItemFrameLogName = "/logs/" + date + "item-placed-in-item-frame-log.txt";
        mcmmoRepairUseLogName = "/logs/" + date + "mcmmo-repair-use-log.txt";
        cratePrizeLogName = "/logs/" + date + "crate-prize-log.txt";
        spawnLimitReachedLogName = "/logs/" + date + "spawn-limit-reached-log.txt";

        LogNamesAndConfigPaths.put(claimCreatedLogName.substring(17), "Logging.ClaimCreatedLog");
        LogNamesAndConfigPaths.put(claimDeletedLogName.substring(17), "Logging.ClaimDeletedLog");
        LogNamesAndConfigPaths.put(claimExpiredLogName.substring(17), "Logging.ClaimExpiredLog");
        LogNamesAndConfigPaths.put(claimModifiedLogName.substring(17), "Logging.ClaimModifiedLog");
        LogNamesAndConfigPaths.put(eggLogLogName.substring(17), "Logging.EggLog");
        LogNamesAndConfigPaths.put(droppedItemsLogName.substring(17), "Logging.DroppedItemsLog");
        LogNamesAndConfigPaths.put(itemPlacedInItemFrameLogName.substring(17), "Logging.ItemPlacedInItemFrameLog");
        LogNamesAndConfigPaths.put(mcmmoRepairUseLogName.substring(17), "Logging.MCMMORepairUseLog");
        LogNamesAndConfigPaths.put(cratePrizeLogName.substring(17), "Logging.CratePrizeLog");
        LogNamesAndConfigPaths.put(spawnLimitReachedLogName.substring(17), "Logging.SpawnLimitReachedLog");
    }


    public static void CheckAndCompress() {

        File logsFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/logs");
        String[] fileNames = logsFolder.list();

        for (Integer i = 0; i < fileNames.length; i++) {

            if (!fileNames[i].startsWith(date.toString())) {

                try {

                    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/logs/" + fileNames[i]);
                    String zipFileName = file.getName().concat(".zip");

                    FileOutputStream fos = new FileOutputStream(zipFileName);
                    ZipOutputStream zos = new ZipOutputStream(fos);

                    zos.putNextEntry(new ZipEntry(file.getName()));
                    byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                    zos.close();

                    File directory = new File(Main.getInstance().getDataFolder() + "/compressed-logs");
                    if (!directory.exists()) {
                        directory.mkdir();
                    }

                    Path temp = Files.move(Paths.get(zipFileName), Paths.get(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder() + "/compressed-logs/" + zipFileName));

                    File fileDelete = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/logs/" + fileNames[i]);

                    fileDelete.delete();

                } catch (Exception e) {
                    Main.getInstance().getLogger().warning("Something failed during file compression, yikes...");
                }
            }
        }
    }

    public static void CheckAndDeleteOld() {

        File logsFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/compressed-logs");
        String[] fileNames = logsFolder.list();

        for (Integer i = 0; i < fileNames.length; i++) {

            try {

                File file = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/compressed-logs/" + fileNames[i]);

                StringBuilder fileDate = new StringBuilder(file.getName());

                Integer fileDay = Integer.valueOf(fileDate.substring(0, 2));
                Integer fileMonth = Integer.valueOf(fileDate.substring(3, 5));
                Integer fileYear = Integer.valueOf(fileDate.substring(6, 10));

                LocalDate fileDateLD = LocalDate.of(fileYear, fileMonth, fileDay);

                String fileNameWithoutDate = file.getName().substring(11);
                fileNameWithoutDate = fileNameWithoutDate.substring(0, fileNameWithoutDate.indexOf(".zip"));

                Integer numberOfLogsToKeepFromConfig = Main.getInstance().getConfig().getInt(Logging.LogNamesAndConfigPaths.get(fileNameWithoutDate) + ".NumberOfLogsToKeep");

                Long temp1 = fileDateLD.toEpochDay();
                Long temp2 = LocalDate.now().toEpochDay();

                Integer epochDayOfFileCreation = temp1.intValue();
                Integer epochDayRightNow = temp2.intValue();

                if(epochDayOfFileCreation + numberOfLogsToKeepFromConfig <= epochDayRightNow) {
                    file.delete();
                    Main.getInstance().getLogger().info(file.getName() + " deleted.");
                }

            } catch (Exception e) {
                Main.getInstance().getLogger().warning(fileNames[i] + " has an invalid name. Please set it to dd-mm-yyyy format if you want the plugin to keep track of it and delete it after the specified time.");
            }

        }

    }

    public static String getBetterLocationString(Location location) {

        StringBuilder message = new StringBuilder();

        String X = String.valueOf(location.getBlockX());
        String Y = String.valueOf(location.getBlockY());
        String Z = String.valueOf(location.getBlockZ());

        String dimension = location.getWorld().getEnvironment().toString();
        String worldName = location.getWorld().getName();

        message.append("World: " + worldName + " Dimension: " + dimension + " X:" + X + " Y:" + Y + " Z:" + Z);

        return message.toString();
    }


    public static void WriteToFile(String path, String message) {

        new BukkitRunnable() {
            public void run() {
                try {
                    UpdateLogDates();
                    FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + path, true);
                    writer.write(message + "\n");
                    writer.close();
                } catch (IOException e) {
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }

}