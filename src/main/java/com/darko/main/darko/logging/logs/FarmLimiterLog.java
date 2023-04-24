package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class FarmLimiterLog extends Log {

    public FarmLimiterLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Entity", "");
                put("Location", "");
                put("ClaimOwner", "");
            }
        });

    }

}
