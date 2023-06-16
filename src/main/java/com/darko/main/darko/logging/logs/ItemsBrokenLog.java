package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ItemsBrokenLog extends Log {

    public ItemsBrokenLog() {

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
