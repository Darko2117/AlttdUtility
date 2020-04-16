package com.darko.main.utilities.logging;

import com.darko.main.Main;
import de.schlichtherle.io.FileOutputStream;
import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Logging {

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

    public static void Initiate() {

        try {

            Main.getInstance().saveDefaultConfig();

            File directory = new File(Main.getInstance().getDataFolder() + "/logs");
            directory.mkdir();

            UpdateLogDates();

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

                File dataFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(),
                        fileName);
                if (!dataFile.exists()) {
                    dataFile.createNewFile();
                }

            }

        } catch (IOException e) {
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


    public static void UpdateLogDates() {

        StringBuilder date = new StringBuilder();
        date.append(LocalDate.now().getDayOfMonth() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear() + "-");

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

    }


    /*public static void CheckAndCompress() {

        StringBuilder date = new StringBuilder();
        date.append(LocalDate.now().getDayOfMonth() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear() + "-");

        File logsFolder = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(),
                "/logs");
        String[] fileNames = logsFolder.list();

        for (Integer i = 0; i < fileNames.length; i++) {

            if (!fileNames[i].startsWith(date.toString())) {

                try {

                    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(),
                            "/logs/" + fileNames[i]);
                    String zipFileName = file.getName().concat(".zip");

                    FileOutputStream fos = new FileOutputStream(zipFileName);
                    ZipOutputStream zos = new ZipOutputStream(fos);

                    zos.putNextEntry(new ZipEntry(file.getName()));

                    byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                    zos.close();

                } catch (Exception e) {
                }
            }
        }
    }*/
}