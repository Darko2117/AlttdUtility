package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ItemsPlacedInItemFramesLog extends Log {

    public ItemsPlacedInItemFramesLog() {

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
