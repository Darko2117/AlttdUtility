package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class IllegalItemsLog extends Log {

    public IllegalItemsLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Item", "");
                put("ReplacedWithItem", "");
                put("Location", "");
                put("Event", "");
            }
        });

    }

}
