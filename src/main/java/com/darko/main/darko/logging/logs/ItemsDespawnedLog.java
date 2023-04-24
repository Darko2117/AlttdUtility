package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ItemsDespawnedLog extends Log {

    public ItemsDespawnedLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Item", "");
                put("Location", "");
            }
        });

    }

}
