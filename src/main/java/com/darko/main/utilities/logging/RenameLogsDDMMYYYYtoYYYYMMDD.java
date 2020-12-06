package com.darko.main.utilities.logging;

import com.darko.main.Main;
import com.darko.main.other.Methods;

import java.io.File;

public class RenameLogsDDMMYYYYtoYYYYMMDD {

    public static void initiate() {

        Long startTime = System.currentTimeMillis();

        LoggingSearch.clearTemporaryFiles();

        for (File f : new File(Main.getInstance().getDataFolder() + "/compressed-logs/").listFiles()) {

            try {

                if (nameSwitch(f.getName()) == null) continue;
                if (!f.getName().endsWith(".gz")) continue;

                new Methods().uncompressFile(f.getAbsolutePath(), Main.getInstance().getDataFolder() + "/temporary-files");

                f.delete();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

        for (File f : new File(Main.getInstance().getDataFolder() + "/temporary-files/").listFiles()) {

            try {

                String newPath = Main.getInstance().getDataFolder() + "/temporary-files/" + nameSwitch(f.getName());

                f.renameTo(new File((newPath)));

                new Methods().compressFile(newPath, newPath.replace("/temporary-files/", "/compressed-logs/").concat(".gz"));

                f = new File(newPath);
                f.delete();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

        for (File f : new File(Main.getInstance().getDataFolder() + "/logs/").listFiles()) {

            try {

                if (nameSwitch(f.getName()) == null) continue;

                String newPath = Main.getInstance().getDataFolder() + "/logs/" + nameSwitch(f.getName());

                f.renameTo(new File(newPath));

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

        Long endTime = System.currentTimeMillis();
        Main.getInstance().getLogger().info("Renamed, took " + (endTime - startTime) + "ms.");

    }

    static String nameSwitch(String name) {

        try {

            Integer day = new Methods().getDateValuesFromStringDDMMYYYY(name)[0];
            Integer month = new Methods().getDateValuesFromStringDDMMYYYY(name)[1];
            Integer year = new Methods().getDateValuesFromStringDDMMYYYY(name)[2];

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

            return date + name.substring(10);

        } catch (Throwable throwable) {
            Main.getInstance().getLogger().warning("Error renaming file " + name);
        }

        return null;

    }

}
