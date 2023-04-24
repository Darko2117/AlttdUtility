package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class LightningStrikesLog extends Log {

    public LightningStrikesLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Cause", "");
                put("Location", "");
            }
        });

    }

}
