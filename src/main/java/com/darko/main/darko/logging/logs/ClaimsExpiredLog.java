package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ClaimsExpiredLog extends Log {

    public ClaimsExpiredLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("LowestY", "");
                put("Area", "");
            }
        });

    }

}
