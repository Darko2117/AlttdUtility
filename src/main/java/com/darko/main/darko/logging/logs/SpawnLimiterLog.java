package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class SpawnLimiterLog extends Log {

    public SpawnLimiterLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("EntityType", "");
                put("Location", "");
                put("ClaimOwner", "");
            }
        });

    }



}
