package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class DroppedItemsLog extends Log {

    public DroppedItemsLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Item", "");
                put("Location", "");
            }
        });

    }

}
