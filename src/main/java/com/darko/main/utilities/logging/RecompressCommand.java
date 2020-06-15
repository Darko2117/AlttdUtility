package com.darko.main.utilities.logging;

import com.darko.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class RecompressCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        sender.sendMessage(ChatColor.YELLOW + "Re compression of log files started.");

        new BukkitRunnable() {
            @Override
            public void run() {

                File[] files = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/compressed-logs").listFiles();

                for (File file : files) {

                    String filePath = file.getPath();
                    String destDirectory = file.getPath().substring(0, file.getPath().indexOf(file.getName()));

                    try {

                        Unzip(filePath, destDirectory);
                        Main.getInstance().getLogger().info(file.getName() + " unzipped!");
                        file.delete();
                        Main.getInstance().getLogger().info(file.getName() + " deleted!");

                    } catch (Exception ex) {
                        // some errors occurred
                        ex.printStackTrace();
                    }

                }

                files = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/compressed-logs").listFiles();
                sender.sendMessage(ChatColor.YELLOW + "Re compression 50% done.");

                for (File file : files) {

                    try {

                        String zipFileName = file.getName().concat(".gz");

                        FileOutputStream fos = new FileOutputStream(zipFileName);
                        ZipOutputStream zos = new ZipOutputStream(fos);

                        zos.putNextEntry(new ZipEntry(file.getName()));
                        byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
                        zos.write(bytes, 0, bytes.length);
                        zos.closeEntry();
                        zos.close();

                        Files.move(Paths.get(zipFileName), Paths.get(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder() + "/compressed-logs/" + zipFileName));

                        Main.getInstance().getLogger().info(file.getName() + " compressed!");

                        file.delete();

                        Main.getInstance().getLogger().info(file.getName() + " deleted!");

                    } catch (Exception e) {
                        Main.getInstance().getLogger().warning("Something failed during file compression, yikes...");
                    }

                }

                Main.getInstance().getLogger().info("Re compressed " + files.length + " files!");
                sender.sendMessage(ChatColor.GREEN + "Re compressed " + files.length + " files!");

            }
        }.runTaskAsynchronously(Main.getInstance());

        return false;
    }

    private static final int BUFFER_SIZE = 4096;

    private static void Unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}
