package com.darko.main.common;

import com.darko.main.AlttdUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.time.LocalDate;
import java.util.zip.*;

public class Methods {

    public void sendConfigMessage(CommandSender receiver, String path) {

        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString(path)));

    }

    /*
     * I don't remember exactly why I made this, but I'm pretty sure that in some cases where a value in
     * the config is not set properly it can cause the whole config to reset to default. This tries
     * loading it before the plugin itself does, if it fails it disables the plugin and stops it from
     * resetting it.
     */
    public void checkConfig() {
        try {
            AlttdUtility.getInstance().saveDefaultConfig();
            new Yaml().load(new FileInputStream(AlttdUtility.getInstance().getDataFolder() + "/config.yml"));
            AlttdUtility.getInstance().reloadConfig();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            AlttdUtility.getInstance().getLogger().warning("Plugin encountered an error while trying to load the config, disabling...");
            Bukkit.getPluginManager().disablePlugin(AlttdUtility.getInstance());
        }
    }

    public String getDateStringDDMMYYYY() {

        Integer day = LocalDate.now().getDayOfMonth();
        Integer month = LocalDate.now().getMonthValue();
        Integer year = LocalDate.now().getYear();

        String date = "";
        if (day < 10) {
            date = date.concat("0");
        }
        date = date.concat(day.toString());
        date = date.concat("-");
        if (month < 10) {
            date = date.concat("0");
        }
        date = date.concat(month.toString());
        date = date.concat("-");
        date = date.concat(year.toString());

        return date;

    }

    public String getDateStringYYYYMMDD() {

        Integer day = LocalDate.now().getDayOfMonth();
        Integer month = LocalDate.now().getMonthValue();
        Integer year = LocalDate.now().getYear();

        String date = "";

        date = date.concat(year.toString());
        date = date.concat("-");

        if (month < 10) {
            date = date.concat("0");
        }
        date = date.concat(month.toString());
        date = date.concat("-");

        if (day < 10) {
            date = date.concat("0");
        }
        date = date.concat(day.toString());

        return date;

    }

    public Integer[] getDateValuesFromStringDDMMYYYY(String dateString) {

        Integer[] values = new Integer[3];

        values[0] = Integer.valueOf(dateString.substring(0, 2)); // day
        values[1] = Integer.valueOf(dateString.substring(3, 5)); // month
        values[2] = Integer.valueOf(dateString.substring(6, 10)); // year

        return values;

    }

    public Integer[] getDateValuesFromStringYYYYMMDD(String dateString) {

        Integer[] values = new Integer[3];

        values[0] = Integer.valueOf(dateString.substring(8, 10)); // day
        values[1] = Integer.valueOf(dateString.substring(5, 7)); // month
        values[2] = Integer.valueOf(dateString.substring(0, 4)); // year

        return values;

    }

    public Boolean compressFile(String inputPath, String outputPath) throws Throwable {

        FileOutputStream fos = new FileOutputStream(outputPath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(inputPath);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
        fis.close();
        fos.close();

        return true;

    }

    public Boolean uncompressFile(String inputPath, String outputPath) throws Throwable {

        File destDir = new File(outputPath);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(inputPath));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {

            File destFile = new File(destDir, zipEntry.getName());

            String destDirPath = destDir.getCanonicalPath();
            String destFilePath = destFile.getCanonicalPath();

            if (!destFilePath.startsWith(destDirPath + File.separator)) {
                throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
            }

            FileOutputStream fos = new FileOutputStream(destFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        return true;

    }

    public Boolean uncompressFileGZIP(String inputPath, String outputPath) throws Throwable {

        byte[] buffer = new byte[1024];

        FileInputStream fileIn = new FileInputStream(inputPath);

        GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);

        FileOutputStream fileOutputStream = new FileOutputStream(outputPath);

        int bytes_read;

        while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {

            fileOutputStream.write(buffer, 0, bytes_read);
        }

        gZIPInputStream.close();
        fileOutputStream.close();

        return true;

    }

    public void copyPasteFile(File file, File destination) throws Throwable {

        try (InputStream is = new FileInputStream(file); OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }

    }

    public String getServerJarPath() {

        String path = new File(".").getAbsolutePath();
        path = path.substring(0, path.length() - 1);
        return path;

    }

    public String serializeItemStack(ItemStack itemStack) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(1);

            dataOutput.writeObject(itemStack);

            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;

    }

    public ItemStack deserializeItemStack(String string) {

        try {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();

            return items[0];

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;

    }

    public String getTimeStringFromIntSeconds(Integer time) {

        Integer hours = time / 3600;
        Integer minutes = time % 3600 / 60;
        Integer seconds = time % 60;

        StringBuilder string = new StringBuilder();
        if (hours != 0) {
            if (hours == 1) {
                string.append(hours).append(" hour ");
            } else {
                string.append(hours).append(" hours ");
            }
        }
        if (minutes != 0) {
            if (minutes == 1) {
                string.append(minutes).append(" minute ");
            } else {
                string.append(minutes).append(" minutes ");
            }
        }
        if (seconds != 0) {
            if (seconds == 1) {
                string.append(seconds).append(" second");
            } else {
                string.append(seconds).append(" seconds");
            }
        }
        if (hours == 0 && minutes == 0 && seconds == 0) {
            string = new StringBuilder("less than a second");
        }

        return string.toString();

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

}
