package com.darko.main.other;

import com.darko.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.time.LocalDate;
import java.util.zip.*;

public class Methods {

    public static void sendConfigMessage(CommandSender receiver, String path) {

        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString(path)));

    }

    public static String getDateString() {

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

    public static Integer[] getDateValuesFromString(String dateString) {

        Integer[] values = new Integer[3];

        values[0] = Integer.valueOf(dateString.substring(0, 2)); //day
        values[1] = Integer.valueOf(dateString.substring(3, 5)); //month
        values[2] = Integer.valueOf(dateString.substring(6, 10)); //year

        return values;

    }

    public static Boolean compressFile(String inputPath, String outputPath) {

        try {

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

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }

    }

    public static Boolean uncompressFile(String inputPath, String outputPath) {

        try {

            File destDir = new File(outputPath);
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(inputPath));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
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

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }

    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) {

        try {

            File destFile = new File(destinationDir, zipEntry.getName());

            String destDirPath = destinationDir.getCanonicalPath();
            String destFilePath = destFile.getCanonicalPath();

            if (!destFilePath.startsWith(destDirPath + File.separator)) {
                throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
            }

            return destFile;

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }

    }

    public static void copyPasteFile(File file, File destination) {

        try {

            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(file);
                os = new FileOutputStream(destination);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } finally {
                is.close();
                os.close();
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}
