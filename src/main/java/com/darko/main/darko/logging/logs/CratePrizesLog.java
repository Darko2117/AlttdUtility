package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class CratePrizesLog extends Log {

    public CratePrizesLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Items", "");
                put("Commands", "");
                put("Crate", "");
            }
        });

    }

}
