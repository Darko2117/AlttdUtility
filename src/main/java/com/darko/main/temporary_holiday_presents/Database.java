package com.darko.main.temporary_holiday_presents;

import java.util.ArrayList;
import java.util.List;

public class Database {

    public static void createPresentsTable(){

        try {
            String tableQuery = "CREATE TABLE IF NOT EXISTS presents("
                    + "ID INT NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (ID))";
            com.darko.main.database.Database.connection.prepareStatement(tableQuery).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        List<String> columns = new ArrayList<>();
        columns.add("ALTER TABLE presents ADD UUID TEXT NOT NULL");
        columns.add("ALTER TABLE presents ADD Item TEXT NOT NULL");
        columns.add("ALTER TABLE presents ADD Claimed Boolean NOT NULL");
        for (String string : columns) {
            try {
                com.darko.main.database.Database.connection.prepareStatement(string).executeUpdate();
            } catch (Throwable ignored) {
            }
        }

    }

}
