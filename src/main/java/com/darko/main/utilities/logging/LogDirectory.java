package com.darko.main.utilities.logging;

import com.darko.main.Main;

import java.io.File;

public class LogDirectory {

    public static void CreateLogDirectory() {
        File directory = new File(Main.getInstance().getDataFolder() + "/logs");
        directory.mkdir();
    }

}
