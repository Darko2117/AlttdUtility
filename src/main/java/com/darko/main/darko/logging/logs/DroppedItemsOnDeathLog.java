package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class DroppedItemsOnDeathLog extends Log {

    public DroppedItemsOnDeathLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("DeathMessage", "");
                put("Item", "");
                put("Location", "");
            }
        });

    }

}
