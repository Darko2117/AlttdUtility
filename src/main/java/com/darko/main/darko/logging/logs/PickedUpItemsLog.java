package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class PickedUpItemsLog extends Log {

    public PickedUpItemsLog() {

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
