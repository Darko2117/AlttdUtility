package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class TridentsLog extends Log {

    public TridentsLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Trident", "");
                put("Location", "");
                put("Action", "");
                put("Target", "");
            }
        });

    }

}
