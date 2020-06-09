package com.darko.main.utilities.logging;

import com.darko.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TemporaryLogRecompressCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        new BukkitRunnable() {
            @Override
            public void run() {

                File[] files = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/compressed-logs").listFiles();

                for (File file : files) {

                    String filePath = file.getPath();
                    String destDirectory = file.getPath().substring(0, file.getPath().indexOf(file.getName()));

                    UnzipUtility unzipper = new UnzipUtility();
                    try {

                        unzipper.unzip(filePath, destDirectory);
                        Main.getInstance().getLogger().info(file.getName() + " unzipped!");
                        file.delete();
                        Main.getInstance().getLogger().info(file.getName() + " deleted!");

                    } catch (Exception ex) {
                        // some errors occurred
                        ex.printStackTrace();
                    }

                }

                files = new File(Bukkit.getServer().getPluginManager().getPlugin("AlttdUtility").getDataFolder(), "/compressed-logs").listFiles();

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

            }
        }.runTaskAsynchronously(Main.getInstance());

        return false;
    }
}
