package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class DroppedItemsOnDeathLog extends Log {

    public DroppedItemsOnDeathLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("Killer", "");
                put("Item", "");
                put("Location", "");
            }
        });

    }

}
