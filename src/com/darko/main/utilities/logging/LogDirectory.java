package com.darko.main.utilities.logging;

import java.io.File;

import com.darko.main.Main;

public class LogDirectory {

    public static void CreateLogDirectory() {
        File directory = new File(Main.getInstance().getDataFolder() + "/logs");
        directory.mkdir();
    }

}
