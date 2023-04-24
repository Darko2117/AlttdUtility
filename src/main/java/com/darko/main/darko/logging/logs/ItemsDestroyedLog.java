package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ItemsDestroyedLog extends Log {

    public ItemsDestroyedLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Item", "");
                put("Location", "");
                put("Cause", "");
            }
        });

    }

}
