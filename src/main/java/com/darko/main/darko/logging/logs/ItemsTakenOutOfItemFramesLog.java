package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ItemsTakenOutOfItemFramesLog extends Log {

    public ItemsTakenOutOfItemFramesLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("Item", "");
                put("Location", "");
            }
        });

    }

}
