package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ItemsTakenOutOfItemFramesLog extends Log {

    public ItemsTakenOutOfItemFramesLog() {

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
